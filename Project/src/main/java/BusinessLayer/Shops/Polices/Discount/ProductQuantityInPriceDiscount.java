package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;

public class ProductQuantityInPriceDiscount extends DiscountPolicy{

    int productID;
    int quantity;
    double priceForQuantity;

    public ProductQuantityInPriceDiscount(int productID, int quantity, double priceForQuantity) {
        super();
        this.productID = productID;
        this.quantity = quantity;
        this.priceForQuantity = priceForQuantity;
    }

    @Override
    double calculateDiscount(Basket basket) {
        int quantityOfPid = basket.getProducts().get(productID);
        if(basket.getProducts().containsKey(productID))
            return quantityOfPid*basket.getPrices().get(productID)-((quantityOfPid/quantity)*priceForQuantity+(quantityOfPid%quantity)*basket.getPrices().get(productID))
                    +this.discountPolicy.calculateDiscount(basket);
        else
            return this.discountPolicy.calculateDiscount(basket);    }
}
