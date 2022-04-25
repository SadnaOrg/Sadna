package test.Bridge;

import test.Mocks.*;

import java.util.List;

public class GuestProxy extends UserProxy {
    private int guestCounter = 0;

    public boolean registration(RegistrationInfo info) {
        return false;
    }

}
