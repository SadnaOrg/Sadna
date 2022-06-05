package com.SadnaORM.ShopsRepositories;

import com.SadnaORM.Shops.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<Product,Integer> {
}
