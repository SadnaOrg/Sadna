package main.java.ServiceLayer.interfaces;

import main.java.BusinessLayer.Products.Product;
import main.java.BusinessLayer.Products.ProductFilters;
import main.java.BusinessLayer.Shops.Shop;
import main.java.BusinessLayer.Shops.ShopFilters;

import java.util.Collection;
import java.util.Map;

public interface ShopService {
    Map<Shop, Collection<Product>> searchProducts(ShopFilters shopPred, ProductFilters productPred);
}
