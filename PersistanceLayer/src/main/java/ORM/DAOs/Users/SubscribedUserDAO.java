package ORM.DAOs.Users;

import ORM.DAOs.DBImpl;
import ORM.HibernateUtil;
import ORM.Users.ShopAdministrator;
import ORM.Users.SubscribedUser;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Collection;

public class SubscribedUserDAO implements DBImpl<SubscribedUser, String> {
    private EntityManager entityManager = HibernateUtil.getEntityManager();

    @Override
    public void save(SubscribedUser entity) {
        entityManager.getTransaction().begin();
        entityManager.persist(entity);
        entityManager.getTransaction().commit();
    }

    @Override
    public void update(SubscribedUser entity) {
        entityManager.getTransaction().begin();
        entityManager.merge(entity);
        entityManager.getTransaction().commit();
    }

    @Override
    public void delete(String key) {
        entityManager.getTransaction().begin();
        entityManager.find(SubscribedUser.class, key);
        entityManager.getTransaction().commit();
    }

    @Override
    public SubscribedUser findById(String key) {
        SubscribedUser subscribedUser = entityManager.find(SubscribedUser.class, key);
        return subscribedUser;
    }

    @Override
    public Collection<SubscribedUser> findAll() {
        String jpql = "SELECT c FROM SubscribedUser c";
        TypedQuery<SubscribedUser> query = entityManager.createQuery(jpql, SubscribedUser.class);

        return query.getResultList();
    }
}
