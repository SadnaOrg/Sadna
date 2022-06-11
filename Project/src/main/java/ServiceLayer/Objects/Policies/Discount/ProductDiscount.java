package ServiceLayer.Objects.Policies.Discount;

public record ProductDiscount(int discountId,int productId, double discount) {
    public ProductDiscount(BusinessLayer.Shops.Polices.Discount.ProductDiscount productDiscount)
    {
        this(productDiscount.getDiscountId(), productDiscount.getProductId(), productDiscount.getDiscount());
    }
}
