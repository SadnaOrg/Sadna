package BusinessLayer.Mappers;

import com.SadnaORM.RepositoriesImpl.UserRepositoriesImpl.ShopAdministratorRepositoryImpl;
import com.SadnaORM.Users.ShopAdministrator;


public  class ShopAdministratorMapper implements MapperInterface<com.SadnaORM.Users.ShopAdministrator, ShopAdministrator,com.SadnaORM.Users.ShopAdministrator.ShopAdministratorPK> {
    private ShopAdministratorRepositoryImpl shopAdministratorRepository;
    @Override
    public void save(ShopAdministrator entity) {

    }

    @Override
    public void delete(ShopAdministrator entity) {

    }

    @Override
    public ShopAdministrator toEntity(ShopAdministrator entity) {
        return null;
    }

    @Override
    public ShopAdministrator FromEntity(ShopAdministrator entity) {
        return null;
    }

    @Override
    public ShopAdministrator findByID(ShopAdministrator.ShopAdministratorPK key) {
        return null;
    }
}
