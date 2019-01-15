package com.kousenit.persistence.dao

import com.kousenit.persistence.entities.Officer
import com.kousenit.persistence.entities.Rank
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param

interface OfficerRepository extends JpaRepository<Officer, Integer> {
    List<Officer> findByRank(@Param('rank') Rank rank)
    List<Officer> findByLast(@Param('last') String last)
}