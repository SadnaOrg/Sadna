package Threads;

import Bridge.UserBridge;
import Mocks.Guest;
import Mocks.RegistrationInfo;
import Mocks.User;
import Tests.UserTests;

public class ACEFounderPurchase extends Thread{
    public void run(){
        UserBridge bridge = UserTests.getUserBridge();
        int castroID = UserTests.shops[UserTests.castro_ID].ID;
        Guest g = bridge.visit();
        User MegaSportFounder = bridge.login(g,new RegistrationInfo("ACEFounder","ACE_rocks"));

        bridge.addProductToCart(MegaSportFounder.ID,castroID,2,21);
    }
}
