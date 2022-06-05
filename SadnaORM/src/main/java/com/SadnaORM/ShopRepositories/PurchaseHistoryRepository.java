package com.SadnaORM.ShopRepositories;

import com.SadnaORM.Shops.PurchaseHistory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseHistoryRepository extends CrudRepository<PurchaseHistory, PurchaseHistory.PurchaseHistoryPKID> {
}
