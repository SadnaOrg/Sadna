package ORM.DAOs.Shops;

import ORM.DAOs.DBImpl;
import ORM.HibernateUtil;
import ORM.Shops.HeskemMinui;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Collection;

public class HeskemMinuiDAO implements DBImpl<HeskemMinui, HeskemMinui.HeskemMinuiPK> {
    private EntityManager entityManager = HibernateUtil.getEntityManager();

    @Override
    public int save(HeskemMinui entity) {
        entityManager.getTransaction().begin();
        entityManager.persist(entity);
        entityManager.getTransaction().commit();
        return 0;
    }

    @Override
    public void update(HeskemMinui entity) {
        entityManager.getTransaction().begin();
        entityManager.merge(entity);
        entityManager.getTransaction().commit();
    }

    @Override
    public void delete(HeskemMinui.HeskemMinuiPK key) {
        entityManager.getTransaction().begin();
        entityManager.find(HeskemMinui.class, key);
        entityManager.getTransaction().commit();
    }

    @Override
    public HeskemMinui findById(HeskemMinui.HeskemMinuiPK key) {
        HeskemMinui heskem = entityManager.find(HeskemMinui.class, key);
        return heskem;
    }

    @Override
    public Collection<HeskemMinui> findAll() {
        String jpql = "SELECT c FROM HeskemMinui c";
        TypedQuery<HeskemMinui> query = entityManager.createQuery(jpql, HeskemMinui.class);

        return query.getResultList();
    }
}
