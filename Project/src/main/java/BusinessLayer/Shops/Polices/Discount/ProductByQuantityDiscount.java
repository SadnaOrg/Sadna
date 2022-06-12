package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;

public class ProductByQuantityDiscount implements DiscountPolicy{
    private int discountId;
    private int productId;
    private int productQuantity;
    private double discount;

    public ProductByQuantityDiscount(int productId, int productQuantity, double discount)
    {
        this.productId= productId;
        this.productQuantity= productQuantity;
        this.discount= discount;
        this.discountId = atomicDiscountID.incrementAndGet();
    }

    public ProductByQuantityDiscount( int discountId,int productId, int productQuantity, double discount) {
        this.productId = productId;
        this.productQuantity = productQuantity;
        this.discount = discount;
        this.discountId = discountId;
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

    public int getProductId() {
        return productId;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public double getDiscount() {
        return discount;
    }

    public int getDiscountId() {
        return discountId;
    }
}
