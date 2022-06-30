package BusinessLayer.Shops.Polices.Purchase;

import BusinessLayer.Mappers.Converter;

public interface ConvertablePurchase {
    ORM.Shops.Purchases.PurchasePolicy toEntity(Converter c, ORM.Shops.Shop shop);
}