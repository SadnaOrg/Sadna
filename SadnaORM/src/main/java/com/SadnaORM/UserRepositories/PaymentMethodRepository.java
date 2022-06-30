package com.SadnaORM.UserRepositories;

import com.SadnaORM.Users.PaymentMethod;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMethodRepository extends CrudRepository<PaymentMethod,Integer> {
}
