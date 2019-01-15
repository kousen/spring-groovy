package com.kousenit.restclient.entities

import groovy.transform.Canonical

@Canonical
class Site {
    String name
    BigDecimal latitude
    BigDecimal longitude
}
