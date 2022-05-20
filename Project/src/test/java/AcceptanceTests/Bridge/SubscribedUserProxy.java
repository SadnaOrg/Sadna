package AcceptanceTests.Bridge;

import AcceptanceTests.DataObjects.*;

import java.util.List;
import java.util.Map;

public class SubscribedUserProxy extends UserProxy implements SubscribedUserBridge{
    private SubscribedUserBridge adapter = new SubscribedUserAdapter();

    public SubscribedUserProxy(UserProxy proxy){
        ((SubscribedUserAdapter)adapter).setUsers(proxy.getGuests(), proxy.getSubscribed());
    }
    @Override
    public Guest logout(String userName) {
        return adapter.logout(userName);
    }

    @Override
    public boolean updateProductQuantity(String username, int shopID, int productID, int newQuantity) {
        return adapter.updateProductQuantity(username,shopID,productID,newQuantity);
    }

    @Override
    public boolean updateProductPrice(String username, int shopID, int productID, double newPrice) {
        return adapter.updateProductPrice(username,shopID,productID,newPrice);
    }

    @Override
    public boolean updateProductDescription(String username, int shopID, int productID, String Desc) {
        return adapter.updateProductDescription(username,shopID,productID,Desc);
    }

    @Override
    public boolean updateProductName(String username, int shopID, int productID, String newName) {
        return adapter.updateProductName(username,shopID,productID,newName);
    }

    @Override
    public boolean deleteProductFromShop(String username, int shopID, int productID) {
        return adapter.deleteProductFromShop(username,shopID,productID);
    }

    @Override
    public boolean appointOwner(int shopID, String appointerName, String appointeeName) {
        return adapter.appointOwner(shopID,appointerName,appointeeName);
    }

    @Override
    public boolean appointManager(int shopID, String appointerName, String appointeeName) {
        return adapter.appointManager(shopID,appointerName,appointeeName);
    }

    @Override
    public boolean closeShop(int shopID, String userName) {
        return adapter.closeShop(shopID,userName);
    }

    @Override
    public boolean addManagerPermission(int shopID, String giverName, String receiverName, SubscribedUser.Permission permission) {
        return adapter.addManagerPermission(shopID,giverName,receiverName,permission);
    }

    @Override
    public boolean addOwnerPermission(int shopID, String giverName, String receiverName, SubscribedUser.Permission permission) {
        return adapter.addOwnerPermission(shopID,giverName,receiverName,permission);
    }

    @Override
    public Map<String, Appointment> getShopAppointments(String requestingUsername, int shopID) {
        return adapter.getShopAppointments(requestingUsername,shopID);
    }

    @Override
    public Map<String, List<SubscribedUser.Permission>> getShopPermissions(String requestingUsername, int shopID) {
        return adapter.getShopPermissions(requestingUsername,shopID);
    }

    @Override
    public boolean addProductToShop(String username, int shopID, Product product, int productID, int quantity, double price) {
        return adapter.addProductToShop(username,shopID,product,productID,quantity,price);
    }

    @Override
    public Shop openShop(String username, String name, String desc) {
        return adapter.openShop(username,name,desc);
    }

    @Override
    public boolean removeAdmin(int shopID, String requesting, String toRemove) {
        return adapter.removeAdmin(shopID,requesting,toRemove);
    }

    @Override
    public boolean removePermission(int shopID, String removing, String removeTo, SubscribedUser.Permission permission) {
        return adapter.removePermission(shopID,removing,removeTo,permission);
    }
}
