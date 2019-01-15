package com.kousenit.persistence.dao

import com.kousenit.persistence.entities.Officer

interface OfficerDAO {
    Officer save(Officer officer)
    Optional<Officer> findById(Integer id)
    List<Officer> findAll()
    long count()
    void delete(Officer officer)
    boolean existsById(Integer id)
}