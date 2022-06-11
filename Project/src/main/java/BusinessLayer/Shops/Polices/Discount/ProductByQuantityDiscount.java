package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;

public class ProductByQuantityDiscount implements DiscountPolicy{
    int productId;
    int productQuantity;
    double discount;
    private int discountId;

    public ProductByQuantityDiscount(int productId, int productQuantity, double discount)
    {
        this.productId= productId;
        this.productQuantity= productQuantity;
        this.discount= discount;
        this.discountId = atomicDiscountID.incrementAndGet();
    }

    @Override
    public double calculateDiscount(Basket basket)
    {
        if(basket.getProducts().containsKey(productId) && basket.getProducts().get(productId)>productQuantity)
                return discount*basket.getProducts().get(productId)*basket.getPrices().get(productId);
        return 0;
    }

    @Override
    public NumericDiscountRules getNumericRule(int searchConnectId) {
        return null;
    }

    @Override
    public LogicDiscountRules getLogicRule(int searchConnectId) {
        return null;
    }

    @Override
    public int getID() {
        return this.discountId;
    }

}
