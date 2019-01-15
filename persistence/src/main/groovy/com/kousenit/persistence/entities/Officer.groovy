package com.kousenit.persistence.entities

import groovy.transform.Canonical

@Canonical
class Officer {
    Integer id
    Rank rank
    String first
    String last
}
