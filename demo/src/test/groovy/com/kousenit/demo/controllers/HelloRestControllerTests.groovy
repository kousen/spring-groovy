package com.kousenit.demo.controllers

import com.kousenit.demo.entities.Greeting
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HelloRestControllerTests {
    @Autowired
    TestRestTemplate template

    @Test
    void greetWithoutName() {
        ResponseEntity<Greeting> entity = template.getForEntity("/rest", Greeting);
        assert HttpStatus.OK == entity.statusCode
        assert MediaType.APPLICATION_JSON_UTF8 == entity.headers.getContentType()
        Greeting response = entity.body
        assert "Hello, World!" == response.message
    }

    @Test
    void greetWithName() {
        Greeting response = template.getForObject("/rest?name=Dolly", Greeting)
        assert "Hello, Dolly!" == response.message
    }

}
