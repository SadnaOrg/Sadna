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
        return DataOnPurchases;
    }

    public Collection<PurchaseHistory> getPurchaseInfo()
    {
        return DataOnPurchases;

    }
    public Collection<PurchaseHistory> getPurchaseInfo(String user)
    {
        Collection<PurchaseHistory> allinfo= MapperController.getInstance().getPurchaseHistoryMapper().findAll();
        Collection<PurchaseHistory> relevantinfo= new ArrayList<>();
        for(PurchaseHistory purchaseHistory:allinfo)
        {
            if(purchaseHistory.getUser().equals(user)) {
                relevantinfo.add(purchaseHistory);
            }
        }
        return relevantinfo;
    }

    public Collection<PurchaseHistory> getPurchaseInfo(int shopid)
    {
        Collection<PurchaseHistory> allinfo= MapperController.getInstance().getPurchaseHistoryMapper().findAll();
        Collection<PurchaseHistory> relevantinfo= new ArrayList<>();
        for(PurchaseHistory purchaseHistory:allinfo)
        {
            if(purchaseHistory.getShop().getId()== shopid) {
                relevantinfo.add(purchaseHistory);
            }
        }
        return relevantinfo;
    }
    public Collection<PurchaseHistory> getPurchaseInfo(int shopid, String user)
    {
        PurchaseHistory purchaseHistory= MapperController.getInstance().getPurchaseHistoryMapper().findByIds(shopid,user);
        Collection<PurchaseHistory> relevantinfo= new ArrayList<>();
//        for(PurchaseHistory purchaseHistory:allinfo)
//        {
            if(purchaseHistory.getUser().equals(user) && purchaseHistory.getShop().getId()== shopid) {
                relevantinfo.add(purchaseHistory);
            }
//        }
        return relevantinfo;
    }

    public PurchaseHistory createPurchaseHistory(Shop shop, String user){
        PurchaseHistory purchaseHistory;
        if(getPurchaseInfo(shop.getId(), user).size() == 0) {
            purchaseHistory = new PurchaseHistory(shop, user);
            DataOnPurchases.add(purchaseHistory);
            MapperController.getInstance().getPurchaseHistoryMapper().save(purchaseHistory);
        }
        else{
            purchaseHistory = DataOnPurchases.stream().toList().get(0);
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
