package com.SadnaORM.RepositoriesImpl.ShopRepositoriesImpl;

import com.SadnaORM.RepositoriesImpl.RepositoryImpl;
import com.SadnaORM.ShopRepositories.PurchaseRepository;
import com.SadnaORM.Shops.Purchase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;

@Profile({"sa", "a"})
public class PurchaseRepositoryImpl implements RepositoryImpl<Purchase, Integer> {
    @Autowired
    private PurchaseRepository purchaseRepository;

    @Override
    public void save(Purchase entity) {
        purchaseRepository.save(entity);
    }

    @Override
    public void delete(Purchase entity) {
        purchaseRepository.delete(entity);
    }

    @Override
    public Purchase findById(Integer key) {
        return purchaseRepository.findById(key).get();
    }
}
