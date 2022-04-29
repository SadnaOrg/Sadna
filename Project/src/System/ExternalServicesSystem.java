package System;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ExternalServicesSystem {
    private ConcurrentHashMap<Integer, Payment> payment;
    private ConcurrentHashMap<Integer, Supply> supply;

    public ExternalServicesSystem(){
        payment = new ConcurrentHashMap<>();
        supply = new ConcurrentHashMap<>();
    }
}
