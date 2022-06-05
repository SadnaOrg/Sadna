package com.SadnaORM.UserRepositories;

import com.SadnaORM.Users.Basket;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BasketRepositroy extends CrudRepository<Basket, Basket.BasketPKID> {
}
