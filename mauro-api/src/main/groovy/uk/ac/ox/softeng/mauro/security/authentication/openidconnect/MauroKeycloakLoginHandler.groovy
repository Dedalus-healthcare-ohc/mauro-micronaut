package uk.ac.ox.softeng.mauro.security.authentication.openidconnect

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import io.micronaut.context.annotation.Replaces
import io.micronaut.core.annotation.Nullable
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.cookie.Cookie
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.authentication.AuthenticationResponse
import io.micronaut.security.config.RedirectConfiguration
import io.micronaut.security.config.RedirectService
import io.micronaut.security.errors.PriorToLoginPersistence
import io.micronaut.security.oauth2.endpoint.token.response.IdTokenLoginHandler
import io.micronaut.security.token.cookie.AccessTokenCookieConfiguration
import jakarta.inject.Inject
import jakarta.inject.Singleton
import uk.ac.ox.softeng.mauro.security.authentication.MauroSessionLoginHandler

@Singleton
@Slf4j
@CompileStatic
@Replaces(IdTokenLoginHandler)
class MauroKeycloakLoginHandler extends IdTokenLoginHandler  {

    public static final String OAUTH = '/oauth/'

    @Inject
    MauroSessionLoginHandler mauroSessionLoginHandler

    RedirectConfiguration redirectConfiguration
    PriorToLoginPersistence priorToLoginPersistence

    /**
     * @param accessTokenCookieConfiguration Access token cookie configuration
     * @param redirectConfiguration Redirect configuration
     * @param redirectService Redirect service
     * @param priorToLoginPersistence The prior to login persistence strategy
     */
    MauroKeycloakLoginHandler(AccessTokenCookieConfiguration accessTokenCookieConfiguration, RedirectConfiguration redirectConfiguration,
                              RedirectService redirectService,
    @Nullable PriorToLoginPersistence<HttpRequest<?>, MutableHttpResponse<?>> priorToLoginPersistence) {
       super(accessTokenCookieConfiguration, redirectConfiguration, redirectService, priorToLoginPersistence)
       this.redirectConfiguration = redirectConfiguration
        this.priorToLoginPersistence = priorToLoginPersistence
    }

    @Override
    MutableHttpResponse<?> loginSuccess(Authentication authentication, HttpRequest<?> request) {
        if (!isOauthRequest(request)) {
            mauroSessionLoginHandler.loginSuccess(authentication, request)
        }else {
            List<Cookie> cookies = super.getCookies(authentication, request)
            super.applyCookies(createSuccessResponse(request), cookies);
        }
    }

    @Override
    MutableHttpResponse<?> loginFailed(AuthenticationResponse authenticationFailed, HttpRequest<?> request) {
        try {
            if (loginFailure == null || loginFailure == '/') {
                return HttpResponse.unauthorized();
            }
            URI location = new URI(loginFailure);
            return HttpResponse.seeOther(location);
        } catch (URISyntaxException e) {
            return HttpResponse.serverError();
        }
    }

    boolean isOauthRequest(HttpRequest<?> request){
        request.path.contains(OAUTH)
    }
}

