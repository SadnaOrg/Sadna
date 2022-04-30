package BusinessLayer.Users.BaseActions;

import BusinessLayer.Shops.Shop;
import BusinessLayer.Users.AdministratorInfo;
import BusinessLayer.Users.ShopAdministrator;
import BusinessLayer.Users.ShopManager;
import BusinessLayer.Users.ShopOwner;

import java.util.Collection;
import java.util.stream.Collectors;

public class RolesInfo extends BaseAction {

    Shop shop;

    public RolesInfo(Shop shop) {
        this.shop = shop;
    }

    public Collection<AdministratorInfo> act(){
       return shop.getShopAdministrators().stream().map(shopAdministrator ->
               new AdministratorInfo(getAdministratorType(shopAdministrator),shopAdministrator.getActionsTypes())).collect(Collectors.toList());
    }

    private AdministratorInfo.ShopAdministratorType getAdministratorType(ShopAdministrator shopAdministrator) {
        if(shopAdministrator instanceof ShopManager)
            return AdministratorInfo.ShopAdministratorType.MANAGER;
        else return ((ShopOwner)shopAdministrator).isFounder() ? AdministratorInfo.ShopAdministratorType.FOUNDER: AdministratorInfo.ShopAdministratorType.OWNER;
    }

}
