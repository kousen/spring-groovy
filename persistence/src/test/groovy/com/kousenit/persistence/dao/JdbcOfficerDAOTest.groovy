package com.kousenit.persistence.dao

import com.kousenit.persistence.entities.Officer
import com.kousenit.persistence.entities.Rank
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.containsInAnyOrder

@RunWith(SpringRunner)
@SpringBootTest
@Transactional
class JdbcOfficerDAOTest {
    @Autowired
    OfficerDAO dao

    @Test
    void save() throws Exception {
        Officer officer = new Officer(rank: Rank.LIEUTENANT, first: 'Nyota', last: 'Uhuru')
        officer = dao.save(officer)
        assert officer.id
    }

    @Test
    void findByIdThatExists() throws Exception {
        Optional<Officer> officer = dao.findById(1)
        assert officer.present
        assert 1 == officer.get().id
    }

    @Test
    void findByIdThatDoesNotExist() throws Exception {
        Optional<Officer> officer = dao.findById(999)
        assert !officer.present
    }

    @Test
    void count() throws Exception {
        assert 5 == dao.count()
    }

    @Test
    void findAll() throws Exception {
        List<String> dbNames = dao.findAll().collect { it.last }
        assertThat(dbNames, containsInAnyOrder(
                'Kirk', 'Picard', 'Sisko', 'Janeway', 'Archer'))
    }

    @Test
    void delete() throws Exception {
        (1..5).each { id ->
            Optional<Officer> officer = dao.findById(id)
            assert officer.present
            dao.delete(officer.get());
        }
        assert 0 == dao.count()
    }

    @Test
    void existsById() throws Exception {
        (1..5).each { id ->
            assert dao.existsById(id)
        }
    }
}
