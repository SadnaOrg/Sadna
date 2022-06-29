package ORM.DAOs.Users;

import ORM.DAOs.DBImpl;
import ORM.HibernateUtil;
import ORM.Users.ShopAdministrator;
import ORM.Users.PaymentMethod;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Collection;

public class PaymentMethodDAO implements DBImpl<PaymentMethod, Integer> {
    private EntityManager entityManager = HibernateUtil.getEntityManager();

    @Override
    public int save(PaymentMethod entity) {
        entityManager.getTransaction().begin();
        entityManager.persist(entity);
        entityManager.getTransaction().commit();
        return 0;
    }

    @Override
    public void update(PaymentMethod entity) {
        entityManager.getTransaction().begin();
        entityManager.merge(entity);
        entityManager.getTransaction().commit();
    }

    @Override
    public void delete(Integer key) {
        entityManager.getTransaction().begin();
        entityManager.find(PaymentMethod.class, key);
        entityManager.getTransaction().commit();
    }

    @Override
    public PaymentMethod findById(Integer key) {
        PaymentMethod paymentMethod = entityManager.find(PaymentMethod.class, key);
        return paymentMethod;
    }

    @Override
    public Collection<PaymentMethod> findAll() {
        String jpql = "SELECT c FROM PaymentMethod c";
        TypedQuery<PaymentMethod> query = entityManager.createQuery(jpql, PaymentMethod.class);

        return query.getResultList();
    }
}
