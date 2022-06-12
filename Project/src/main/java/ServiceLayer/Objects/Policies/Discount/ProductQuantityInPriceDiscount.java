package ServiceLayer.Objects.Policies.Discount;

public record ProductQuantityInPriceDiscount(int discountId,int productID, int quantity, double priceForQuantity) implements DiscountRules{

    public ProductQuantityInPriceDiscount(BusinessLayer.Shops.Polices.Discount.ProductQuantityInPriceDiscount productQuantityInPriceDiscount)
    {
        this(productQuantityInPriceDiscount.getDiscountId(), productQuantityInPriceDiscount.getProductID(), productQuantityInPriceDiscount.getQuantity(), productQuantityInPriceDiscount.getPriceForQuantity());
    }

}
