package BusinessLayer.Mappers.ShopMappers;

import BusinessLayer.Mappers.CastEntity;
import BusinessLayer.Products.Product;

public class ProductMapper implements CastEntity<ORM.Shops.Product, Product> {

    private static class ProductMapperHolder {
        private static final ProductMapper instance = new ProductMapper();
    }

    private ProductMapper(){

    }

    public static ProductMapper getInstance(){
        return ProductMapperHolder.instance;
    }

    @Override
    public ORM.Shops.Product toEntity(Product entity) {
        return new ORM.Shops.Product(entity.getID(), entity.getName(), entity.getDescription(), entity.getManufacturer(), entity.getPrice(), entity.getQuantity(), entity.getCategory());
    }

    @Override
    public Product fromEntity(ORM.Shops.Product entity) {
        Product p = new Product(entity.getId(), entity.getName(), entity.getPrice(), entity.getQuantity());
        p.setCategory(entity.getCategory());
        p.setManufacturer(entity.getManufacturer());
        p.setDescription(p.getDescription());
        return p;
    }
}
