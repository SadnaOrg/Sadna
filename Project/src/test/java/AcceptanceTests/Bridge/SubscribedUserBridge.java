package AcceptanceTests.Bridge;

import AcceptanceTests.DataObjects.*;

import java.util.List;
import java.util.Map;

public interface SubscribedUserBridge extends UserBridge {

    SubscribedUser login(int guestID, RegistrationInfo info);

    Guest logout(int userID);

    boolean updateProduct(int userID, int shopID, int productID, int newID, int newQuantity, double newPrice);

    boolean deleteProductFromShop(int userID, int shopID, int productID);

    boolean appointOwner(int shopID, int appointerID, int appointeeID);

    boolean appointManager(int shopID, int appointerID, int appointeeID);

    boolean closeShop(int shopID, int userID);

    boolean addManagerPermission(int shopID, int giverID, int receiverID, String permission);

    boolean addOwnerPermission(int shopID, int giverID, int receiverID, String permission);

    Map<Integer, Appointment> getShopAppointments(int requestingUserID, int shopID);

    Map<Integer, List<String>> getShopPermissions(int requestingUserID, int shopID);

    boolean addProductToShop(int userID,int shopID, Product product,int productID, double rating, int quantity, double price);

    Shop openShop(int userID, String name, String category);
}
