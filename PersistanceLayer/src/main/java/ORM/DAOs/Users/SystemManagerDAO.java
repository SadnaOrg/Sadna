package ORM.DAOs.Users;

import ORM.DAOs.DBImpl;
import ORM.HibernateUtil;
import ORM.Users.ShopAdministrator;
import ORM.Users.SystemManager;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Collection;

public class SystemManagerDAO implements DBImpl<SystemManager, String> {
    private EntityManager entityManager = HibernateUtil.getEntityManager();

    @Override
    public void save(SystemManager entity) {
        entityManager.getTransaction().begin();
        entityManager.persist(entity);
        entityManager.getTransaction().commit();
    }

    @Override
    public void update(SystemManager entity) {
        entityManager.getTransaction().begin();
        entityManager.merge(entity);
        entityManager.getTransaction().commit();
    }

    @Override
    public void delete(String key) {
        entityManager.getTransaction().begin();
        entityManager.find(SystemManager.class, key);
        entityManager.getTransaction().commit();
    }

    @Override
    public SystemManager findById(String key) {
        SystemManager systemManager = entityManager.find(SystemManager.class, key);
        return systemManager;
    }

    @Override
    public Collection<SystemManager> findAll() {
        String jpql = "SELECT c FROM SystemManager c";
        TypedQuery<SystemManager> query = entityManager.createQuery(jpql, SystemManager.class);

        return query.getResultList();
    }
}
