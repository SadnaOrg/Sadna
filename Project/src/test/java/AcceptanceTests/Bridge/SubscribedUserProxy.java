package AcceptanceTests.Bridge;

import AcceptanceTests.DataObjects.*;

import java.util.List;
import java.util.Map;

public class SubscribedUserProxy extends UserProxy implements SubscribedUserBridge{
    private UserProxy p;
    public SubscribedUserProxy(UserProxy proxy){
        this.p = proxy; // to get access to service object of existing proxy
    }
    @Override
    public Guest logout(String userName) {
        return null;
    }

    @Override
    public boolean updateProductQuantity(String username, int shopID, int productID, int newQuantity) {
        return false;
    }

    @Override
    public boolean updateProductPrice(String username, int shopID, int productID, double newPrice) {
        return false;
    }

    @Override
    public boolean updateProductDescription(String username, int shopID, int productID, String Desc) {
        return false;
    }

    @Override
    public boolean updateProductName(String username, int shopID, int productID, String newName) {
        return false;
    }

    @Override
    public boolean deleteProductFromShop(String username, int shopID, int productID) {
        return false;
    }

    @Override
    public boolean appointOwner(int shopID, String appointerName, String appointeeName) {
        return false;
    }

    @Override
    public boolean appointManager(int shopID, String appointerName, String appointeeName) {
        return false;
    }

    @Override
    public boolean closeShop(int shopID, String userName) {
        return false;
    }

    @Override
    public boolean addManagerPermission(int shopID, String giverName, String receiverName, String permission) {
        return false;
    }

    @Override
    public boolean addOwnerPermission(int shopID, String giverName, String receiverName, String permission) {
        return false;
    }

    @Override
    public Map<Integer, Appointment> getShopAppointments(String requestingUsername, int shopID) {
        return null;
    }

    @Override
    public Map<Integer, List<String>> getShopPermissions(String requestingUsername, int shopID) {
        return null;
    }

    @Override
    public boolean addProductToShop(String username, int shopID, Product product, int productID, int quantity, double price) {
        return false;
    }

    @Override
    public Shop openShop(String username, String name, String desc) {
        return null;
    }
}
