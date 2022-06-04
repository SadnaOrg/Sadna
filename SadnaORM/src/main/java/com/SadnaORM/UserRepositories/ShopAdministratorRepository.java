package com.SadnaORM.UserRepositories;

import com.SadnaORM.Users.ShopAdministrator;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ShopAdministratorRepository extends CrudRepository<ShopAdministrator, ShopAdministrator.ShopAdministratorPK> {
}
