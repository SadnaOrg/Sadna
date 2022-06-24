package ORM.DAOs.Shops;

import ORM.DAOs.DBImpl;
import ORM.HibernateUtil;
import ORM.Shops.Purchase;
import ORM.Shops.PurchaseHistory;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Collection;

public class PurchaseDAO implements DBImpl<Purchase, Integer> {
    private EntityManager entityManager = HibernateUtil.getEntityManager();

    @Override
    public void save(Purchase entity) {
        entityManager.getTransaction().begin();
        entityManager.persist(entity);
        entityManager.getTransaction().commit();
    }

    @Override
    public void update(Purchase entity) {
        entityManager.getTransaction().begin();
        entityManager.merge(entity);
        entityManager.getTransaction().commit();
    }

    @Override
    public void delete(Integer key) {
        entityManager.getTransaction().begin();
        entityManager.find(Purchase.class, key);
        entityManager.getTransaction().commit();
    }

    @Override
    public Purchase findById(Integer key) {
        Purchase purchase = entityManager.find(Purchase.class, key);
        return purchase;
    }

    @Override
    public Collection<Purchase> findAll() {
        String jpql = "SELECT c FROM Purchase c";
        TypedQuery<Purchase> query = entityManager.createQuery(jpql, Purchase.class);

        return query.getResultList();
    }
}
