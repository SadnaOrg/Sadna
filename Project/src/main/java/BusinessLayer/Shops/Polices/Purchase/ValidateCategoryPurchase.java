package BusinessLayer.Shops.Polices.Purchase;

import BusinessLayer.Mappers.Converter;
import BusinessLayer.Users.Basket;
import BusinessLayer.Users.User;
import ORM.Shops.Purchases.PurchasePolicy;

public class ValidateCategoryPurchase implements ValidatePurchasePolicy{
    private final int policyLogicId;

    //category
    private String category;
    private int productQuantity;
    //if true then can't be higher than false can't be lower than
    private boolean cantbemore;

    public ValidateCategoryPurchase(String category, int productQuantity, boolean cantbemore) {
        this.category = category;
        this.productQuantity = productQuantity;
        this.cantbemore = cantbemore;
        this.policyLogicId = purchaseLogicId.incrementAndGet();
    }

    public ValidateCategoryPurchase(int policyLogicId, String category, int productQuantity, boolean cantbemore) {
        this.policyLogicId = policyLogicId;
        this.category = category;
        this.productQuantity = productQuantity;
        this.cantbemore = cantbemore;
    }

    public boolean isValid(User u, Basket basket) {

        boolean valid =true;
        for (int pid:basket.getProducts().keySet()) {
            String productCategory = basket.getCategories().get(pid);
            if(productCategory!= null) {
                if (category.equals(productCategory)) {
                    if (cantbemore)
                        valid = valid && basket.getProducts().get(pid) <= productQuantity;
                    else
                        valid = valid && basket.getProducts().get(pid) >= productQuantity;
                }
            }
        }
        return valid;
    }
    @Override
    public LogicPurchasePolicy getLogicRule(int searchConnectId)
    {
        return null;
    }

    @Override
    public int getID(){return policyLogicId;}

    public int getPolicyLogicId() {
        return policyLogicId;
    }

    public String getCategory() {
        return category;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public boolean isCantbemore() {
        return cantbemore;
    }

    @Override
    public PurchasePolicy toEntity(Converter c,ORM.Shops.Shop shop) {
        return c.toEntity(this,shop);
    }
}