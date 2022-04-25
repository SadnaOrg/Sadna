package test.Bridge;

import test.Mocks.Product;
import test.Mocks.Shop;


public interface ShopsBridge {
    boolean addShop(Shop shop); // move to system bridge?

    void addProductToShop(int shopID, Product product,int productID, int quantity, double price);

    void addManager(int shopID, int userID);

    boolean appointOwner(int shopID, int appointerID, int appointeeID);
}
