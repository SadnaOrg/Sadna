package BusinessLayer.System;

import BusinessLayer.Products.ProductInfo;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class PackageInfo {
    AtomicInteger userId;
    String address;
    Collection<ProductInfo> pack;

    public PackageInfo(AtomicInteger userId, String address, Collection<ProductInfo> pack){
        this.userId = userId;
        this.address = address;
        this.pack = pack;
    }

    public Collection<ProductInfo> getPack() {
        return pack.stream().toList();
    }

    public String getAddress() {
        return address;
    }
}
