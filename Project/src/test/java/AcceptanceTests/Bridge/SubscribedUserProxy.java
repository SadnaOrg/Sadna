package AcceptanceTests.Bridge;

import AcceptanceTests.DataObjects.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubscribedUserProxy extends UserProxy implements SubscribedUserBridge{
    SubscribedUserAdapter casted;
    public SubscribedUserProxy(UserProxy proxy){
        super(new SubscribedUserAdapter(proxy.getGuests(),proxy.getSubscribed()));
        casted = (SubscribedUserAdapter) super.adapter;
    }
    @Override
    public Guest logout(String userName) {
        return casted.logout(userName);
    }

    @Override
    public boolean updateProductQuantity(String username, int shopID, int productID, int newQuantity) {
        return casted.updateProductQuantity(username,shopID,productID,newQuantity);
    }

    @Override
    public boolean updateProductPrice(String username, int shopID, int productID, double newPrice) {
        return casted.updateProductPrice(username,shopID,productID,newPrice);
    }

    @Override
    public boolean updateProductDescription(String username, int shopID, int productID, String Desc) {
        return casted.updateProductDescription(username,shopID,productID,Desc);
    }

    @Override
    public boolean updateProductName(String username, int shopID, int productID, String newName) {
        return casted.updateProductName(username,shopID,productID,newName);
    }

    @Override
    public boolean deleteProductFromShop(String username, int shopID, int productID) {
        return casted.deleteProductFromShop(username,shopID,productID);
    }

    @Override
    public boolean appointOwner(int shopID, String appointerName, String appointeeName) {
        return casted.appointOwner(shopID,appointerName,appointeeName);
    }

    @Override
    public boolean appointManager(int shopID, String appointerName, String appointeeName) {
        return casted.appointManager(shopID,appointerName,appointeeName);
    }

    @Override
    public boolean closeShop(int shopID, String userName) {
        return casted.closeShop(shopID,userName);
    }

    @Override
    public boolean addManagerPermission(int shopID, String giverName, String receiverName, SubscribedUser.Permission permission) {
        return casted.addManagerPermission(shopID,giverName,receiverName,permission);
    }

    @Override
    public boolean addOwnerPermission(int shopID, String giverName, String receiverName, SubscribedUser.Permission permission) {
        return casted.addOwnerPermission(shopID,giverName,receiverName,permission);
    }

    @Override
    public Map<String, Appointment> getShopAppointments(String requestingUsername, int shopID) {
        return casted.getShopAppointments(requestingUsername,shopID);
    }

    @Override
    public Map<String, List<SubscribedUser.Permission>> getShopPermissions(String requestingUsername, int shopID) {
        return casted.getShopPermissions(requestingUsername,shopID);
    }

    @Override
    public boolean addProductToShop(String username, int shopID, Product product, int productID, int quantity, double price) {
        return casted.addProductToShop(username,shopID,product,productID,quantity,price);
    }

    @Override
    public Shop openShop(String username, String name, String desc) {
        return casted.openShop(username,name,desc);
    }

    @Override
    public boolean removeAdmin(int shopID, String requesting, String toRemove) {
        return casted.removeAdmin(shopID,requesting,toRemove);
    }

    @Override
    public boolean removePermission(int shopID, String removing, String removeTo, SubscribedUser.Permission permission) {
        return casted.removePermission(shopID,removing,removeTo,permission);
    }
}
