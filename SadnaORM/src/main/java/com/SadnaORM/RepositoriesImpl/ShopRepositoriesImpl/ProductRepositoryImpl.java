package com.SadnaORM.RepositoriesImpl.ShopRepositoriesImpl;

import com.SadnaORM.RepositoriesImpl.RepositoryImpl;
import com.SadnaORM.ShopRepositories.ProductRepository;
import com.SadnaORM.Shops.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;

@Profile({"sa", "a"})
public class ProductRepositoryImpl implements RepositoryImpl<Product, Integer> {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public void save(Product entity) {
        productRepository.save(entity);
    }

    @Override
    public void delete(Product entity) {
        productRepository.delete(entity);
    }

    @Override
    public Product findById(Integer key) {
        return productRepository.findById(key).get();
    }
}
