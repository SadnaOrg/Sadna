package AcceptanceTests.Tests;

import AcceptanceTests.DataObjects.User;

public class GuestTests extends UserTests {
    public User enter(){
        return userBridge.visit();
    }
}
