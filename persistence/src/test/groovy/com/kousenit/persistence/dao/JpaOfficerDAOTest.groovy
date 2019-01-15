package com.kousenit.persistence.dao

import com.kousenit.persistence.entities.Officer
import com.kousenit.persistence.entities.Rank
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.containsInAnyOrder

@RunWith(SpringRunner)
@SpringBootTest
@Transactional
class JpaOfficerDAOTest {
    @Autowired
    @Qualifier('jpaOfficerDAO')
    OfficerDAO dao

    @Autowired
    JdbcTemplate template

    RowMapper idMapper = { rs, num -> rs.getInt('id') }

    @Test
    void save() throws Exception {
        Officer officer = new Officer(rank: Rank.LIEUTENANT, first: 'Nyota', last: 'Uhuru')
        officer = dao.save(officer)
        assert officer.id
    }

    @Test
    void findByIdThatExists() throws Exception {
        template.query('select id from officers', idMapper).each { id ->
            Optional<Officer> officer = dao.findById(id)
            assert officer.present
            assert id == officer.get().id
        }
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
        template.query('select id from officers', idMapper).each { id ->
            Optional<Officer> officer = dao.findById(id)
            assert officer.present
            dao.delete(officer.get());
        }
        assert 0 == dao.count()
    }

    @Test
    void existsById() throws Exception {
        template.query('select id from officers', idMapper).each { id ->
            assert dao.existsById(id)
        }
    }
}
