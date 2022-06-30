package com.SadnaORM.ShopImpl.ShopServices;

import com.SadnaORM.ShopRepositories.ShopRepository;
import com.SadnaORM.Shops.Shop;
import com.SadnaORM.UserImpl.RepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class ShopService implements RepositoryImpl<Shop, Integer> {
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
        return shopRepository.findById(key).orElse(null);
    }

    public Collection<Shop> findAll() {
        Iterable<Shop> shops = shopRepository.findAll();
        Collection<Shop> shopCollection = new ArrayList<>();
        for (Shop sh : shops) {
            shopCollection.add(sh);
        }
        return shopCollection;
    }
}
