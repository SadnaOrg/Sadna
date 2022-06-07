package BusinessLayer.Mappers;

import BusinessLayer.Shops.Shop;

public class ShopMapper implements MapperInterface<com.SadnaORM.Shops.Shop, Shop,Integer> {
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
