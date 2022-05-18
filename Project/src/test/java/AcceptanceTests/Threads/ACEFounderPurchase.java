package AcceptanceTests.Threads;

import AcceptanceTests.Bridge.SubscribedUserBridge;
import AcceptanceTests.DataObjects.Guest;
import AcceptanceTests.DataObjects.RegistrationInfo;
import AcceptanceTests.DataObjects.User;
import AcceptanceTests.Tests.SubscribedUserTests;
import AcceptanceTests.Tests.UserTests;

public class ACEFounderPurchase extends Thread{
    public void run(){
        SubscribedUserBridge bridge = SubscribedUserTests.getUserBridge();
        int castroID = UserTests.shops[UserTests.castro_ID].ID;
        Guest g = bridge.visit();
        User ACEFounder = bridge.login(g.name,new RegistrationInfo("ACEFounder","ACE_rocks"));

        bridge.addProductToCart(ACEFounder.name,castroID,2,21);
    }
}
