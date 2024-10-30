package uk.ac.ox.softeng.mauro.security.authentication.openidconnect

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import io.micronaut.context.annotation.Replaces
import io.micronaut.core.convert.value.MutableConvertibleValues
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpResponse
import io.micronaut.security.config.RedirectConfiguration
import io.micronaut.security.config.RedirectService
import io.micronaut.security.filters.SecurityFilter
import io.micronaut.security.token.cookie.AccessTokenCookieConfiguration
import io.micronaut.security.token.cookie.RefreshTokenCookieConfiguration
import io.micronaut.security.token.cookie.TokenCookieClearerLogoutHandler
import io.micronaut.session.Session
import io.micronaut.session.http.HttpSessionFilter
import jakarta.inject.Singleton

@Singleton
@Slf4j
@CompileStatic
@Replaces(TokenCookieClearerLogoutHandler)
class MauroKeycloakLogoutHandler extends TokenCookieClearerLogoutHandler {
    /**
     * @param accessTokenCookieConfiguration JWT Cookie Configuration
     * @param refreshTokenCookieConfiguration Refresh token cookie configuration
     * @param redirectConfiguration Redirect configuration
     * @param redirectService Redirection Service
     */
    MauroKeycloakLogoutHandler(AccessTokenCookieConfiguration accessTokenCookieConfiguration, RefreshTokenCookieConfiguration refreshTokenCookieConfiguration, RedirectConfiguration redirectConfiguration, RedirectService redirectService) {
        super(accessTokenCookieConfiguration, refreshTokenCookieConfiguration, redirectConfiguration, redirectService)
    }

    MutableHttpResponse<?> logout(HttpRequest<?> request) {
        if (isSessionLogout(request)) {
            sessionLogout(request)
        } else {
            super.logout(request)
        }
    }

    private MutableHttpResponse<?> sessionLogout(HttpRequest<?> request) {
        MutableConvertibleValues<Object> attrs = request.getAttributes();
        Optional<Session> existing = attrs.get(HttpSessionFilter.SESSION_ATTRIBUTE, Session.class);
        if (existing.isPresent()) {
            Session session = existing.get();
            log.debug("Removing Session  {} ", session.toString())
            session.remove(SecurityFilter.AUTHENTICATION);
        }
        try {
            HttpResponse.ok()
        } catch (URISyntaxException e) {
            return HttpResponse.serverError();
        }
    }

    boolean isSessionLogout(HttpRequest<?> httpRequest) {
        httpRequest.getAttributes().findAll { HttpSessionFilter.SESSION_ATTRIBUTE }
    }
}

