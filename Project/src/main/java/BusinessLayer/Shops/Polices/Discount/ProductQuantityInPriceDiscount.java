package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;

public class ProductQuantityInPriceDiscount implements DiscountPolicy{

    int productID;
    int quantity;
    double priceForQuantity;

    public ProductQuantityInPriceDiscount(int productID, int quantity, double priceForQuantity) {
        this.productID = productID;
        this.quantity = quantity;
        this.priceForQuantity = priceForQuantity;
    }

    @Override
    public double calculateDiscount(Basket basket) {
        if (basket.getProducts().containsKey(productID)) {
            int quantityOfPid = basket.getProducts().get(productID);
            return quantityOfPid * basket.getPrices().get(productID) - ((quantityOfPid / quantity) * priceForQuantity + (quantityOfPid % quantity) * basket.getPrices().get(productID));
        }
        return 0;
    }
}
