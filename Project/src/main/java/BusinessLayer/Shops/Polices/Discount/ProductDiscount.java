package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;

public class ProductDiscount implements DiscountPolicy {
    int productId;
    double discount;
    private int discountId;

    public ProductDiscount(int productId, double discount)
    {
        this.productId= productId;
        this.discount= discount;
        this.discountId = atomicDiscountID.incrementAndGet();
    }

    @Override
    public double calculateDiscount(Basket basket)
    {
        if(basket.getProducts().containsKey(productId))
            return discount*basket.getProducts().get(productId)*basket.getPrices().get(productId);

        return 0;

    }


}
