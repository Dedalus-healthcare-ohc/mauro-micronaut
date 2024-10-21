package uk.ac.ox.softeng.mauro.security.authentication

import groovy.util.logging.Slf4j
import io.micronaut.core.annotation.NonNull
import io.micronaut.core.annotation.Nullable
import io.micronaut.core.util.functional.ThrowingSupplier
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpResponse
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.authentication.AuthenticationResponse
import io.micronaut.security.config.RedirectConfiguration
import io.micronaut.security.errors.PriorToLoginPersistence
import io.micronaut.session.Session
import io.micronaut.session.SessionStore
import io.micronaut.session.http.SessionForRequest
import jakarta.inject.Inject
import jakarta.inject.Singleton
import uk.ac.ox.softeng.mauro.persistence.cache.ItemCacheableRepository.CatalogueUserCacheableRepository
import uk.ac.ox.softeng.mauro.security.AccessControlService

@Singleton
@Slf4j
class MauroSessionLoginHandler {

    @Inject
    AccessControlService accessControlService

    @Inject
    CatalogueUserCacheableRepository catalogueUserCacheableRepository

    @Inject
    SessionStore sessionStore


    MutableHttpResponse<?> loginSuccess(Authentication authentication, HttpRequest<?> request,
                                        RedirectConfiguration redirectConfiguration,
                                        PriorToLoginPersistence priorToLoginPersistence) {

        saveAuthenticationInSession(authentication, request);
        return loginSuccessResponse(request, redirectConfiguration, priorToLoginPersistence);
        //  MutableHttpResponse defaultResponse = super.loginSuccess(authentication, request)
        if (defaultResponse.status == HttpStatus.OK) {
            log.debug 'Successful login, returning Authentication'
            return HttpResponse.ok(catalogueUserCacheableRepository.readById((UUID) authentication.attributes.id))
        } else {
            defaultResponse
        }

    }


    MutableHttpResponse<?> loginFailed(AuthenticationResponse authenticationFailed, HttpRequest<?> request) {
        MutableHttpResponse defaultResponse = super.loginFailed(authenticationFailed, request)
        if (defaultResponse.status == HttpStatus.OK) {
            log.debug 'Login failed'
            return HttpResponse.unauthorized()
        } else {
            defaultResponse
        }

    }

    @NonNull
    private MutableHttpResponse<?> loginSuccessResponse(HttpRequest<?> request,
                                                        RedirectConfiguration redirectConfiguration,
                                                        PriorToLoginPersistence priorToLoginPersistence) {
        if (redirectConfiguration.loginSuccess == null) {
            return HttpResponse.ok()
        }
        try {
            MutableHttpResponse<?> response = HttpResponse.status(io.micronaut.http.HttpStatus.SEE_OTHER);
            ThrowingSupplier<URI, URISyntaxException> uriSupplier =
                    loginSuccessUriSupplier(redirectConfiguration.loginSuccess, request, response, priorToLoginPersistence)

            response.getHeaders().location(uriSupplier).get();
            return response;
        } catch (URISyntaxException e) {
            return HttpResponse.serverError();
        }
    }

    @NonNull
    private ThrowingSupplier<URI, URISyntaxException> loginSuccessUriSupplier(@NonNull String loginSuccess,
                                                                              HttpRequest<?> request,
                                                                              @NonNull MutableHttpResponse<?> response,
                                                                              @Nullable PriorToLoginPersistence priorToLoginPersistence) {
        ThrowingSupplier<URI, URISyntaxException> uriSupplier = () -> new URI(loginSuccess);
        if (priorToLoginPersistence != null) {
            Optional<URI> originalUri = priorToLoginPersistence.getOriginalUri(request, response);
            if (originalUri.isPresent()) {
                uriSupplier = originalUri::get;
            }
        }
        return uriSupplier;
    }


    private void saveAuthenticationInSession(Authentication authentication, HttpRequest<?> request) {
        Session session = SessionForRequest.find(request).orElseGet(() ->
                SessionForRequest.create(sessionStore, request))
        session.put(io.micronaut.security.filters.SecurityFilter.AUTHENTICATION, authentication);
    }



}




