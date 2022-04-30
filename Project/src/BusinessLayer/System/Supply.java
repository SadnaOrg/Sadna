package BusinessLayer.System;

import BusinessLayer.Users.User;

public abstract class Supply {
    int id;
    public abstract boolean checkSupply(int userId, int packageNumber);
}
