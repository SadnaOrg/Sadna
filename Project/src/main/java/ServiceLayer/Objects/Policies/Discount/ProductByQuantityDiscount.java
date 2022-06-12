package ServiceLayer.Objects.Policies.Discount;

public record ProductByQuantityDiscount(int discountId,int productId, int productQuantity, double discount) implements DiscountRules {

    public ProductByQuantityDiscount(BusinessLayer.Shops.Polices.Discount.ProductByQuantityDiscount productByQuantityDiscount)
    {
        this(productByQuantityDiscount.getDiscountId(), productByQuantityDiscount.getProductId(), productByQuantityDiscount.getProductQuantity(), productByQuantityDiscount.getDiscount());
    }

}
