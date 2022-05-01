package Tests;

import DataObjects.User;

public class GuestTests extends UserTests {
    public User enter(){
        return UserTests.userBridge.visit();
    }
}
