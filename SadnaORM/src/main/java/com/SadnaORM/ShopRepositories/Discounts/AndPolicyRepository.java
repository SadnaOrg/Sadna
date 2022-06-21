package com.SadnaORM.ShopRepositories.Discounts;

import com.SadnaORM.Shops.Discounts.AndPolicy;
import com.SadnaORM.Shops.Discounts.DiscountPolicy;
import org.springframework.data.repository.CrudRepository;

public interface AndPolicyRepository extends CrudRepository<AndPolicy, DiscountPolicy.DiscountPolicyPK> {
}
