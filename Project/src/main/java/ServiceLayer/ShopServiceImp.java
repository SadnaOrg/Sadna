package main.java.ServiceLayer;

import main.java.BusinessLayer.Products.Product;
import main.java.BusinessLayer.Products.ProductFilters;
import main.java.BusinessLayer.Shops.Shop;
import main.java.BusinessLayer.Shops.ShopController;
import main.java.BusinessLayer.Shops.ShopFilters;
import main.java.ServiceLayer.interfaces.ShopService;

import java.util.Collection;
import java.util.Map;

public class ShopServiceImp implements ShopService {

    public Map<Shop,Collection<Product>> searchProducts(ShopFilters shopPred, ProductFilters productPred){
            return ShopController.getInstance().searchProducts(shopPred,productPred);
    }
}
