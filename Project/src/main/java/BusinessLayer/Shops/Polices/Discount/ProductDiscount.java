package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;

public class ProductDiscount extends DiscountPolicy {
    int productId;
    double discount;

    public ProductDiscount(DiscountPolicyInterface discountPolicy, int productId, double discount)
    {
        super(discountPolicy);
        this.productId= productId;
        this.discount= discount;
    }

    @Override
    public double calculateDiscount(Basket basket)
    {
        if(basket.getProducts().containsKey(productId))
            return discount*basket.getProducts().get(productId)*basket.getPrices().get(productId)
                    +this.discountPolicy.calculateDiscount(basket);
        else
            return this.discountPolicy.calculateDiscount(basket);
    }


}
