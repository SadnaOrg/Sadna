package ServiceLayer;

import BusinessLayer.Products.Product;
import BusinessLayer.Products.ProductFilters;
import BusinessLayer.Shops.Shop;
import BusinessLayer.Shops.ShopFilters;
import BusinessLayer.Users.SubscribedUser;
import BusinessLayer.Users.User;
import ServiceLayer.interfaces.GeneralService;
import ServiceLayer.interfaces.ShopService;
import ServiceLayer.interfaces.SystemService;
import ServiceLayer.interfaces.UserService;

import java.util.Collection;
import java.util.Map;

public class GeneralServiceImp extends UserServiceImp implements GeneralService {
    SubscribedUser currUser;

    public GeneralServiceImp(SubscribedUser currUser) {
        this.currUser=currUser;
    }


}
