package com.kousenit.persistence.dao

import com.kousenit.persistence.entities.Officer
import com.kousenit.persistence.entities.Rank
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository

import javax.sql.DataSource

@Repository
class JdbcOfficerDAO implements OfficerDAO {
    JdbcTemplate jdbcTemplate
    SimpleJdbcInsert insertOfficer

    RowMapper officerMapper = { rs, rowNum ->
        new Officer(id: rs.getInt('id'),
                rank: Rank.valueOf(rs.getString('rank')),
                first: rs.getString('first_name'),
                last: rs.getString('last_name'))
    }

    JdbcOfficerDAO(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource)
        insertOfficer = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName('officers')
                .usingGeneratedKeyColumns('id')
    }

    @Override
    Officer save(Officer officer) {
        Map parameters = [
                rank: officer.rank,
                first_name: officer.first,
                last_name: officer.last
        ]
        officer.id = insertOfficer.executeAndReturnKey(parameters)
        return officer
    }

    @Override
    Optional<Officer> findById(Integer id) {
        if (!existsById(id)) return Optional.empty()
        return Optional.of(jdbcTemplate.queryForObject(
                'SELECT * FROM officers WHERE id=?',
                officerMapper,
                id))
    }

    @Override
    List<Officer> findAll() {
        jdbcTemplate.query('SELECT * FROM officers', officerMapper)
    }



    @Override
    long count() {
        jdbcTemplate.queryForObject('select count(*) from officers', Long)
    }

    @Override
    void delete(Officer officer) {
        jdbcTemplate.update('DELETE FROM officers WHERE id=?', officer.id)
    }

    @Override
    boolean existsById(Integer id) {
        jdbcTemplate.queryForObject(
                'SELECT EXISTS(SELECT 1 FROM officers where id=?)', Boolean, id)
    }
}
