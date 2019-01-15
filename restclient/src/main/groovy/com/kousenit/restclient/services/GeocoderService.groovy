package com.kousenit.restclient.services

import com.kousenit.restclient.entities.Site
import com.kousenit.restclient.json.Response
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class GeocoderService {
    public static final String BASE = 'https://maps.googleapis.com/maps/api/geocode/json'
    private static final String KEY = 'AIzaSyDw_d6dfxDEI7MAvqfGXEIsEMwjC1PWRno'

    RestTemplate restTemplate

    GeocoderService(RestTemplateBuilder builder) {
        restTemplate = builder.build()
    }

    Site getLatLng(String... address) {
        String encoded = address.collect { URLEncoder.encode(it, 'UTF-8') }.join(',')
        String url = "$BASE?address=$encoded&key=$KEY"
        Response response = restTemplate.getForObject(url, Response)
        return new Site(name: response.formattedAddress,
            latitude: response.location.lat,
            longitude: response.location.lng)
    }
}
