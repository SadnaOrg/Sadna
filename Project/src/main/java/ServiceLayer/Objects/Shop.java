package ServiceLayer.Objects;

import BusinessLayer.Shops.ShopInfo;

import java.util.Collection;
import java.util.stream.Collectors;

public record Shop(int shopId, String shopName, String shopDescription, Collection<Product> shopProducts) {

    public Shop(ShopInfo s) {
        this(s.getShopid(), s.getShopname(), s.getShopdescription(),s.getShopproductsinfo().values().stream().map(p->new Product(s.getShopid(),p)).collect(Collectors.toList()));
    }

    public Shop(BusinessLayer.Shops.Shop s) {
        this(new ShopInfo(s));
    }
}
