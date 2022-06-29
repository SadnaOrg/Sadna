package ORM.DAOs.Statistics;

import ORM.DAOs.DBImpl;
import ORM.HibernateUtil;
import ORM.Shops.Shop;
import ORM.Statistics.StatisticMap;
import ORM.Statistics.Statistics;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Collection;

public class StatisticMapDAO implements DBImpl<StatisticMap, Statistics> {
    private EntityManager entityManager = HibernateUtil.getEntityManager();


    @Override
    public int save(StatisticMap entity) {
        entityManager.getTransaction().begin();
        entityManager.persist(entity);
        entityManager.getTransaction().commit();
        return 0;
    }

    @Override
    public void update(StatisticMap entity) {
        entityManager.getTransaction().begin();
        entityManager.merge(entity);
        entityManager.getTransaction().commit();
    }

    @Override
    public void delete(Statistics key) {
        entityManager.getTransaction().begin();
        entityManager.find(Shop.class, key);
        entityManager.getTransaction().commit();
    }

    @Override
    public StatisticMap findById(Statistics key) {
        StatisticMap statisticMap = entityManager.find(StatisticMap.class, key);
        return statisticMap;
    }

    @Override
    public Collection<StatisticMap> findAll() {
        String jpql = "SELECT c FROM StatisticMap c";
        TypedQuery<StatisticMap> query = entityManager.createQuery(jpql, StatisticMap.class);

        return query.getResultList();    }
}
