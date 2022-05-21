package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;

public class ProductByQuantityDiscount extends DiscountPolicy{
    int productId;
    int productQuantity;
    double discount;

    public ProductByQuantityDiscount(int productId, int productQuantity, double discount)
    {
        super();
        this.productId= productId;
        this.productQuantity= productQuantity;
        this.discount= discount;
    }

    @Override
    public double calculateDiscount(Basket basket)
    {
        if(basket.getProducts().containsKey(productId) && basket.getProducts().get(productId)>productQuantity)
                return discount*basket.getProducts().get(productId)*basket.getPrices().get(productId)
                        +this.discountPolicy.calculateDiscount(basket);
        else
            return this.discountPolicy.calculateDiscount(basket);
    }
}
