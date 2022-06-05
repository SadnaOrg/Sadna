package com.SadnaORM.ShopRepositories;

import com.SadnaORM.Shops.Purchase;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRepository extends CrudRepository<Purchase, Integer> {
}
