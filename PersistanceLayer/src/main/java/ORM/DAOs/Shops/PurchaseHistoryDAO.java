package ORM.DAOs.Shops;

import ORM.DAOs.DBImpl;
import ORM.HibernateUtil;
import ORM.Shops.PurchaseHistory;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Collection;

public class PurchaseHistoryDAO implements DBImpl<PurchaseHistory, PurchaseHistory.PurchaseHistoryPKID> {
    private EntityManager entityManager = HibernateUtil.getEntityManager();

    @Override
    public int save(PurchaseHistory entity) {
        entityManager.getTransaction().begin();
        entityManager.persist(entity);
        entityManager.getTransaction().commit();
        return 0;
    }

    @Override
    public void update(PurchaseHistory entity) {
        entityManager.getTransaction().begin();
        entityManager.merge(entity);
        entityManager.getTransaction().commit();
    }

    @Override
    public void delete(PurchaseHistory.PurchaseHistoryPKID key) {
        entityManager.getTransaction().begin();
        entityManager.find(PurchaseHistory.class, key);
        entityManager.getTransaction().commit();
    }

    @Override
    public PurchaseHistory findById(PurchaseHistory.PurchaseHistoryPKID key) {
        PurchaseHistory purchaseHistory = entityManager.find(PurchaseHistory.class, key);
        return purchaseHistory;
    }

    @Override
    public Collection<PurchaseHistory> findAll() {
        String jpql = "SELECT c FROM PurchaseHistory c";
        TypedQuery<PurchaseHistory> query = entityManager.createQuery(jpql, PurchaseHistory.class);

        return query.getResultList();
    }
}
