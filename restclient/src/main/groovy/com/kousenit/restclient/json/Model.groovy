package com.kousenit.restclient.json

import com.fasterxml.jackson.annotation.JsonProperty

class JokeResponse {
    String type
    Value value
}

class Value {
    int id
    String joke
}

class Response {
    List<Result> results
    String status

    Location getLocation() {
        results.get(0).geometry.location
    }

    String getFormattedAddress() {
        results.get(0).formattedAddress
    }
}

class Result {
    // @JsonProperty('formatted_address')
    String formattedAddress
    Geometry geometry
}

class Geometry {
    Location location;
}

public class Location {
    double lat;
    double lng;

    String toString() {
        "($lat,$lng)"
    }
}