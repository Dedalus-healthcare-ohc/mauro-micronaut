package uk.ac.ox.softeng.mauro.controller.model


import groovy.transform.CompileStatic
import io.micronaut.core.annotation.NonNull
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule

@CompileStatic
@Controller
@Secured(SecurityRule.IS_AUTHENTICATED)
class SemanticLinkController{
    //todo: implement actual
    @Get('/{domainType}/{domainId}/semanticLinks')
    List<Map> show(@NonNull String domainType, @NonNull UUID domainId) {
        [
            [
                id        : domainId,
                domainType: domainType

            ]
        ] as List<Map>
    }

}
