package BusinessLayer.Products.Users.BaseActions;

import BusinessLayer.Products.Users.*;
import BusinessLayer.Shops.Shop;

import javax.naming.NoPermissionException;
import java.util.Collection;
import java.util.stream.Collectors;

public class RolesInfo extends BaseAction {

    Shop shop;
    SubscribedUser user;

    public RolesInfo(Shop shop, SubscribedUser user) {
        this.shop = shop;
        this.user=user;
    }

    public Collection<AdministratorInfo> act() throws NoPermissionException {
        if(!shop.isOpen() || !(user.getAdministrator(shop.getId()) instanceof ShopOwner))
            throw new NoPermissionException("cant search info in a closed shop");

        return shop.getShopAdministrators().stream().map(shopAdministrator ->
               new AdministratorInfo(shopAdministrator.getUser().getUserName(),getAdministratorType(shopAdministrator),shopAdministrator.getActionsTypes(),shop.getId(),shopAdministrator.getAppointer())).collect(Collectors.toList());
    }

    private AdministratorInfo.ShopAdministratorType getAdministratorType(ShopAdministrator shopAdministrator) {
        if(shopAdministrator instanceof ShopManager)
            return AdministratorInfo.ShopAdministratorType.MANAGER;
        else return ((ShopOwner)shopAdministrator).isFounder() ? AdministratorInfo.ShopAdministratorType.FOUNDER: AdministratorInfo.ShopAdministratorType.OWNER;
    }

}
