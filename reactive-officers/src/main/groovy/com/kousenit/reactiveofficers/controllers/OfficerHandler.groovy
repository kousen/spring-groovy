package com.kousenit.reactiveofficers.controllers

import com.kousenit.reactiveofficers.dao.OfficerRepository
import com.kousenit.reactiveofficers.entities.Officer
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.web.reactive.function.BodyInserters.fromObject

@Component
class OfficerHandler {
    OfficerRepository repository

    OfficerHandler(OfficerRepository repository) {
        this.repository = repository
    }

    Mono<ServerResponse> listOfficers(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(repository.findAll(), Officer)
    }

    Mono<ServerResponse> createOfficer(ServerRequest request) {
        Mono<Officer> officerMono = request.bodyToMono(Officer.class)
        return officerMono.flatMap { officer ->
            ServerResponse.status(HttpStatus.CREATED)
                    .contentType(APPLICATION_JSON)
                    .body(repository.save(officer), Officer)
        }
    }

    Mono<ServerResponse> getOfficer(ServerRequest request) {
        String id = request.pathVariable("id")
        Mono<ServerResponse> notFound = ServerResponse.notFound().build()
        Mono<Officer> personMono = this.repository.findById(id)
        return personMono.flatMap { person ->
            ServerResponse.ok()
                    .contentType(APPLICATION_JSON)
                    .body(fromObject(person))
        }
        .switchIfEmpty(notFound)
    }
}
