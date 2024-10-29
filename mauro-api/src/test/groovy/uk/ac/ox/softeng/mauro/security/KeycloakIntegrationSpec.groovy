package uk.ac.ox.softeng.mauro.security

import groovy.util.logging.Slf4j
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.runtime.EmbeddedApplication
import jakarta.inject.Inject
import jakarta.ws.rs.client.ClientBuilder
import jakarta.ws.rs.client.Entity
import jakarta.ws.rs.core.Form
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.jboss.resteasy.client.jaxrs.ResteasyClient
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget
import org.junit.Test
import org.testcontainers.spock.Testcontainers
import spock.lang.Shared
import uk.ac.ox.softeng.mauro.persistence.SecuredContainerizedTest

@Testcontainers
@SecuredContainerizedTest
@Slf4j
@spock.lang.Ignore
class KeycloakIntegrationSpec extends SecuredIntegrationSpec {
    @Shared
    String authServerUrl = 'http://localhost:9009/realms/master/protocol/openid-connect/token'


    @Inject
    EmbeddedApplication<?> application

    //todo:fix this. Not working because unable to set realm-import file.
    //Work-around: run keycloak locally on port 9009, get client-secret credentials, replace value below for token
//    @Shared
//    KeycloakContainer keycloak = new KeycloakContainer()
//    //   .withRealmImportFile('sandbox-realm.json')
//            .withAdminUsername("admin")
//            .withAdminPassword("password")


    void setupSpec() {
        //  keycloak.start()
        //  authServerUrl = keycloak.getAuthServerUrl() + '/realms/master/protocol/openid-connect/token'

    }

    void cleanupSpec() {
//        keycloak.close()
    }

    @Test
    void 'can get access token from keycloak instance'() {
//   not sure  how to use Keycloak instance
//        Keycloak keycloakAdminClient = KeycloakBuilder.builder()
//                .serverUrl(keycloak.getAuthServerUrl())
//                .realm("master")
//                .clientId("sandbox")
//                .username(keycloak.getAdminUsername())
//                .password(keycloak.getAdminPassword())
//                .clientSecret('FR5Xz0TaymuAyPxGIqFRhf8HpBtwyOVi')
//                .build();

        //using Resteasy because micronaut httpclient does not support URLENCODED body
        Form form = new Form()
        form.param('grant_type', 'password')
        form.param('client_id', 'sandbox')
        form.param('client_secret', 'FR5Xz0TaymuAyPxGIqFRhf8HpBtwyOVi')
        form.param('username', 'admin')
        form.param('password', 'password')

        Entity entity = Entity.form(form)
        ResteasyClient resteasyClient = ((ResteasyClientBuilder) ClientBuilder.newBuilder()).build()
        ResteasyWebTarget target = resteasyClient.target(authServerUrl)

        when:
        Response resp = target.request(MediaType.APPLICATION_FORM_URLENCODED_TYPE)
                .post(entity)

        then:
        resp
        Map<String, String> responseEntity = resp.readEntity(Map.class)
        resp.status == HttpStatus.OK.code

        def currentAccessToken = responseEntity.get('access_token')

                when:
        HttpResponse response = client.toBlocking().exchange(HttpRequest.GET('/')
                .header('Authorization', currentAccessToken), Map<String, String>.class)

        then:
        response
        response.getStatus() == HttpStatus.OK
    }


}