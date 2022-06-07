package com.SadnaORM.ShopRepositoriesImpl;

import com.SadnaORM.ShopRepositories.ShopRepository;
import com.SadnaORM.Shops.Shop;
import com.SadnaORM.Shops.Shop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

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
