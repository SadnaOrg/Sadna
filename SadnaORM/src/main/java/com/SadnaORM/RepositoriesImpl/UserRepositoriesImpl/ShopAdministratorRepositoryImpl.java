package com.SadnaORM.RepositoriesImpl.UserRepositoriesImpl;

import com.SadnaORM.RepositoriesImpl.RepositoryImpl;
import com.SadnaORM.UserRepositories.ShopAdministratorRepository;
import com.SadnaORM.Users.ShopAdministrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;

import java.util.Optional;

@Profile("sa")
public class ShopAdministratorRepositoryImpl implements RepositoryImpl<ShopAdministrator,ShopAdministrator.ShopAdministratorPK> {
    @Autowired
    private ShopAdministratorRepository shopAdministratorRepository;

    @Override
    public void save(ShopAdministrator entity) {
        shopAdministratorRepository.save(entity);
    }

    @Override
    public void delete(ShopAdministrator entity) {
        shopAdministratorRepository.delete(entity);
    }

    @Override
    public ShopAdministrator findById(ShopAdministrator.ShopAdministratorPK key) {
        Optional<ShopAdministrator> shopAdministratorOptional = shopAdministratorRepository.findById(key);
        return shopAdministratorOptional.orElse(null);
    }
}
