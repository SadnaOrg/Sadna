package BusinessLayer.Mappers.ShopMappers;

import BusinessLayer.Mappers.CastEntity;
import BusinessLayer.Mappers.ShopMappers.ProductMapper;
import BusinessLayer.Products.Product;
import ORM.DAOs.DBImpl;
import ORM.DAOs.Shops.ProductDAO;

import java.util.Collection;

public class ProductMapper implements CastEntity<ORM.Shops.Product, Product> {
    private ProductDAO dao = new ProductDAO();
    static private class ProductMapperHolder {

        static final ProductMapper mapper = new ProductMapper();

    }
    public static ProductMapper getInstance(){
        return ProductMapper.ProductMapperHolder.mapper;
    }
    @Override
    public ORM.Shops.Product toEntity(Product entity) {
        ORM.Shops.Product ormProduct = findORMById(entity.getID());
        if (ormProduct == null)
            return new ORM.Shops.Product(entity.getName(), entity.getDescription(), entity.getManufacturer(), entity.getCategory(),
                entity.getPrice(), entity.getQuantity());
        else {
            ormProduct.setName(entity.getName());
            ormProduct.setDescription(entity.getDescription());
            ormProduct.setManufacturer(entity.getManufacturer());
            ormProduct.setQuantity(entity.getQuantity());
            ormProduct.setPrice(entity.getPrice());
        }
        return ormProduct;
    }

    @Override
    public Product fromEntity(ORM.Shops.Product entity) {
        return new Product(entity.getId(), entity.getName(), entity.getDescription(), entity.getManufacturer(), entity.getCategory(),
                entity.getPrice(), entity.getQuantity());
    }

    public ORM.Shops.Product findORMById(Integer key) {
        return dao.findById(key);
    }
}
