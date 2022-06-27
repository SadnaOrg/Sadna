package BusinessLayer.Caches;

import BusinessLayer.Shops.PurchaseHistory;
import BusinessLayer.Shops.Shop;

import java.util.Collection;
import java.util.LinkedList;

public class PurchaseHistoryCache extends Cache<PurchaseHistoryCache.PurchaseHistoryKey, PurchaseHistory>{
    public PurchaseHistoryCache(int maxSize) {
        super(maxSize);
    }

    @Override
    public Collection<PurchaseHistory> findAll() {
        Collection<PurchaseHistory> purchaseHistories = new LinkedList<>();
        for (Collection<PurchaseHistory> purchaseHistory:
        ShopCache.getInstance().findAll().stream().map(Shop::getPurchaseHistory).toList()) {
            purchaseHistories.addAll(purchaseHistory);
        }
        return purchaseHistories;
    }

    @Override
    public PurchaseHistory remoteLookUp(PurchaseHistoryKey id) {
        return null;
    }


    @Override
    public void remoteUpdate(PurchaseHistory element) {

    }

    @Override
    public void remoteRemove(PurchaseHistoryKey id) {

    }


    @Override
    public void clear() {

    }

    public static class PurchaseHistoryKey{
        private String username;
        private int id;
    }
}
