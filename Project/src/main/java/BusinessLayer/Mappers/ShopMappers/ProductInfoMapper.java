package BusinessLayer.Mappers.ShopMappers;

import BusinessLayer.Mappers.CastEntity;
import BusinessLayer.Products.ProductInfo;
import ORM.DAOs.Shops.ProductInfoDAO;
import ORM.DAOs.Shops.PurchaseDAO;

import java.util.Collection;

public class ProductInfoMapper implements CastEntity<ORM.Shops.ProductInfo, ProductInfo> {

    static private class ProductInfoMapperHolder {
        static final ProductInfoMapper mapper = new ProductInfoMapper();
    }
    private final ProductInfoDAO dao = new ProductInfoDAO();

    public static ProductInfoMapper getInstance(){
        return ProductInfoMapper.ProductInfoMapperHolder.mapper;
    }

    private ProductInfoMapper() {

    }

    @Override
    public ORM.Shops.ProductInfo toEntity(ProductInfo entity) {
        return new ORM.Shops.ProductInfo(entity.getProductid(),entity.getProductquantity(),entity.getProductprice());
    }

    @Override
    public ProductInfo fromEntity(ORM.Shops.ProductInfo entity) {
        return new ProductInfo(entity.getProductID(),entity.getQuantity(),entity.getPrice());
    }
}
