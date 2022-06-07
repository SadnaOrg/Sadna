package com.SadnaORM.RepositoriesImpl.ShopRepositoriesImpl;

import com.SadnaORM.RepositoriesImpl.RepositoryImpl;
import com.SadnaORM.ShopRepositories.ShopRepository;
import com.SadnaORM.Shops.Shop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;

@Profile({"sa", "a"})
public class ShopRepositoryImpl implements RepositoryImpl<Shop, Integer> {
    @Autowired
    private ShopRepository shopRepository;

    @Override
    public void save(Shop entity) {
        shopRepository.save(entity);
    }

    @Override
    public void delete(Shop entity) {
        shopRepository.delete(entity);
    }

    @Override
    public Shop findById(Integer key) {
        return shopRepository.findById(key).get();
    }
}
