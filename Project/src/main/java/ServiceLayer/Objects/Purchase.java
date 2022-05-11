package ServiceLayer.Objects;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public record Purchase(String user, int shopId, Date dateOfPurchase, int transectionId, List<ProductInfo> products){

    public Purchase(BusinessLayer.Shops.Purchase p) {
        this(p.getUser(),p.getShopid(),p.getDateOfPurchase(),p.getTransectionid(),p.getInfoProducts().entrySet().stream().map(e->new ProductInfo(p.getShopid(),e.getKey(),e.getValue())).collect(Collectors.toList()));
    }
}
