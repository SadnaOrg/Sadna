package BusinessLayer.Users;

import BusinessLayer.Shops.Shop;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ApproveBid {
    private int shopId;
    private ConcurrentHashMap<String,Boolean> administraitorsApproval;

    public ApproveBid(int shopId, List<String> admins) {
        this.shopId = shopId;
        this.administraitorsApproval = new ConcurrentHashMap<>();
        for (String name:admins)
        {
            this.administraitorsApproval.put(name,false);
        }
    }

    public ApproveBid(int shopId, ConcurrentHashMap<String, Boolean> administraitorsApproval) {
        this.shopId = shopId;
        this.administraitorsApproval = administraitorsApproval;
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

    public ConcurrentHashMap<String, Boolean> getAdministraitorsApproval() {
        return administraitorsApproval;
    }
}
