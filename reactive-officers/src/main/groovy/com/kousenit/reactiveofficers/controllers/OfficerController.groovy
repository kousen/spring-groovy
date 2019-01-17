package com.kousenit.reactiveofficers.controllers

import com.kousenit.reactiveofficers.dao.OfficerRepository
import com.kousenit.reactiveofficers.entities.Officer
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/officers")
class OfficerController {
    OfficerRepository repository

    OfficerController(OfficerRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    Flux<Officer> getAllOfficers() {
        repository.findAll()
    }

    @GetMapping("{id}")
    Mono<Officer> getOfficer(@PathVariable String id) {
        repository.findById(id)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Mono<Officer> saveOfficer(@RequestBody Officer officer) {
        repository.save(officer)
    }

    @PutMapping("{id}")
    Mono<ResponseEntity<Officer>> updateOfficer(@PathVariable(value = "id") String id,
                                                @RequestBody Officer officer) {
        repository.findById(id)
                .flatMap { existingOfficer ->
            existingOfficer.rank = officer.rank
            existingOfficer.first = officer.first
            existingOfficer.last = officer.last
            return repository.save(existingOfficer)
        }
        .map { new ResponseEntity<>(it, HttpStatus.OK) }
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND))
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    Mono<ResponseEntity<Void>> deleteOfficer(@PathVariable(value = "id") String id) {
        return repository.deleteById(id)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND))
    }

    @DeleteMapping
    Mono<Void> deleteAllOfficers() {
        return repository.deleteAll()
    }
}
