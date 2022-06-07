package AcceptanceTests.Bridge;

import AcceptanceTests.DataObjects.*;

import java.util.List;
import java.util.Map;

public interface SubscribedUserBridge extends UserBridge {

    Guest logout(String  userName);

    boolean updateProductQuantity(String username, int shopID, int productID ,int newQuantity);

    boolean updateProductPrice(String username, int shopID, int productID, double newPrice);

    boolean updateProductDescription(String username, int shopID, int productID, String Desc);

    boolean updateProductName(String username, int shopID, int productID, String newName);

    boolean deleteProductFromShop(String username, int shopID, int productID);

    boolean appointOwner(int shopID, String appointerName, String appointeeName);

    boolean appointManager(int shopID, String appointerName, String appointeeName);

    boolean closeShop(int shopID, String  userName);

    boolean changeAdminPermission(int shopID, String giverName, String receiverName, List<SubscribedUser.Permission> permission);

    Map<String, Appointment> getShopAppointments(String requestingUsername, int shopID);

    Map<String, List<SubscribedUser.Permission>> getShopPermissions(String requestingUsername, int shopID);

    boolean addProductToShop(String username,int shopID, Product product,int productID, int quantity, double price);

    Shop openShop(String username, String name, String desc);

    boolean removeAdmin(int shopID, String requesting, String toRemove);

    boolean reOpenShop(String username, int shopID);
}
