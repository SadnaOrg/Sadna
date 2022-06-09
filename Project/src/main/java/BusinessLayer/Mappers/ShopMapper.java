package BusinessLayer.Mappers;

import BusinessLayer.Shops.Shop;
import com.SadnaORM.RepositoriesImpl.ShopRepositoriesImpl.ShopRepositoryImpl;

public class ShopMapper implements MapperInterface<com.SadnaORM.Shops.Shop, Shop,Integer> {
    private ShopRepositoryImpl shopRepository = new ShopRepositoryImpl();
    private ShopMapper mapper = new ShopMapper();

    public ShopMapper getInstance(){
        return this.mapper;
    }

    @Override
    public void save(Shop entity) {

    }

    @Override
    public void delete(Shop entity) {

    }

    @Override
    public com.SadnaORM.Shops.Shop toEntity(Shop entity) {
        return null;
    }

    @Override
    public Shop FromEntity(com.SadnaORM.Shops.Shop entity) {
        return null;
    }

    @Override
    public Shop findByID(Integer key) {
        return null;
    }
}
