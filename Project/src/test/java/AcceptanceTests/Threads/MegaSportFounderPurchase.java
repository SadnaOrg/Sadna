package AcceptanceTests.Threads;

import AcceptanceTests.Bridge.SubscribedUserBridge;
import AcceptanceTests.DataObjects.Guest;
import AcceptanceTests.DataObjects.RegistrationInfo;
import AcceptanceTests.DataObjects.User;
import AcceptanceTests.Tests.SubscribedUserTests;
import AcceptanceTests.Tests.UserTests;
// USING MEGASPORT FOUNDER
public class MegaSportFounderPurchase extends Thread{
    private double bought;
    public void run(){
        SubscribedUserBridge bridge = SubscribedUserTests.getUserBridge();
        int castroID = UserTests.shops[UserTests.castro_ID].ID;
        bridge.addProductToCart("MegaSportFounder",castroID,45,10);
        bought = bridge.purchaseCart("MegaSportFounder","4580470047368888",564,9,13);
    }
}
