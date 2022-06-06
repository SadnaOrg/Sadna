package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;

public class ProductByQuantityDiscount implements DiscountPolicy{
    int productId;
    int productQuantity;
    double discount;

    public ProductByQuantityDiscount(int productId, int productQuantity, double discount)
    {
        this.productId= productId;
        this.productQuantity= productQuantity;
        this.discount= discount;
    }

    @Override
    public double calculateDiscount(Basket basket)
    {
        if(basket.getProducts().containsKey(productId) && basket.getProducts().get(productId)>productQuantity)
                return discount*basket.getProducts().get(productId)*basket.getPrices().get(productId);
        return 0;
    }
}
