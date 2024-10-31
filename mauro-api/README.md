## Specific Keycloak Test setup -local

- Requires local keycloak instance. Use docker-compose.yml:
-   docker compose up -d (detached mode)
-   Can verify container by command: docker ps -a
    Then: goto Keycloak UI running on localhost:9009:   >>"http://localhost:9009/admin"
-   Scroll down to Clients on left panel; then createClient-> ImportClient(to right of "CreateClient" tab)
-  and import 'sandbox.json' under test resources 
   This will allow the microanut app to be brought up at localhost:8088; with login in 2 ways:
-   via keycloak (localhost:8088 on browser)
- or  POST (postman POST login endpoint ({{baseurl}}/authentication/login) )

- 
- Note KeyCloakIntegrationSpec commented out @Ignored; but this will work locally (just not for CI)
- 
  Keycloak and related documentation links:
- https://www.keycloak.org/getting-started/getting-started-docker
- 
- Notes
-  Client sandbox (as per 'sandbox.json') was created with the following information
-    
-    clientId: sandbox
-    name:     sandbox
-    Description: micronaut
-    Valid Redirect uris: http://localhost:8088/oauth/callback/keycloak
-    valid post logout: http://localhost:8088/logout
- 
-  No additional realm was created (using master)

- a POST to http://localhost:9009/realms/master/protocol/openid-connect/token with URLencoded body should return keycloak  authorization token
- 

