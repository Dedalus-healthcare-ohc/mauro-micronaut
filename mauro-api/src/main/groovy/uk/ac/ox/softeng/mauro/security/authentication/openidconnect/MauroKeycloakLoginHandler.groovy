package uk.ac.ox.softeng.mauro.security.authentication.openidconnect

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import io.micronaut.context.annotation.Replaces
import io.micronaut.core.annotation.Nullable
import io.micronaut.http.HttpRequest
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
import uk.ac.ox.softeng.mauro.persistence.cache.ItemCacheableRepository.CatalogueUserCacheableRepository
import uk.ac.ox.softeng.mauro.security.AccessControlService
import uk.ac.ox.softeng.mauro.security.authentication.MauroSessionLoginHandler

@Singleton
@Slf4j
@CompileStatic
@Replaces(IdTokenLoginHandler)
class MauroKeycloakLoginHandler extends IdTokenLoginHandler {

    @Inject
    AccessControlService accessControlService

    @Inject
    CatalogueUserCacheableRepository catalogueUserCacheableRepository

    @Inject
    MauroSessionLoginHandler mauroSessionLoginHandler

    /**
     * @param accessTokenCookieConfiguration Access token cookie configuration
     * @param redirectConfiguration Redirect configuration
     * @param redirectService Redirect service
     * @param priorToLoginPersistence The prior to login persistence strategy
     */
    MauroKeycloakLoginHandler(AccessTokenCookieConfiguration accessTokenCookieConfiguration, RedirectConfiguration redirectConfiguration, RedirectService redirectService
            ) {
        super(accessTokenCookieConfiguration, redirectConfiguration, redirectService, null)
    }

    @Override
    MutableHttpResponse<?> loginSuccess(Authentication authentication, HttpRequest<?> request) {
        log.debug(">>>>>>>>>>>>>. loginsuccess ")
        if (!request.path.contains('/oauth/')) {
            mauroSessionLoginHandler.loginSuccess(authentication, request)
        }else {
            List<Cookie> cookies = super.getCookies(authentication, request)
            super.applyCookies(createSuccessResponse(request), cookies);
            //super.loginSuccess(authentication, request)

        }
    }

    @Override
    MutableHttpResponse<?> loginFailed(AuthenticationResponse authenticationFailed, HttpRequest<?> request) {
        if (!request.path.contains('/oauth/')) {
            mauroSessionLoginHandler.loginFailed(authenticationFailed, request)
        }
     //   MutableHttpResponse defaultResponse = super.loginFailed(authenticationFailed, request)
//        if (defaultResponse.status == HttpStatus.OK) {
//            log.debug 'Login failed'
//            return HttpResponse.unauthorized()
//        } else {
//            defaultResponse
//        }
    }


//    private MutableHttpResponse<?> createSuccessResponse(HttpRequest<?> request) {
//        try {
//            if (loginSuccess == null) {
//                return HttpResponse.ok();
//            }
//            MutableHttpResponse<?> response = HttpResponse.status(HttpStatus.SEE_OTHER);
//            ThrowingSupplier<URI, URISyntaxException> uriSupplier = () -> new URI(loginSuccess);
//            if (priorToLoginPersistence != null) {
//                Optional<URI> originalUri = priorToLoginPersistence.getOriginalUri(request, response);
//                if (originalUri.isPresent()) {
//                    uriSupplier = originalUri::get;
//                }
//            }
//            response.getHeaders().location(uriSupplier.get());
//            return response;
//        } catch (URISyntaxException e) {
//            return HttpResponse.serverError();
//        }
//    }

}

