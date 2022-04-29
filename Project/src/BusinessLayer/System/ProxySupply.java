package BusinessLayer.System;

import BusinessLayer.Users.User;

public class ProxySupply extends Supply {
    private Supply s = null;
    @Override
    public boolean checkSupply(int userId, int packageNumber) {
        if(s != null)
            return s.checkSupply(userId, packageNumber);
        return true;
    }
}
