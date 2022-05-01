package ServiceLayer.interfaces;

import BusinessLayer.Products.Product;
import BusinessLayer.Products.ProductFilters;
import BusinessLayer.Shops.Shop;
import BusinessLayer.Shops.ShopFilters;
import ServiceLayer.Result;

import java.util.Collection;
import java.util.Map;

public interface GeneralService extends UserService {

    Result logout();
}
