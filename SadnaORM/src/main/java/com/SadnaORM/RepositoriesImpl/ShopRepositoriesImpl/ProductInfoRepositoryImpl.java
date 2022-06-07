package com.SadnaORM.RepositoriesImpl.ShopRepositoriesImpl;

import com.SadnaORM.RepositoriesImpl.RepositoryImpl;
import com.SadnaORM.ShopRepositories.ProductInfoRepository;
import com.SadnaORM.Shops.ProductInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;

@Profile({"sa", "a"})
public class ProductInfoRepositoryImpl implements RepositoryImpl<ProductInfo, ProductInfo.ProductInfoPKID> {
    @Autowired
    private ProductInfoRepository productInfoRepository;

    @Override
    public void save(ProductInfo entity) {
        productInfoRepository.save(entity);
    }

    @Override
    public void delete(ProductInfo entity) {
        productInfoRepository.delete(entity);
    }

    @Override
    public ProductInfo findById(ProductInfo.ProductInfoPKID key) {
        return productInfoRepository.findById(key).get();
    }
}
