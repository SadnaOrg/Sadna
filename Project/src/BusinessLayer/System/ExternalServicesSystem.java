package BusinessLayer.System;

import BusinessLayer.Shops.Purchase;

import java.util.concurrent.ConcurrentHashMap;

public class ExternalServicesSystem {
    private ConcurrentHashMap<Integer, Payment> payment;
    private ConcurrentHashMap<Integer, Supply> supply;

    public ExternalServicesSystem(){
        payment = new ConcurrentHashMap<>();
        supply = new ConcurrentHashMap<>();
    }
    public boolean pay(double totalPrice)
    {
        boolean flag = false;
        for(Payment p:payment.values())
        {
            flag=p.pay(totalPrice);
            if (flag)
            {
                return true;
            }
        }
        return false;
    }

}
