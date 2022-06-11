package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;

public class ProductQuantityInPriceDiscount implements DiscountPolicy{
    private int discountId;

    int productID;
    int quantity;
    double priceForQuantity;

    public ProductQuantityInPriceDiscount(int productID, int quantity, double priceForQuantity) {
        this.productID = productID;
        this.quantity = quantity;
        this.priceForQuantity = priceForQuantity;
        this.discountId = atomicDiscountID.incrementAndGet();
    }

    @Override
    public double calculateDiscount(Basket basket) {
        if (basket.getProducts().containsKey(productID)) {
            int quantityOfPid = basket.getProducts().get(productID);
            return quantityOfPid * basket.getPrices().get(productID) - ((quantityOfPid / quantity) * priceForQuantity + (quantityOfPid % quantity) * basket.getPrices().get(productID));
        }
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
}
