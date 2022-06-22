package BusinessLayer.Users;

import BusinessLayer.Shops.Shop;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class BidOffer {
    private int shopID;
    private ConcurrentHashMap<Integer , Integer> products;
    private ConcurrentHashMap<Integer, Double> prices;
    private ConcurrentHashMap<Integer, ApproveBid> approvals;

    public BidOffer(int shopID) {
        this.shopID = shopID;
        this.products= new ConcurrentHashMap<>();
        this.prices = new ConcurrentHashMap<>();
        this.approvals= new ConcurrentHashMap<>();
    }

    public BidOffer(int shopID, ConcurrentHashMap<Integer, Integer> products, ConcurrentHashMap<Integer, Double> prices, ConcurrentHashMap<Integer, ApproveBid> approvals) {
        this.shopID = shopID;
        this.products = products;
        this.prices = prices;
        this.approvals = approvals;
    }

    public boolean AddToBid(int productid, int quantity, double price) {
        if(!products.containsKey(productid))
        {
            products.put(productid,quantity);
            prices.put(productid,price);
            approvals.put(productid,null);
            return true;
        }
        else
            throw new IllegalStateException("The product is already in the basket");

    }

    public void setApproval(int productid,List<String> admins) {
        if(approvals.get(productid)==null)
            approvals.put(productid,new ApproveBid(shopID,admins));
        else
            throw new IllegalStateException("The product is already have an approval");
    }

    public boolean removeProduct(int productId) {
        if (products.containsKey(productId)) {
            products.remove(productId);
            prices.remove(productId);
            approvals.remove(productId);
            return true;
        }
        else
        {
            throw new IllegalStateException("The product is not exist in the basket");
        }
    }

    public boolean editProductQuantity(int productId, int newquantity) {
        if(newquantity < 0){
            throw new IllegalStateException("a product can't appear in a basket with a negative quantity!");
        }
        if (products.containsKey(productId)) {
            if(newquantity == 0){
                removeProduct(productId);
            }
            else{
                products.put(productId, newquantity);
            }
            return true;
        }
        else
        {
            throw new IllegalStateException("The product is not exist in the basket");
        }
    }

    public boolean editProductPrice(int productId, double newPrice) {
        if(newPrice < 0){
            throw new IllegalStateException("a product can't appear in a basket with a negative price!");
        }
        if (prices.containsKey(productId)) {
            prices.put(productId, newPrice);

            this.approvals.get(productId).resetApproves();
            return true;
        }
        else
        {
            throw new IllegalStateException("The product is not exist in the basket");
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
        return products;
    }

    public int getQuantityById(int productId) {
        return products.get(productId);
    }
    public ConcurrentHashMap<Integer, Double> getPrices() {
        return prices;
    }

    public double getPriceById(int productId) {
        return prices.get(productId);
    }
    public ConcurrentHashMap<Integer, ApproveBid> getApprovals() {
        return approvals;
    }


}
