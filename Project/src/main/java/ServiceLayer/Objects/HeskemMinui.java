package ServiceLayer.Objects;

import java.util.concurrent.ConcurrentHashMap;

public record HeskemMinui(String adminToAssign, String appointer, ConcurrentHashMap<String, Boolean> approvals,
                          int shopId) {

    public HeskemMinui(BusinessLayer.Users.HeskemMinui heskem){
        this(heskem.getAdminToAssign(),heskem.getAppointer(),new ConcurrentHashMap<>(heskem.getApprovals()),heskem.getShopId());
    }
}
