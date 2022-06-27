package BusinessLayer.Mappers.ShopMappers;

import BusinessLayer.Mappers.CastEntity;
import BusinessLayer.Mappers.ShopMappers.ProductMapper;
import BusinessLayer.Products.Product;

public class ProductMapper implements CastEntity<ORM.Shops.Product, Product> {
    static private class ProductMapperHolder {

        static final ProductMapper mapper = new ProductMapper();
    }
    public static ProductMapper getInstance(){
        return ProductMapper.ProductMapperHolder.mapper;
    }

    @Override
    public ORM.Shops.Product toEntity(Product entity) {
        return new ORM.Shops.Product(entity.getID(), entity.getName(), entity.getDescription(), entity.getManufacturer(), entity.getCategory(),
                entity.getPrice(), entity.getQuantity());
    }

    @Override
    public Product fromEntity(ORM.Shops.Product entity) {
        return new Product(entity.getId(), entity.getName(), entity.getDescription(), entity.getManufacturer(), entity.getCategory(),
                entity.getPrice(), entity.getQuantity());
    }

}
