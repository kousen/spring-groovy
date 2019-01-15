package com.kousenit.persistence.entities

import groovy.transform.Canonical

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Canonical
@Entity
@Table(name = 'officers')
class Officer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    Rank rank

    @Column(nullable = false, name = 'first_name')
    String first

    @Column(nullable = false, name = 'last_name')
    String last
}
