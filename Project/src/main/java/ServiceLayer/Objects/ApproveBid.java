package ServiceLayer.Objects;

import java.util.concurrent.ConcurrentHashMap;

public record ApproveBid(int shopId, int productId,int quantity, Double price, ConcurrentHashMap<String, Boolean> administraitorsApproval,String userName) {
    public ApproveBid(BusinessLayer.Users.ApproveBid approveBid,String username)
    {
        this(approveBid.getShopId(),
                approveBid.getProductId(),
                approveBid.getQuantity(),
                approveBid.getPrice(),
                approveBid.getAdministraitorsApproval()
                ,username);
    }
}
