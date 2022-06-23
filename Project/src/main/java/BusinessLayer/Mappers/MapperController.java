package BusinessLayer.Mappers;

import BusinessLayer.Mappers.ShopMappers.ShopMapper;
import BusinessLayer.Mappers.UserMappers.SubscribedUserMapper;

public class MapperController {

    static private class MapperControllerHolder {
        static final MapperController mapper = new MapperController();
    }

    public static MapperController getInstance(){
        return MapperControllerHolder.mapper;
    }

    private String baseURL = "http://localhost:8081";

    public String getBaseURL() {
        return baseURL;
    }
}
