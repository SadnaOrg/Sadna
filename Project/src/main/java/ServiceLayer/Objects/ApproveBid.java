package ServiceLayer.Objects;

import java.util.concurrent.ConcurrentHashMap;

public record ApproveBid(int shopId, ConcurrentHashMap<String, Boolean> administraitorsApproval) {
    public ApproveBid(BusinessLayer.Users.ApproveBid approveBid)
    {
        this(approveBid.getShopId(),approveBid.getAdministraitorsApproval());
    }
}
