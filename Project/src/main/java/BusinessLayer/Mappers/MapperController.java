package BusinessLayer.Mappers;

import BusinessLayer.Mappers.ShopMappers.PurchaseHistoryMapper;
import BusinessLayer.Mappers.ShopMappers.ShopMapper;
import BusinessLayer.Mappers.UserMappers.SubscribedUserMapper;
import BusinessLayer.Mappers.UserMappers.SystemManagerMapper;
import BusinessLayer.Shops.PurchaseHistory;

public class MapperController {
    private boolean dbflag = false;

    static private class MapperControllerHolder {
        static  MapperController mapper = new MapperController();
    }

    public static MapperController getInstance(){
        return MapperControllerHolder.mapper;
    }

    public ShopMapper getShopMapper() {
        return shopMapper;
    }

    public SubscribedUserMapper getSubscribedUserMapper() {
        return subscribedUserMapper;
    }

    public SystemManagerMapper getSystemManagerMapper() {
        return systemManagerMapper;
    }

    public PurchaseHistoryMapper getPurchaseHistoryMapper() {
        return purchaseHistoryMapper;
    }

    private ShopMapper shopMapper = ShopMapper.getInstance();
    private SubscribedUserMapper subscribedUserMapper = SubscribedUserMapper.getInstance();
    private SystemManagerMapper systemManagerMapper = SystemManagerMapper.getInstance();
    private PurchaseHistoryMapper purchaseHistoryMapper = PurchaseHistoryMapper.getInstance();

    public void setDbflag(boolean dbflag) {
        this.dbflag = dbflag;
    }
}
