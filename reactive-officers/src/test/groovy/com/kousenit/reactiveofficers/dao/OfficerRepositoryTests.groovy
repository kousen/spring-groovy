package com.kousenit.reactiveofficers.dao

import com.kousenit.reactiveofficers.entities.Officer
import com.kousenit.reactiveofficers.entities.Rank
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import reactor.core.publisher.Flux
import reactor.test.StepVerifier

@RunWith(SpringRunner)
@SpringBootTest
class OfficerRepositoryTests {
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
    void setUp() throws Exception {
        repository.deleteAll()
                .thenMany(Flux.fromIterable(officers))
                .flatMap { repository.save(it) }
                .then()
                .block()
    }

    @Test
    void save() {
        Officer lorca = new Officer(rank: Rank.CAPTAIN, first: "Gabriel", last: "Lorca")
        StepVerifier.create(repository.save(lorca))
                .expectNextMatches { it.id != '' }
                .verifyComplete()
    }

    @Test
    void findAll() {
        StepVerifier.create(repository.findAll())
                .expectNextCount(5)
                .verifyComplete()
    }

    @Test
    void findById() {
        officers.collect { it.id }
                .each { id ->
            StepVerifier.create(repository.findById(id))
                    .expectNextCount(1)
                    .verifyComplete()
        }
    }

    @Test
    public void findByIdNotExist() {
        StepVerifier.create(repository.findById("xyz"))
                .verifyComplete()
    }

    @Test
    void count() {
        StepVerifier.create(repository.count())
                .expectNext(5L)
                .verifyComplete()
    }

    @Test
    public void findByRank() {
        StepVerifier.create(
                repository.findByRank(Rank.CAPTAIN)
                        .map { it.rank }
                        .distinct())
                .expectNextCount(1)
                .verifyComplete()

        StepVerifier.create(
                repository.findByRank(Rank.ENSIGN)
                        .map { it.rank }
                        .distinct())
                .verifyComplete()
    }

    @Test
    void findByLast() {
        officers.collect { it.last }
                .each { lastName ->
            StepVerifier.create(repository.findByLast(lastName))
                    .expectNextMatches { officer ->
                officer.last == lastName
            }
            .verifyComplete()
        }
    }

}
