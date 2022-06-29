package BusinessLayer.Mappers.ShopMappers;

import BusinessLayer.Mappers.CastEntity;
import BusinessLayer.Mappers.ShopMappers.ProductMapper;
import BusinessLayer.Products.Product;
import ORM.DAOs.DBImpl;
import ORM.DAOs.Shops.ProductDAO;

import java.util.Collection;

public class ProductMapper implements CastEntity<ORM.Shops.Product, Product>, DBImpl<ORM.Shops.Product, Integer> {
    private ProductDAO dao = new ProductDAO();
    static private class ProductMapperHolder {

        static final ProductMapper mapper = new ProductMapper();

    }
    public static ProductMapper getInstance(){
        return ProductMapper.ProductMapperHolder.mapper;
    }
    @Override
    public ORM.Shops.Product toEntity(Product entity) {
        ORM.Shops.Product ormProduct = findById(entity.getID());
        if (ormProduct == null)
            return new ORM.Shops.Product(entity.getID(), entity.getName(), entity.getDescription(), entity.getManufacturer(), entity.getCategory(),
                entity.getPrice(), entity.getQuantity());
        else {
            ormProduct.setName(entity.getName());
            ormProduct.setDescription(entity.getDescription());
            ormProduct.setManufacturer(entity.getManufacturer());
            ormProduct.setQuantity(ormProduct.getQuantity());
            ormProduct.setPrice(entity.getPrice());
        }
        return ormProduct;
    }

    @Override
    public Product fromEntity(ORM.Shops.Product entity) {
        return new Product(entity.getId(), entity.getName(), entity.getDescription(), entity.getManufacturer(), entity.getCategory(),
                entity.getPrice(), entity.getQuantity());
    }


    @Override
    public int save(ORM.Shops.Product product) {
        return 0;
    }

    @Override
    public void update(ORM.Shops.Product product) {

    }

    @Override
    public void delete(Integer key) {

    }

    @Override
    public ORM.Shops.Product findById(Integer key) {
        return dao.findById(key);
    }

    @Override
    public Collection<ORM.Shops.Product> findAll() {
        return null;
    }
}
