package com.kousenit.restclient.services

import org.junit.Test
import org.junit.runner.RunWith
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import reactor.test.StepVerifier

import java.time.Duration

@RunWith(SpringRunner)
@SpringBootTest
class JokeServiceTest {
    Logger logger = LoggerFactory.getLogger(JokeServiceTest)

    @Autowired
    JokeService service

    @Test
    void getJoke() throws Exception {
        String joke = service.getJoke("Craig", "Walls")
        logger.info(joke)
        assert joke.contains("Craig") || joke.contains("Walls")
    }

    @Test
    void getJokeAsync() {
        String joke = service.getJokeAsync('Craig', 'Walls')
                .block(Duration.ofSeconds(2))
        logger.info(joke);
        assert joke.contains("Craig") || joke.contains("Walls")
    }

    @Test
    void useStepVerifier() {
        StepVerifier.create(service.getJokeAsync("Craig", "Walls"))
                .assertNext { joke ->
            logger.info(joke)
            assert joke.contains('Craig') || joke.contains('Walls')
        }
        .verifyComplete()
    }

}
