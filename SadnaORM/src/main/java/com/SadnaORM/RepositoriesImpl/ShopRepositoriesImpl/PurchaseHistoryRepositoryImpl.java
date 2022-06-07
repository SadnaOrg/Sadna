package com.SadnaORM.RepositoriesImpl.ShopRepositoriesImpl;

import com.SadnaORM.RepositoriesImpl.RepositoryImpl;
import com.SadnaORM.ShopRepositories.PurchaseHistoryRepository;
import com.SadnaORM.Shops.PurchaseHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;

@Profile({"sa", "a"})
public class PurchaseHistoryRepositoryImpl implements RepositoryImpl<PurchaseHistory, PurchaseHistory.PurchaseHistoryPKID> {
    @Autowired
    private PurchaseHistoryRepository purchaseHistoryRepository;

    @Override
    public void save(PurchaseHistory entity) {
        purchaseHistoryRepository.save(entity);
    }

    @Override
    public void delete(PurchaseHistory entity) {
        purchaseHistoryRepository.delete(entity);
    }

    @Override
    public PurchaseHistory findById(PurchaseHistory.PurchaseHistoryPKID key) {
        return purchaseHistoryRepository.findById(key).get();
    }
}
