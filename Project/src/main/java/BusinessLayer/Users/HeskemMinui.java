package BusinessLayer.Users;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class HeskemMinui {
    private int shopId;
    private String adminToAssign;
    private String appointer;
    private ConcurrentHashMap<String,Boolean> approvals;

    public HeskemMinui(int shopId, String adminToAssign,String appointer, Collection<String> admins) {
        this.shopId = shopId;
        this.adminToAssign = adminToAssign;
        this.appointer =appointer;
        this.approvals = new ConcurrentHashMap<>();
        for(String name: admins)
        {
            this.approvals.put(name,false);
        }
    }

    public boolean approve(String adminName)
    {
        if(approvals.containsKey(adminName)) {
            if (!approvals.get(adminName)) {
                approvals.put(adminName, true);
                return isApproved();
            }
            else
                throw new IllegalStateException("the admin is already approve");
        }
        else
            throw new IllegalStateException("the admin is not in the approve list");
    }


    public boolean isApproved()
    {
        for (Boolean check: approvals.values()) {
            if(!check)
                return check;
        }
        return true;
    }

    public int getShopId() {
        return shopId;
    }

    public ConcurrentHashMap<String, Boolean> getApprovals() {
        return approvals;
    }
}
