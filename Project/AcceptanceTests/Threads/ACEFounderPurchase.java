package Threads;

import Bridge.SubscribedUserBridge;
import Bridge.UserBridge;
import Mocks.Guest;
import Mocks.RegistrationInfo;
import Mocks.User;
import Tests.SubscribedUserTests;
import Tests.UserTests;

public class ACEFounderPurchase extends Thread{
    public void run(){
        SubscribedUserBridge bridge = SubscribedUserTests.getUserBridge();
        int castroID = UserTests.shops[UserTests.castro_ID].ID;
        Guest g = bridge.visit();
        User ACEFounder = bridge.login(g.ID,new RegistrationInfo("ACEFounder","ACE_rocks"));

        bridge.addProductToCart(ACEFounder.ID,castroID,2,21);
    }
}
