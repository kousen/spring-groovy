package com.kousenit.restclient.services

import com.kousenit.restclient.json.JokeResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class JokeService {
    public static final String BASE = 'http://api.icndb.com/jokes/random?limitTo=[nerdy]'
    RestTemplate restTemplate

    @Autowired
    JokeService(RestTemplateBuilder builder) {
        restTemplate = builder.build()
    }

    String getJoke(String first, String last) {
        String url = "$BASE&firstName=$first&lastName=$last"
        JokeResponse jokeResponse = restTemplate.getForObject(url, JokeResponse)
        return jokeResponse.value.joke
    }
}
