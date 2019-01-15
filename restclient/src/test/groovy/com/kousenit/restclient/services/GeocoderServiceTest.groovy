package com.kousenit.restclient.services

import com.kousenit.restclient.entities.Site
import org.junit.Test
import org.junit.runner.RunWith
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.*

@RunWith(SpringRunner)
@SpringBootTest
class GeocoderServiceTest {
    @Autowired
    GeocoderService service

    Logger logger = LoggerFactory.getLogger(GeocoderServiceTest)

    @Test
    void getLatLngWithoutStreet() throws Exception {
        Site site = service.getLatLng("Boston", "MA")
        logger.info(site.name)
        assertThat(site.latitude, is(closeTo(42.36, 0.01)))
        assertThat(site.longitude, is(closeTo(-71.06, 0.01)))
    }

    @Test
    void getLatLngWithStreet() throws Exception {
        Site site = service.getLatLng("1600 Ampitheatre Parkway",
                "Mountain View", "CA");
        assertThat(site.latitude, is(closeTo(37.42, 0.01)))
        assertThat(site.longitude, is(closeTo(-122.08, 0.01)))
    }
}
