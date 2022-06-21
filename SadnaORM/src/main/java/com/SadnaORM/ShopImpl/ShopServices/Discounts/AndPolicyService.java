package com.SadnaORM.ShopImpl.ShopServices.Discounts;

import com.SadnaORM.ShopRepositories.Discounts.AndPolicyRepository;
import com.SadnaORM.Shops.Discounts.AndPolicy;
import com.SadnaORM.Shops.Discounts.DiscountPolicy;
import com.SadnaORM.UserImpl.RepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;

public class AndPolicyService implements RepositoryImpl<AndPolicy, DiscountPolicy.DiscountPolicyPK> {
    @Autowired
    AndPolicyRepository repository;
    @Override
    public void save(AndPolicy entity) {
        repository.save(entity);
    }

    @Override
    public void delete(AndPolicy entity) {
        repository.delete(entity);
    }

    @Override
    public AndPolicy findById(DiscountPolicy.DiscountPolicyPK key) {
        return repository.findById(key).orElse(null);
    }
}
