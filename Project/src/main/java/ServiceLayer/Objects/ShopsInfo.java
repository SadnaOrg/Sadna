package ServiceLayer.Objects;

import BusinessLayer.Products.Product;
import BusinessLayer.Shops.ShopInfo;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public record ShopsInfo(Collection<Shop> shops) {

    public ShopsInfo(ConcurrentHashMap<Integer, ShopInfo> shops) {
        this(shops.values().stream().map(Shop::new) .collect(Collectors.toList()));
    }

    public ShopsInfo(Map<BusinessLayer.Shops.Shop, Collection<Product>> products) {
        this(products.entrySet().stream().map(e-> new Shop(e.getKey().getId(),e.getKey().getName(),e.getKey().getDescription(),e.getValue().stream().map(p->new ServiceLayer.Objects.Product(e.getKey().getId(), p)).collect(Collectors.toList()), e.getKey().isOpen())).collect(Collectors.toList()));
    }
}
