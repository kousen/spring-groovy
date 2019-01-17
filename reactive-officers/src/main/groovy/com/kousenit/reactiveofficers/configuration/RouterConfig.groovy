package com.kousenit.reactiveofficers.configuration

import com.kousenit.reactiveofficers.controllers.OfficerHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.web.reactive.function.server.RequestPredicates.*

@Configuration
class RouterConfig {
    @Bean
    RouterFunction<ServerResponse> route(OfficerHandler handler) {
        return RouterFunctions
                .route(GET("/route/{id}").and(accept(APPLICATION_JSON)),
                { id -> handler.getOfficer(id) })
                .andRoute(GET("/route").and(accept(APPLICATION_JSON)),
                { handler.listOfficers() })
                .andRoute(POST("/route").and(contentType(APPLICATION_JSON)),
                { handler.createOfficer(it) })
    }
}
