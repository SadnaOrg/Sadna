package BusinessLayer.Mappers.UserMappers;

import BusinessLayer.Mappers.MapperInterface;
import com.SadnaORM.UserImpl.UserObjects.ShopAdministratorDTO;
import com.SadnaORM.Users.ShopAdministrator;


public  class ShopAdministratorMapper implements MapperInterface<ShopAdministrator, ShopAdministratorDTO, BusinessLayer.Users.ShopAdministrator, ShopAdministrator.ShopAdministratorPK> {


    @Override
    public void save(BusinessLayer.Users.ShopAdministrator entity) {

    }

    @Override
    public void delete(BusinessLayer.Users.ShopAdministrator entity) {

    }

    @Override
    public ShopAdministratorDTO toEntity(BusinessLayer.Users.ShopAdministrator entity) {
        return null;
    }

    @Override
    public BusinessLayer.Users.ShopAdministrator FromEntity(ShopAdministratorDTO entity) {
        return null;
    }

    @Override
    public BusinessLayer.Users.ShopAdministrator findByID(ShopAdministrator.ShopAdministratorPK key) {
        return null;
    }
}
