## Specific Keycloak Integration Testing

- KeyCloakIntegrationSpec is not working as intended. The intention is to run and start a keycloak instance in a testcontainer
- Issue using library testcontainers-keycloak  (https://github.com/dasniko/testcontainers-keycloak) with realmImportFile on startup
- with Spock
-  Hence authorization from Keycloak auth endpoint does not pass.
- 
- Current test @Ignored by default does work in the following manner :
- assumes there is local keycloak running on port 9009 with admin client: "admin" (or run docker-compose script as follows:)
-  on command line: docker compose up -d (run image in detached mode)
- this brings up the keycloak login: 
- http://localhost:9009/admin
- login, create client "sandbox -see guide here (ignore "start keycloak" instructions) https://www.keycloak.org/getting-started/getting-started-docker
- use master realm
- see file sandbox-realm.json for settings (overwrite client-secret value)

- a POST to http://localhost:9009/realms/master/protocol/openid-connect/token with URLencoded body should return keycloak  authorization token
- 

