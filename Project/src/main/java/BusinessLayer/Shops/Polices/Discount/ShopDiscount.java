package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Shops.Shop;
import BusinessLayer.Users.Basket;

public class ShopDiscount implements DiscountPolicy {
    private int basketQuantity;
    private double discount;
    private int discountId;

    public ShopDiscount(int basketQuantity,double discount)
    {
        this.basketQuantity= basketQuantity;
        this.discount = discount;
        this.discountId = atomicDiscountID.incrementAndGet();
    }

    @Override
    public double calculateDiscount(Basket basket)
    {
        int currentQuantity=0;
        double currentPrice=0;
        for (int productId:basket.getProducts().keySet()) {
            currentQuantity+= basket.getProducts().get(productId);
            currentPrice+= basket.getPrices().get(productId)*basket.getProducts().get(productId);
        }
        if(currentQuantity>=basketQuantity)
            return discount*currentPrice;

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
    public int getID(){
        return this.discountId;
    }

    public int getBasketQuantity() {
        return basketQuantity;
    }

    public double getDiscount() {
        return discount;
    }

    public int getDiscountId() {
        return discountId;
    }
}
