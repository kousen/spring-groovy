package com.kousenit.reactiveofficers.entities

import groovy.transform.Canonical

@Canonical
class Officer {
    String id    // String to map to MongoDB ObjectId
    Rank rank
    String first
    String last
}
