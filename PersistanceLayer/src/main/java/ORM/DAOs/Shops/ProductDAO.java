package ORM.DAOs.Shops;

import ORM.DAOs.DBImpl;
import ORM.HibernateUtil;
import ORM.Shops.Product;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Collection;

public class ProductDAO implements DBImpl<Product, Integer> {
    private EntityManager entityManager = HibernateUtil.getEntityManager();

    @Override
    public int save(Product entity) {
        entityManager.getTransaction().begin();
        entityManager.persist(entity);
        entityManager.getTransaction().commit();
        return 0;
    }

    @Override
    public void update(Product entity) {
        entityManager.getTransaction().begin();
        entityManager.merge(entity);
        entityManager.getTransaction().commit();
    }

    @Override
    public void delete(Integer key) {
        entityManager.getTransaction().begin();
        entityManager.find(Product.class, key);
        entityManager.getTransaction().commit();
    }

    @Override
    public Product findById(Integer key) {
        Product product = entityManager.find(Product.class, key);
        return product;
    }

    @Override
    public Collection<Product> findAll() {
        String jpql = "SELECT c FROM Product c";
        TypedQuery<Product> query = entityManager.createQuery(jpql, Product.class);

        return query.getResultList();
    }
}
