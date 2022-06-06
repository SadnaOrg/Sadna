package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;

public class ValidateProductQuantityDiscount implements  DiscountPred{

    private int productId;
    private int productQuantity;

    public ValidateProductQuantityDiscount(int productId, int productQuantity) {
        this.productId = productId;
        this.productQuantity = productQuantity;
    }

    @Override
    public boolean validateDiscount(Basket basket) {
        return basket.getProducts().get(productId) >= productQuantity;
    }
}
