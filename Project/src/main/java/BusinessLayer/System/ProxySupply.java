package main.java.BusinessLayer.System;

import main.java.BusinessLayer.Products.ProductInfo;
import main.java.BusinessLayer.Users.User;

public class ProxySupply implements Supply {
    private Supply s = null;
    @Override
    public boolean checkSupply(PackageInfo pack) {
        for(ProductInfo p : pack.getPack()){
            if(p.getProductquantity() <= 0)
                return false;
        }
        if(s != null)
            return s.checkSupply(pack);
        return true;
    }
}
