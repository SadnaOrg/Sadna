package ORM.DAOs.Statistics;

import ORM.DAOs.DBImpl;
import ORM.HibernateUtil;
import ORM.Shops.Shop;
import ORM.Statistics.StatisticMap;
import ORM.Statistics.Statistics;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.Collection;

public class StatisticsDAO implements DBImpl<Statistics, LocalDate> {
    private EntityManager entityManager = HibernateUtil.getEntityManager();


    @Override
    public int save(Statistics entity) {
        entityManager.getTransaction().begin();
        entityManager.persist(entity);
        entityManager.getTransaction().commit();
        return 0;
    }

    @Override
    public void update(Statistics entity) {
        entityManager.getTransaction().begin();
        entityManager.merge(entity);
        entityManager.getTransaction().commit();
    }

    @Override
    public void delete(LocalDate key) {
        entityManager.getTransaction().begin();
        entityManager.find(Shop.class, key);
        entityManager.getTransaction().commit();
    }

    @Override
    public Statistics findById(LocalDate key) {
        Statistics statistics = entityManager.find(Statistics.class, key);
        return statistics;
    }

    @Override
    public Collection<Statistics> findAll() {
        String jpql = "SELECT c FROM Statistics c";
        TypedQuery<Statistics> query = entityManager.createQuery(jpql, Statistics.class);

        return query.getResultList();
    }
}
