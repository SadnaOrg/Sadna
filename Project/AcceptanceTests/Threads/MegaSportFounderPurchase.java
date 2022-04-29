package Threads;

import Bridge.UserBridge;
import Mocks.Guest;
import Mocks.RegistrationInfo;
import Mocks.User;
import Tests.UserTests;

public class MegaSportFounderPurchase extends Thread{
    public void run(){
        UserBridge bridge = UserTests.getUserBridge();
        int castroID = UserTests.shops[UserTests.castro_ID].ID;
        Guest g = bridge.visit();
        User MegaSportFounder = bridge.login(g,new RegistrationInfo("castroFounder","castro_rocks"));

        bridge.addProductToCart(MegaSportFounder.ID,castroID,2,10);
    }
}
