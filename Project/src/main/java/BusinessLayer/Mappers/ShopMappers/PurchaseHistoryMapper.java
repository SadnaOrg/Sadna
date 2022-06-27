package BusinessLayer.Mappers.ShopMappers;

import BusinessLayer.Shops.PurchaseHistory;
import BusinessLayer.Shops.Shop;
import BusinessLayer.Users.Basket;
import ORM.Shops.ProductInfo;
import ORM.Shops.Purchase;

import java.util.Collection;
import java.util.LinkedList;

public class PurchaseHistoryMapper {
    public ORM.Shops.PurchaseHistory toEntity(Collection<PurchaseHistory> purchaseHistory, ORM.Shops.Shop shop) {
        return null;
    }

    static private class PurchaseHistoryMapperHolder{
        static final PurchaseHistoryMapper mapper = new PurchaseHistoryMapper();

    }

    public ORM.Shops.PurchaseHistory toEntity(PurchaseHistory entity, ORM.Shops.Shop shop) {
            return null;
    }

    public PurchaseHistory fromEntity(ORM.Shops.PurchaseHistory entity, Shop shop) {
        Collection<Purchase> purchases = entity.getPast_purchases();
        Collection<BusinessLayer.Shops.Purchase> purchaseCollection = new LinkedList<>();
        for (Purchase p:
                purchases) {
            Basket b = new Basket(p.getShop().getId());
            for (ProductInfo product:
                    p.getProductInfos()) {
                b.saveProducts(product.getProductID(),product.getQuantity(),product.getPrice(),"");
            }
            BusinessLayer.Shops.Purchase purchase = new BusinessLayer.Shops.Purchase(p.getShop().getId(),p.getUser().getUsername(),p.getTransactionid(),b);
            purchaseCollection.add(purchase);
        }
        return new PurchaseHistory(shop, entity.getUser().getUsername(),purchaseCollection);
    }

    public static PurchaseHistoryMapper getInstance(){
        return PurchaseHistoryMapperHolder.mapper;
    }

    private PurchaseHistoryMapper() {

    }
}
