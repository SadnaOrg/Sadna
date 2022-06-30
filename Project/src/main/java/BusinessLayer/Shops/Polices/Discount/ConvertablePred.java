package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Mappers.Converter;
import BusinessLayer.Shops.Shop;

public interface ConvertablePred {
    ORM.Shops.Discounts.DiscountPred toEntity(Converter c, ORM.Shops.Shop shop);
}