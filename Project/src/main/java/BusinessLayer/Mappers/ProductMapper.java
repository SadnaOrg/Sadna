package BusinessLayer.Mappers;

import BusinessLayer.Products.Product;
import com.SadnaORM.ShopRepositoriesImpl.ProductRepositoryImpl;

public class ProductMapper implements MapperInterface<com.SadnaORM.Shops.Product, Product, Integer> {
    private ProductRepositoryImpl productRepositoryImpl = new ProductRepositoryImpl();

    @Override
    public void save(Product entity) {
        productRepositoryImpl.save(toEntity(entity));
    }

    @Override
    public void delete(Product entity) {
        productRepositoryImpl.delete(toEntity(entity));
    }

    @Override
    public com.SadnaORM.Shops.Product toEntity(Product entity) {
        return new com.SadnaORM.Shops.Product(entity.getID(), entity.getName(), entity.getDescription(),
                entity.getManufacturer(), entity.getPrice(), entity.getQuantity());
    }

    @Override
    public Product FromEntity(com.SadnaORM.Shops.Product entity) {
        return new Product(entity.getId(), entity.getName(), entity.getDescription(),
                entity.getManufacturer(), entity.getPrice(), entity.getQuantity());
    }

    @Override
    public Product findByID(Integer key) {
        return FromEntity(productRepositoryImpl.findById(key));
    }
}
