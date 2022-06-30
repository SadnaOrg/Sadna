package ORM.DAOs.Users;

import ORM.DAOs.DBImpl;
import ORM.HibernateUtil;
import ORM.Users.ShopAdministrator;
import ORM.Users.ShopManager;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Collection;

public class ShopManagerDAO implements DBImpl<ShopManager, ShopAdministrator.ShopAdministratorPK> {
    private EntityManager entityManager = HibernateUtil.getEntityManager();

    @Override
    public int save(ShopManager entity) {
        entityManager.getTransaction().begin();
        entityManager.persist(entity);
        entityManager.getTransaction().commit();
        return 0;
    }

    @Override
    public void update(ShopManager entity) {
        entityManager.getTransaction().begin();
        entityManager.merge(entity);
        entityManager.getTransaction().commit();
    }

    @Override
    public void delete(ShopAdministrator.ShopAdministratorPK key) {
        entityManager.getTransaction().begin();
        entityManager.find(ShopManager.class, key);
        entityManager.getTransaction().commit();
    }

    @Override
    public ShopManager findById(ShopAdministrator.ShopAdministratorPK key) {
        ShopManager shopManager = entityManager.find(ShopManager.class, key);
        return shopManager;
    }

    @Override
    public Collection<ShopManager> findAll() {
        String jpql = "SELECT c FROM ShopManager c";
        TypedQuery<ShopManager> query = entityManager.createQuery(jpql, ShopManager.class);

        return query.getResultList();
    }
}
