package ru.hitrerros.purse.model.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.hitrerros.purse.model.PurseEntity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class PurseRepositoryImpl implements PurseRepository {

    private static final String GET_BETWEEN_QUERY = "SELECT r FROM record WHERE r.tr_date BETWEEN :startDate AND :endDate";
    private static final String GET_ALL = "SELECT * FROM record";

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public PurseEntity getRecord(long id) {
        return entityManager.find(PurseEntity.class, id);
    }

    @Override
    @Transactional
    public PurseEntity saveRecord(PurseEntity record) {

        if (record.isNew()) {
            entityManager.persist(record);
        } else {
            entityManager.merge(record);
        }

        return record;
    }


    @Override
    @Transactional
    public void deleteRecord(PurseEntity entity) {
        PurseEntity ref = entityManager.merge(entity);
        entityManager.remove(ref);
    }

    @Override
    public List<PurseEntity> getRangeOfRecords(LocalDate from, LocalDate to) {
        return entityManager.createNamedQuery(GET_BETWEEN_QUERY, PurseEntity.class)
                .setParameter("startDate", from)
                .setParameter("endDate", to).getResultList();
    }

    @Override
    public List<PurseEntity> getAll() {
        return entityManager.createNamedQuery(GET_ALL, PurseEntity.class).getResultList();
    }
}
