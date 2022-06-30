package com.SadnaORM.ShopRepositories;

import com.SadnaORM.Shops.ProductInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductInfoRepository extends CrudRepository<ProductInfo, ProductInfo.ProductInfoPKID> {
}
