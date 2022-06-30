package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Mappers.Converter;
import BusinessLayer.Shops.Shop;

public interface ConvertableDiscount {
    ORM.Shops.Discounts.DiscountPolicy toEntity(Converter c, ORM.Shops.Shop shop);
}