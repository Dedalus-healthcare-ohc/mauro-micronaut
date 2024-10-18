package uk.ac.ox.softeng.mauro.security.authentication.openidconnect

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import io.micronaut.core.annotation.NonNull
import io.micronaut.core.annotation.Nullable
import io.micronaut.http.HttpRequest
import io.micronaut.security.authentication.AuthenticationFailureReason
import io.micronaut.security.authentication.AuthenticationProvider
import io.micronaut.security.authentication.AuthenticationRequest
import io.micronaut.security.authentication.AuthenticationResponse
import io.micronaut.security.authentication.provider.HttpRequestAuthenticationProvider
import io.micronaut.security.oauth2.client.OpenIdProviderMetadata
import io.micronaut.security.oauth2.configuration.OauthClientConfiguration
import io.micronaut.security.oauth2.endpoint.token.request.TokenEndpointClient
import io.micronaut.security.oauth2.endpoint.token.request.password.OpenIdPasswordAuthenticationProvider
import io.micronaut.security.oauth2.endpoint.token.response.OpenIdAuthenticationMapper
import io.micronaut.security.oauth2.endpoint.token.response.validation.OpenIdTokenResponseValidator
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.reactivestreams.Publisher
import uk.ac.ox.softeng.mauro.domain.security.CatalogueUser
import uk.ac.ox.softeng.mauro.persistence.cache.ItemCacheableRepository
import uk.ac.ox.softeng.mauro.security.utils.SecurityUtils

@CompileStatic
@Singleton
@Slf4j
class IdTokenAuthenticationProvider<T> extends OpenIdPasswordAuthenticationProvider<T> {
    /**
     * @param clientConfiguration The client configuration
     * @param openIdProviderMetadata The provider metadata
     * @param tokenEndpointClient The token endpoint client
     * @param openIdAuthenticationMapper The user details mapper
     * @param tokenResponseValidator The token response validator
     */
    IdTokenAuthenticationProvider(OauthClientConfiguration clientConfiguration, OpenIdProviderMetadata openIdProviderMetadata, TokenEndpointClient tokenEndpointClient, OpenIdAuthenticationMapper openIdAuthenticationMapper, OpenIdTokenResponseValidator tokenResponseValidator) {
        super(clientConfiguration, openIdProviderMetadata, tokenEndpointClient, openIdAuthenticationMapper, tokenResponseValidator)
    }

    @Override
    Publisher<AuthenticationResponse> authenticate(T requestContext, AuthenticationRequest<?, ?> authenticationRequest) {
        log.debug("authenticate ")
        super.authenticate(requestContext, authenticationRequest)

    }
}
