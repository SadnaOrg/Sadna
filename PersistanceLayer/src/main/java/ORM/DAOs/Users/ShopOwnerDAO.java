package ORM.DAOs.Users;

import ORM.DAOs.DBImpl;
import ORM.HibernateUtil;
import ORM.Users.ShopOwner;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Collection;

public class ShopOwnerDAO implements DBImpl<ShopOwner, Integer> {
    private EntityManager entityManager = HibernateUtil.getEntityManager();

    @Override
    public void save(ShopOwner entity) {
        entityManager.getTransaction().begin();
        entityManager.persist(entity);
        entityManager.getTransaction().commit();
    }

    @Override
    public void update(ShopOwner entity) {
        entityManager.getTransaction().begin();
        entityManager.merge(entity);
        entityManager.getTransaction().commit();
    }

    @Override
    public void delete(Integer key) {
        entityManager.getTransaction().begin();
        entityManager.find(ShopOwner.class, key);
        entityManager.getTransaction().commit();
    }

    @Override
    public ShopOwner findById(Integer key) {
        ShopOwner shopOwner = entityManager.find(ShopOwner.class, key);
        return shopOwner;
    }

    @Override
    public Collection<ShopOwner> findAll() {
        String jpql = "SELECT c FROM ShopOwner c";
        TypedQuery<ShopOwner> query = entityManager.createQuery(jpql, ShopOwner.class);

        return query.getResultList();
    }
}