package BusinessLayer.Users;

import BusinessLayer.Shops.Shop;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ApproveBid {
    private int shopId;
    private int productId;
    private int quantity;
    private double price;
    private ConcurrentHashMap<String,Boolean> administraitorsApproval;


    public ApproveBid(int shopId, int productId,int quantity, Double price, List<String> admins) {
        this.shopId = shopId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.administraitorsApproval = new ConcurrentHashMap<>();
        for (String name:admins)
        {
            this.administraitorsApproval.put(name,false);
        }
    }


    public ApproveBid(int shopId, int productId,int quantity, Double price) {
        this.shopId = shopId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.administraitorsApproval = new ConcurrentHashMap<>();
    }


    public boolean resetApproves()
    {
        for (String name:administraitorsApproval.keySet())
        {
            this.administraitorsApproval.put(name,false);
        }
        return true;
    }

    public boolean approve(String adminName)
    {
        if(this.administraitorsApproval.containsKey(adminName))
            if (!this.administraitorsApproval.get(adminName)) {
                this.administraitorsApproval.put(adminName, true);
                return isApproved();
            }
            else
                throw new IllegalStateException("the admin is already answered");
        else
            throw new IllegalStateException("the admin is not in the approval list");
    }

    public boolean isApproved()
    {
        for (boolean approve:this.administraitorsApproval.values())
        {
            if(!approve)
                return false;
        }
        return true;
    }

    public int getShopId() {
        return shopId;
    }

    public List<String> getAdminsNames(){return administraitorsApproval.keySet().stream().toList();}

    public ConcurrentHashMap<String, Boolean> getAdministraitorsApproval() {
        return administraitorsApproval;
    }

    public void setAdministraitorsApproval(List<String> admins) {
        for (String name: admins)
        {
            administraitorsApproval.put(name,false);
        }
    }


    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getProductId() {
        return productId;
    }
}
