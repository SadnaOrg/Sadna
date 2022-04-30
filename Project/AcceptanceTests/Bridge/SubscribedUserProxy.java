package Bridge;

import Mocks.*;

import java.util.List;
import java.util.Map;

public class SubscribedUserProxy extends UserProxy implements SubscribedUserBridge {

    @Override
    public SubscribedUser login(int guestID, RegistrationInfo info) {
        return null;
    }

    @Override
    public Guest logout(int userID) {
        return null;
    }

    @Override
    public boolean updateProduct(int userID, int shopID, int productID, int newID, int newQuantity, double newPrice) {
        return false;
    }

    @Override
    public boolean deleteProductFromShop(int userID, int shopID, int productID) {
        return false;
    }

    @Override
    public boolean appointOwner(int shopID, int appointerID, int appointeeID) {
        return false;
    }

    @Override
    public boolean appointManager(int shopID, int appointerID, int appointeeID) {
        return false;
    }

    @Override
    public boolean closeShop(int shopID, int userID) {
        return false;
    }

    @Override
    public boolean addPermission(int shopiID, int giverID, int receiverID, String permission) {
        return false;
    }

    @Override
    public Map<Integer, Appointment> getShopAppointments(int requestingUserID, int shopID) {
        return null;
    }

    @Override
    public Map<Integer, List<String>> getShopPermissions(int requestingUserID, int shopID) {
        return null;
    }

    @Override
    public void addProductToShop(int userID,int shopID, Product product,int productID, double rating, int quantity, double price) {

    }
    @Override
    public Shop openShop(int userID, String name, String category) {
        return null;
    }
}
