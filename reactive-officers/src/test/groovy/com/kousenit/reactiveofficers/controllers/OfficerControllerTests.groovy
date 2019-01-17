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
public class OfficerControllerTests {
//    WebTestClient client = WebTestClient.bindToServer()
//                                        .baseUrl("http://localhost:8080")
//                                        .build()

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
    void testGetAllOfficers() {
        client.get().uri("/officers")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Officer.class)
                .hasSize(5)
                .consumeWith { println it }
    }

    @Test
    void testGetOfficer() {
        client.get().uri("/officers/{id}", officers.get(0).getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Officer.class)
                .consumeWith { println it }
    }

    @Test
    void testCreateOfficer() {
        Officer officer = new Officer(rank: Rank.LIEUTENANT, first: "Nyota", last: "Uhuru")

        client.post().uri("/officers")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(officer), Officer.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath('$.id').isNotEmpty()
                .jsonPath('$.first').isEqualTo("Nyota")
                .jsonPath('$.last').isEqualTo("Uhuru")
                .consumeWith { println it }
    }
}
