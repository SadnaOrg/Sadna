package ServiceLayer.interfaces;

import BusinessLayer.Products.Product;
import BusinessLayer.Products.ProductFilters;
import BusinessLayer.Shops.Shop;
import BusinessLayer.Shops.ShopFilters;

import java.util.Collection;
import java.util.Map;

public interface ShopService {
    Map<Shop, Collection<Product>> searchProducts(ShopFilters shopPred, ProductFilters productPred);
}
