package AcceptanceTests.Bridge;

import AcceptanceTests.DataObjects.*;

import java.util.List;
import java.util.Map;

public interface SubscribedUserBridge extends UserBridge {

    Guest logout(String  userName); //logout

    boolean updateProductQuantity(String username, int shopID, int productID ,int newQuantity); //updateProductQuantity

    boolean updateProductPrice(String username, int shopID, int productID, double newPrice); //updateProductPrice

    boolean updateProductDescription(String username, int shopID, int productID, String Desc); //updateProductDescription

    boolean updateProductName(String username, int shopID, int productID, String newName); //updateProductName

    boolean deleteProductFromShop(String username, int shopID, int productID); //  deleteProduct

    boolean appointOwner(int shopID, String appointerName, String appointeeName); // assignShopOwner

    boolean appointManager(int shopID, String appointerName, String appointeeName); // assignShopManager

    boolean closeShop(int shopID, String  userName); // closeShop

    boolean addManagerPermission(int shopID, String giverName, String receiverName, String permission); // changeManagerPermission

    boolean addOwnerPermission(int shopID, String giverName, String receiverName, String permission); //changeManagerPermission

    Map<Integer, Appointment> getShopAppointments(String requestingUsername, int shopID); // getAdministratorInfo

    Map<Integer, List<String>> getShopPermissions(String requestingUsername, int shopID); // getAdministratorInfo

    boolean addProductToShop(String username,int shopID, Product product,int productID, int quantity, double price); // addProduct

    Shop openShop(String username, String name, String desc); // openShop
}
