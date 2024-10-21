package uk.ac.ox.softeng.mauro.security.authentication

import groovy.util.logging.Slf4j
import io.micronaut.http.HttpRequest
import io.micronaut.http.MutableHttpResponse
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.authentication.AuthenticationResponse
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


    MutableHttpResponse<?> loginSuccess(Authentication authentication, HttpRequest<?> request) {
//       MutableHttpResponse defaultResponse = super.loginSuccess(authentication, request)
//        if (defaultResponse.status == HttpStatus.OK) {
//            log.debug 'Successful login, returning Authentication'
//            return HttpResponse.ok(catalogueUserCacheableRepository.readById((UUID) authentication.attributes.id))
//        } else {
//            defaultResponse
//        }
        null
    }


    MutableHttpResponse<?> loginFailed(AuthenticationResponse authenticationFailed, HttpRequest<?> request) {
//        MutableHttpResponse defaultResponse = super.loginFailed(authenticationFailed, request)
//        if (defaultResponse.status == HttpStatus.OK) {
//            log.debug 'Login failed'
//            return HttpResponse.unauthorized()
//        } else {
//            defaultResponse
//        }
        null
    }
}




