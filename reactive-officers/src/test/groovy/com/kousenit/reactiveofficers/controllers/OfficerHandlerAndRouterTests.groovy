package com.kousenit.reactiveofficers.controllers

import com.kousenit.reactiveofficers.dao.OfficerRepository
import com.kousenit.reactiveofficers.entities.Officer
import com.kousenit.reactiveofficers.entities.Rank
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RunWith(SpringRunner)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OfficerHandlerAndRouterTests {
    @Autowired
    WebTestClient client

    @Autowired
    OfficerRepository repository

    List<Officer> officers = [
            new Officer(rank: Rank.CAPTAIN, first: "James", last: "Kirk"),
            new Officer(rank: Rank.CAPTAIN, first: "Jean-Luc", last: "Picard"),
            new Officer(rank: Rank.CAPTAIN, first: "Benjamin", last: "Sisko"),
            new Officer(rank: Rank.CAPTAIN, first: "Kathryn", last: "Janeway"),
            new Officer(rank: Rank.CAPTAIN, first: "Jonathan", last: "Archer")
    ]

    @Before
    void setUp() {
        repository.deleteAll()
                .thenMany(Flux.fromIterable(officers))
                .flatMap { repository.save(it) }
                .doOnNext { println it }
                .then()
                .block()
    }

    @Test
    void testCreateOfficer() {
        Officer officer = new Officer(rank: Rank.LIEUTENANT, first: "Hikaru", last: "Sulu")
        client.post().uri("/route")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(officer), Officer.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath('$.id').isNotEmpty()
                .jsonPath('$.first').isEqualTo("Hikaru")
                .jsonPath('$.last').isEqualTo("Sulu")
    }

    @Test
    void testGetAllOfficers() {
        client.get().uri("/route")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Officer);
    }

    @Test
    void testGetSingleOfficer() {
        Officer officer = repository.save(
                new Officer(rank: Rank.ENSIGN, first: "Wesley", last: "Crusher")).block();

        client.get()
        //.uri("/route/{id}", Collections.singletonMap("id", officer.getId()))
                .uri("/route/{id}", officer.id)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith { response ->
            assert response.responseBody
        }
    }
}
