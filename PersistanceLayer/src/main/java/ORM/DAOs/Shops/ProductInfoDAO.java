package ORM.DAOs.Shops;

import ORM.DAOs.DBImpl;
import ORM.HibernateUtil;
import ORM.Shops.ProductInfo;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Collection;

public class ProductInfoDAO implements DBImpl<ProductInfo, ProductInfo.ProductInfoPKID> {
    private EntityManager entityManager = HibernateUtil.getEntityManager();

    @Override
    public void save(ProductInfo entity) {
        entityManager.getTransaction().begin();
        entityManager.persist(entity);
        entityManager.getTransaction().commit();
    }

    @Override
    public void update(ProductInfo entity) {
        entityManager.getTransaction().begin();
        entityManager.merge(entity);
        entityManager.getTransaction().commit();
    }

    @Override
    public void delete(ProductInfo.ProductInfoPKID key) {
        entityManager.getTransaction().begin();
        entityManager.find(ProductInfo.class, key);
        entityManager.getTransaction().commit();
    }

    @Override
    public ProductInfo findById(ProductInfo.ProductInfoPKID key) {
        ProductInfo productInfo = entityManager.find(ProductInfo.class, key);
        return productInfo;
    }

    @Override
    public Collection<ProductInfo> findAll() {
        String jpql = "SELECT c FROM ProductInfo c";
        TypedQuery<ProductInfo> query = entityManager.createQuery(jpql, ProductInfo.class);

        return query.getResultList();
    }
}
