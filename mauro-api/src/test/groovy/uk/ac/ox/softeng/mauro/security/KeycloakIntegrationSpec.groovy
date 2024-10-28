package uk.ac.ox.softeng.mauro.security

import dasniko.testcontainers.keycloak.KeycloakContainer
import io.micronaut.core.annotation.Introspected
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.runtime.EmbeddedApplication
import jakarta.inject.Inject
import jakarta.ws.rs.client.ClientBuilder
import jakarta.ws.rs.client.Entity
import jakarta.ws.rs.core.Form
import jakarta.ws.rs.core.Response
import org.jboss.resteasy.client.jaxrs.ResteasyClient
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget
import org.jboss.resteasy.spi.ResteasyAsynchronousContext
import org.junit.Test
import org.keycloak.admin.client.KeycloakBuilder
import org.testcontainers.spock.Testcontainers
import spock.lang.Shared
import uk.ac.ox.softeng.mauro.persistence.SecuredContainerizedTest

import java.net.http.HttpHeaders

@Testcontainers
@SecuredContainerizedTest
class KeycloakIntegrationSpec extends SecuredIntegrationSpec {
    @Shared
    String authServerUrl

    @Inject
    EmbeddedApplication<?> application

    @Shared

    KeycloakContainer keycloak  = new KeycloakContainer()
            .withAdminUsername("admin")
            .withAdminPassword("password")

    void setupSpec(){
        keycloak.withRealmImportFile("/sandbox.realm.json")
        authServerUrl = keycloak.getAuthServerUrl() + '/realms/master/protocol/openid-connect/token'
    }

    void cleanupSpec() {
        keycloak.close()
    }

    @Test
    void 'keycloak is running'() {
        //   Map<String,String> env =  [ redirectUris: 'http://localhost:8088/oauth/callback/keycloak']

        Form form = new Form()
        form.param('grant_type','password')
        form.param('client_id','sandbox')
        form.param('client_secret','FR5Xz0TaymuAyPxGIqFRhf8HpBtwyOVi')
        form.param('username','admin')
        form.param('password','password')
        Entity entity = Entity.form(form)
        ResteasyClient resteasyClient = ((ResteasyClientBuilder) ClientBuilder.newBuilder()).build()
        ResteasyWebTarget target = resteasyClient.target(authServerUrl)
        when:
        Response resp = target.request(jakarta.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED_TYPE)
        .post(entity)

        then:
        resp
//        resp.status == HttpStatus.OK

    }


}