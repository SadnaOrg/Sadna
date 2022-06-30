package ORM.DAOs.Users;

import ORM.DAOs.DBImpl;
import ORM.HibernateUtil;
import ORM.Users.Basket;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Collection;

public class BasketDAO implements DBImpl<Basket, Basket.BasketPKID> {
    private EntityManager entityManager = HibernateUtil.getEntityManager();

    @Override
    public int save(Basket entity) {
        entityManager.getTransaction().begin();
        entityManager.persist(entity);
        entityManager.getTransaction().commit();
        return 0;
    }

    @Override
    public void update(Basket entity) {
        entityManager.getTransaction().begin();
        entityManager.merge(entity);
        entityManager.getTransaction().commit();
    }

    @Override
    public void delete(Basket.BasketPKID key) {
        entityManager.getTransaction().begin();
        entityManager.find(Basket.class, key);
        entityManager.getTransaction().commit();
    }

    @Override
    public Basket findById(Basket.BasketPKID key) {
        Basket basket = entityManager.find(Basket.class, key);
        return basket;
    }

    @Override
    public Collection<Basket> findAll() {
        String jpql = "SELECT c FROM Basket c";
        TypedQuery<Basket> query = entityManager.createQuery(jpql, Basket.class);

        return query.getResultList();
    }
}
