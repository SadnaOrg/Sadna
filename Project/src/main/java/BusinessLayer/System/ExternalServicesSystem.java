package main.java.BusinessLayer.System;

import main.java.BusinessLayer.Shops.Purchase;

import java.util.concurrent.ConcurrentHashMap;

public class ExternalServicesSystem {
    private ConcurrentHashMap<Integer, Payment> payment;
    private ConcurrentHashMap<Integer, Supply> supply;

    public ExternalServicesSystem(){
        payment = new ConcurrentHashMap<>();
        supply = new ConcurrentHashMap<>();
    }
    public boolean pay(double totalPrice, PaymentMethod method)
    {
        boolean flag = false;
        for(Payment p:payment.values())
        {
            flag=p.pay(totalPrice, method);
            if (flag)
            {
                return true;
            }
        }
        return flag;
    }

}
