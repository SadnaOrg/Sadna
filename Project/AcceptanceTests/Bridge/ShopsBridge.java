package Bridge;

import DataObjects.Product;
import DataObjects.Shop;
import DataObjects.User;


public interface ShopsBridge {
    boolean addShop(Shop shop, User founder);

    void addProductToShop(int shopID, Product product,int productID, double rating, int quantity, double price);

    void addManager(int shopID, int userID);

    void appointOwner(int shopID, int appointerID, int appointeeID);
}
