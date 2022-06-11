package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;

public class ValidateProductQuantityDiscount implements  DiscountPred{
    private int ruleId;

    private int productId;
    private int productQuantity;
    //if true then can't be higher than false can't be lower than
    private boolean cantbemore;


    public ValidateProductQuantityDiscount(int productId, int productQuantity, boolean cantbemore) {
        this.productId = productId;
        this.productQuantity = productQuantity;
        this.cantbemore = cantbemore;
        this.ruleId = atomicRuleID.incrementAndGet();
    }

    @Override
    public boolean validateDiscount(Basket basket) {
        if (basket.getProducts().containsKey(productId))
        {
            if(cantbemore)
                return basket.getProducts().get(productId)<=productQuantity;
            else
                return basket.getProducts().get(productId)>=productQuantity;

        }
        return false;
    }

    public int getRuleId() {
        return ruleId;
    }

    public int getProductId() {
        return productId;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public boolean isCantbemore() {
        return cantbemore;
    }
}
