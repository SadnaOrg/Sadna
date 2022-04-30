package ServiceLayer;

import BusinessLayer.Products.Product;
import BusinessLayer.Products.ProductFilters;
import BusinessLayer.Shops.Shop;
import BusinessLayer.Shops.ShopController;
import BusinessLayer.Shops.ShopFilters;
import ServiceLayer.interfaces.ShopService;

import java.util.Collection;
import java.util.Map;

public class ShopServiceImp implements ShopService {

    public Map<Shop,Collection<Product>> searchProducts(ShopFilters shopPred, ProductFilters productPred){
            return ShopController.getInstance().searchProducts(shopPred,productPred);
    }
}
