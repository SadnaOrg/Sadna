package AcceptanceTests.Bridge;

import AcceptanceTests.DataObjects.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubscribedUserProxy extends UserProxy implements SubscribedUserBridge{
    SubscribedUserAdapter subscribedUserAdapter;
    public SubscribedUserProxy(UserProxy proxy){
        super(new SubscribedUserAdapter(proxy.getGuests(),proxy.getSubscribed()));
        subscribedUserAdapter = (SubscribedUserAdapter) super.adapter;
    }
    @Override
    public Guest logout(String userName) {
        return subscribedUserAdapter.logout(userName);
    }

    @Override
    public boolean updateProductQuantity(String username, int shopID, int productID, int newQuantity) {
        return subscribedUserAdapter.updateProductQuantity(username,shopID,productID,newQuantity);
    }

    @Override
    public boolean updateProductPrice(String username, int shopID, int productID, double newPrice) {
        return subscribedUserAdapter.updateProductPrice(username,shopID,productID,newPrice);
    }

    @Override
    public boolean updateProductDescription(String username, int shopID, int productID, String Desc) {
        return subscribedUserAdapter.updateProductDescription(username,shopID,productID,Desc);
    }

    @Override
    public boolean updateProductName(String username, int shopID, int productID, String newName) {
        return subscribedUserAdapter.updateProductName(username,shopID,productID,newName);
    }

    @Override
    public boolean deleteProductFromShop(String username, int shopID, int productID) {
        return subscribedUserAdapter.deleteProductFromShop(username,shopID,productID);
    }

    @Override
    public boolean appointOwner(int shopID, String appointerName, String appointeeName) {
        return subscribedUserAdapter.appointOwner(shopID,appointerName,appointeeName);
    }

    @Override
    public boolean appointManager(int shopID, String appointerName, String appointeeName) {
        return subscribedUserAdapter.appointManager(shopID,appointerName,appointeeName);
    }

    @Override
    public boolean closeShop(int shopID, String userName) {
        return subscribedUserAdapter.closeShop(shopID,userName);
    }

    @Override
    public boolean changeAdminPermission(int shopID, String giverName, String receiverName, List<SubscribedUser.Permission> permission) {
        return subscribedUserAdapter.changeAdminPermission(shopID,giverName,receiverName,permission);
    }

    @Override
    public Map<String, Appointment> getShopAppointments(String requestingUsername, int shopID) {
        return subscribedUserAdapter.getShopAppointments(requestingUsername,shopID);
    }

    @Override
    public Map<String, List<SubscribedUser.Permission>> getShopPermissions(String requestingUsername, int shopID) {
        return subscribedUserAdapter.getShopPermissions(requestingUsername,shopID);
    }

    @Override
    public boolean addProductToShop(String username, int shopID, Product product, int productID, int quantity, double price) {
        return subscribedUserAdapter.addProductToShop(username,shopID,product,productID,quantity,price);
    }

    @Override
    public Shop openShop(String username, String name, String desc) {
        return subscribedUserAdapter.openShop(username,name,desc);
    }

    @Override
    public boolean removeAdmin(int shopID, String requesting, String toRemove) {
        return subscribedUserAdapter.removeAdmin(shopID,requesting,toRemove);
    }

    @Override
    public boolean reOpenShop(String username, int shopID){
        return subscribedUserAdapter.reOpenShop(username,shopID);
    }
}
