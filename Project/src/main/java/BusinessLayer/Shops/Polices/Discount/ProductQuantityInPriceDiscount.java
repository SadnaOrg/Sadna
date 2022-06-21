package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Mappers.ShopMappers.Converter;
import BusinessLayer.Users.Basket;
import com.SadnaORM.ShopImpl.ShopObjects.Discounts.DiscountPolicyDTO;

public class ProductQuantityInPriceDiscount implements DiscountPolicy{
    private int discountId;

    private int productID;
    private int quantity;
    private double priceForQuantity;

    public ProductQuantityInPriceDiscount(int productID, int quantity, double priceForQuantity) {
        this.productID = productID;
        this.quantity = quantity;
        this.priceForQuantity = priceForQuantity;
        this.discountId = atomicDiscountID.incrementAndGet();
    }

    public ProductQuantityInPriceDiscount(int discountId, int productID, int quantity, double priceForQuantity) {
        this.discountId = discountId;
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

    public int getDiscountId() {
        return discountId;
    }

    public int getProductID() {
        return productID;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPriceForQuantity() {
        return priceForQuantity;
    }

    @Override
    public boolean removeSonPredicate(int ID) {
        return false;
    }

    @Override
    public DiscountPolicyDTO conversion(Converter c) {
        return c.convert(this);
    }
}
