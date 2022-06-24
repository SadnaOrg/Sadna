package ORM.DAOs.Shops;

import ORM.DAOs.DBImpl;
import ORM.HibernateUtil;
import ORM.Users.ShopAdministrator;
import ORM.Shops.Shop;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Collection;

public class ShopDAO implements DBImpl<Shop, Integer> {
    private EntityManager entityManager = HibernateUtil.getEntityManager();

    @Override
    public void save(Shop entity) {
        entityManager.getTransaction().begin();
        entityManager.persist(entity);
        entityManager.getTransaction().commit();
    }

    @Override
    public void update(Shop entity) {
        entityManager.getTransaction().begin();
        entityManager.merge(entity);
        entityManager.getTransaction().commit();
    }

    @Override
    public void delete(Integer key) {
        entityManager.getTransaction().begin();
        entityManager.find(Shop.class, key);
        entityManager.getTransaction().commit();
    }

    @Override
    public Shop findById(Integer key) {
        Shop shop = entityManager.find(Shop.class, key);
        return shop;
    }

    @Override
    public Collection<Shop> findAll() {
        String jpql = "SELECT c FROM Shop c";
        TypedQuery<Shop> query = entityManager.createQuery(jpql, Shop.class);

        return query.getResultList();
    }
}
