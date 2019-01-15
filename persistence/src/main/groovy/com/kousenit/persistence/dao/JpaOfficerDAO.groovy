package com.kousenit.persistence.dao

import com.kousenit.persistence.entities.Officer
import org.springframework.stereotype.Repository

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
class JpaOfficerDAO implements OfficerDAO {
    @PersistenceContext
    EntityManager entityManager

    @Override
    Officer save(Officer officer) {
        entityManager.persist(officer)
        return officer
    }

    @Override
    Optional<Officer> findById(Integer id) {
        Optional.ofNullable(entityManager.find(Officer.class, id))
    }

    @Override
    List<Officer> findAll() {
        entityManager.createQuery("select o from Officer o", Officer)
                .getResultList()
    }

    @Override
    long count() {
        entityManager.createQuery("select count(o.id) from Officer o", Long)
                .getSingleResult()
    }

    @Override
    void delete(Officer officer) {
        entityManager.remove(officer)
    }

    @Override
    boolean existsById(Integer id) {
        Object result = entityManager.createQuery(
                "SELECT 1 from Officer o where o.id=:id")
                .setParameter("id", id)
                .getSingleResult();
        return result != null;
    }
}
