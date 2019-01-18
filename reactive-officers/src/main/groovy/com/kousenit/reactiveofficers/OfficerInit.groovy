package com.kousenit.reactiveofficers

import com.kousenit.reactiveofficers.dao.OfficerRepository
import com.kousenit.reactiveofficers.entities.Officer
import com.kousenit.reactiveofficers.entities.Rank
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux

@Component
class OfficerInit implements ApplicationRunner {
    OfficerRepository dao

    OfficerInit(OfficerRepository dao) {
        this.dao = dao
    }

    @Override
    void run(ApplicationArguments args) throws Exception {
        dao.deleteAll()
                .thenMany(
                Flux.just(
                        new Officer(rank: Rank.CAPTAIN, first: "James", last: "Kirk"),
                        new Officer(rank: Rank.CAPTAIN, first: "Jean-Luc", last: "Picard"),
                        new Officer(rank: Rank.CAPTAIN, first: "Benjamin", last: "Sisko"),
                        new Officer(rank: Rank.CAPTAIN, first: "Kathryn", last: "Janeway"),
                        new Officer(rank: Rank.CAPTAIN, first: "Jonathan", last: "Archer")))
                .flatMap { dao.save(it) }
                .thenMany { dao.findAll() }
                .subscribe { println it }

    }
}
