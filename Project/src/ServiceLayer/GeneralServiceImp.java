package ServiceLayer;

import BusinessLayer.Products.Product;
import BusinessLayer.Products.ProductFilters;
import BusinessLayer.Shops.Shop;
import BusinessLayer.Shops.ShopFilters;
import BusinessLayer.Users.User;
import ServiceLayer.interfaces.GeneralService;
import ServiceLayer.interfaces.ShopService;
import ServiceLayer.interfaces.SystemService;
import ServiceLayer.interfaces.UserService;

import java.util.Collection;
import java.util.Map;

public class GeneralServiceImp implements GeneralService {
    User currUser;
    ShopService shopService;
    UserService userService;
    SystemService systemService;


    @Override
    public Map<Shop, Collection<Product>> searchProducts(ShopFilters shopPred, ProductFilters productPred) {
        return shopService.searchProducts(shopPred, productPred);
    }
}
