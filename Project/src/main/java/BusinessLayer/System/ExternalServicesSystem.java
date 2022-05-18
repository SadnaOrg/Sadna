package BusinessLayer.System;

import BusinessLayer.Products.ProductInfo;
import BusinessLayer.Shops.Purchase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ExternalServicesSystem {
    private Collection<Payment> payment;
    private Collection<Supply> supply;

    public ExternalServicesSystem(){
        payment = new ArrayList<>();
        supply = new ArrayList<>();
    }

    public boolean pay(double totalPrice, PaymentMethod method)
    {
        boolean flag = false;
        for(Payment p:payment.stream().toList())
        {
            flag=p.pay(totalPrice, method);
            if (flag)
            {
                return true;
            }
        }
        return flag;
    }

    public boolean checkSupply(PackageInfo pack){
        for(ProductInfo p : pack.getPack()){
            boolean flag = false;
            for(Supply s:supply.stream().toList())
            {
                flag=s.checkSupply(p) || flag;
            }
            if(!flag)
                return false;
        }
        return true;
    }

    public int getPaymentSize() {
        return payment.size();
    }

    public int getSupplySize() {
        return supply.size();
    }

    public synchronized void addPayment(Payment p){
        payment.add(p);
    }

    public synchronized void addSupply(Supply s){
        supply.add(s);
    }
}
