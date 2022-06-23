package BusinessLayer.Mappers.UserMappers;

public class ShopOwnerMapper {
    static private class ShopOwnerMapperHolder {
        static final ShopOwnerMapper mapper = new ShopOwnerMapper();
    }

    public static ShopOwnerMapper getInstance(){
        return ShopOwnerMapperHolder.mapper;
    }

    private ShopOwnerMapper() {

    }
}
