package BusinessLayer.Users;

import BusinessLayer.Shops.Shop;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class BidOffer {
    private int shopID;

    private ConcurrentHashMap<Integer, ApproveBid> approvals;

    public BidOffer(int shopID) {
        this.shopID = shopID;
        this.approvals= new ConcurrentHashMap<>();
    }

    public boolean AddToBid(int productid, int quantity, double price) {
        if(!approvals.containsKey(productid))
        {
            approvals.put(productid,new ApproveBid(shopID,productid,quantity,price));
            return true;
        }
        else
            throw new IllegalStateException("The product is already in the basket");

    }

    public void setApproval(int productid,List<String> admins) {
       if(approvals.containsKey(productid))
           if(approvals.get(productid).getAdministraitorsApproval().size()==0)
               approvals.get(productid).setAdministraitorsApproval(admins);
           else
               throw new IllegalStateException("The product is already have an approval");

       else
            throw new IllegalStateException("The product isn't already have an approval");
    }

    public boolean removeProduct(int productId) {
        if (approvals.containsKey(productId)) {
            approvals.remove(productId);
            return true;
        }
        else
        {
            throw new IllegalStateException("The product is not exist in the bid");
        }
    }

//    public boolean editProductQuantity(int productId, int newquantity) {
//        if(newquantity < 0){
//            throw new IllegalStateException("a product can't appear in a basket with a negative quantity!");
//        }
//        if (products.containsKey(productId)) {
//            if(newquantity == 0){
//                removeProduct(productId);
//            }
//            else{
//                products.put(productId, newquantity);
//            }
//            return true;
//        }
//        else
//        {
//            throw new IllegalStateException("The product is not exist in the basket");
//        }
//    }

    public boolean editProductPrice(int productId, double newPrice) {
        if(newPrice < 0){
            throw new IllegalStateException("a product can't appear in a bid with a negative price!");
        }
        if (approvals.containsKey(productId)) {
            approvals.get(productId).setPrice(newPrice);

            this.approvals.get(productId).resetApproves();
            return true;
        }
        else
        {
            throw new IllegalStateException("The product is not exist in the bid");
        }
    }

    public boolean declineBidOffer(int productId)
    {
        return removeProduct(productId);
    }

    public boolean approveBidOffer(String adminName, int productId)
    {
        return this.approvals.get(productId).approve(adminName);
    }


    public int getShopID() {
        return shopID;
    }

    public ConcurrentHashMap<Integer, Integer> getProducts() {
        ConcurrentHashMap<Integer, Integer> quanities = new ConcurrentHashMap<>();
        for (int pid:approvals.keySet()) {
            quanities.put(pid,approvals.get(pid).getQuantity());
        }
        return quanities;
    }

    public int getQuantityById(int productId) {
        return approvals.get(productId).getQuantity();
    }
    public ConcurrentHashMap<Integer, Double> getPrices() {
        ConcurrentHashMap<Integer, Double> prices = new ConcurrentHashMap<>();
        for (int pid:approvals.keySet()) {
            prices.put(pid,approvals.get(pid).getPrice());
        }
        return prices;
    }

    public double getPriceById(int productId) {
        return approvals.get(productId).getPrice();
    }
    public ConcurrentHashMap<Integer, ApproveBid> getApprovals() {
        return approvals;
    }


    public boolean isNeedMyApproval(String userName) {
        for (var a:approvals.values()) {
            if(a.getAdminsNames().contains(userName))
                return true;
        }
        return false;
    }
}
