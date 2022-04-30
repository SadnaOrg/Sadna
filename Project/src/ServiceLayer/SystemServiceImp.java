package ServiceLayer;

import ServiceLayer.interfaces.SystemService;
import BusinessLayer.System.System;

public class SystemServiceImp implements SystemService {

    System system = System.getInstance();

    public boolean pay(double totalprice)
    {
        return system.pay(totalprice);
    }

}
