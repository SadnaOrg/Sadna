package BusinessLayer.Shops;


import BusinessLayer.Mappers.MapperController;
import BusinessLayer.Mappers.ShopMappers.PurchaseHistoryMapper;

import java.util.ArrayList;
import java.util.Collection;

public class PurchaseHistoryController {


    private Collection<PurchaseHistory> DataOnPurchases;

    static private class PurchaseHistoryControllerHolder {
        static final PurchaseHistoryController ph = new PurchaseHistoryController();
    }
    private PurchaseHistoryController()
    {
        this.DataOnPurchases = new ArrayList<>();
    }

    public static PurchaseHistoryController getInstance(){
        return PurchaseHistoryController.PurchaseHistoryControllerHolder.ph;
    }

    public Collection<PurchaseHistory> getDataOnPurchases() {
        Collection<PurchaseHistory> allinfo= MapperController.getInstance().getPurchaseHistoryMapper().findAll();
        this.DataOnPurchases.addAll(allinfo);
        return DataOnPurchases;
    }

    public Collection<PurchaseHistory> getPurchaseInfo()
    {
        Collection<PurchaseHistory> allinfo= MapperController.getInstance().getPurchaseHistoryMapper().findAll();
        this.DataOnPurchases.addAll(allinfo);
        return DataOnPurchases;

    }
    public Collection<PurchaseHistory> getPurchaseInfo(String user)
    {
        Collection<PurchaseHistory> allinfo= MapperController.getInstance().getPurchaseHistoryMapper().findAll();
        if(allinfo==null)
            return null;
        Collection<PurchaseHistory> relevantinfo= new ArrayList<>();
        for(PurchaseHistory purchaseHistory:allinfo)
        {
            if(purchaseHistory.getUser().equals(user)) {
                relevantinfo.add(purchaseHistory);
                this.DataOnPurchases.add(purchaseHistory);
            }
        }
        return relevantinfo;
    }

    public Collection<PurchaseHistory> getPurchaseInfo(int shopid)
    {
        Collection<PurchaseHistory> allinfo= MapperController.getInstance().getPurchaseHistoryMapper().findAll();
        if(allinfo==null)
            return null;
        Collection<PurchaseHistory> relevantinfo= new ArrayList<>();
        for(PurchaseHistory purchaseHistory:allinfo)
        {
            if(purchaseHistory.getShop().getId()== shopid) {
                relevantinfo.add(purchaseHistory);
                this.DataOnPurchases.add(purchaseHistory);
            }
        }
        return relevantinfo;
    }
    public Collection<PurchaseHistory> getPurchaseInfo(int shopid, String user)
    {
        PurchaseHistory purchaseHistory= MapperController.getInstance().getPurchaseHistoryMapper().findByIds(shopid,user);
        if(purchaseHistory==null)
            return null;
        Collection<PurchaseHistory> relevantinfo= new ArrayList<>();
//        for(PurchaseHistory purchaseHistory:allinfo)
//        {
            if(purchaseHistory.getUser().equals(user) && purchaseHistory.getShop().getId()== shopid) {
                relevantinfo.add(purchaseHistory);
                this.DataOnPurchases.add(purchaseHistory);
            }
//        }
        return relevantinfo;
    }

    public PurchaseHistory createPurchaseHistory(Shop shop, String user){
        PurchaseHistory purchaseHistory;
        Collection<PurchaseHistory> purchaseHistoryDb =getPurchaseInfo(shop.getId(), user);
        if(purchaseHistoryDb == null||purchaseHistoryDb.size() == 0) {
            purchaseHistory = new PurchaseHistory(shop, user);
            DataOnPurchases.add(purchaseHistory);
            MapperController.getInstance().getPurchaseHistoryMapper().save(purchaseHistory);
        }
        else{
            purchaseHistory= purchaseHistoryDb.stream().toList().get(0);
            this.DataOnPurchases.add(purchaseHistory);
        }
        return purchaseHistory;
    }

    //for tests only
    public void emptyDataOnPurchases(){
        DataOnPurchases = new ArrayList<>();
    }

    public void clearForTestsOnly() {
        DataOnPurchases.clear();
    }
}
