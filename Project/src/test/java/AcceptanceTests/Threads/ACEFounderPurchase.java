package AcceptanceTests.Threads;

import AcceptanceTests.Bridge.SubscribedUserBridge;
import AcceptanceTests.DataObjects.Guest;
import AcceptanceTests.DataObjects.RegistrationInfo;
import AcceptanceTests.DataObjects.User;
import AcceptanceTests.Tests.SubscribedUserTests;
import AcceptanceTests.Tests.UserTests;
// USING ACE_FOUNDER
public class ACEFounderPurchase extends Thread{
    private boolean bought;
    public void run(){
        SubscribedUserBridge bridge = SubscribedUserTests.getUserBridge();
        int castroID = UserTests.shops[UserTests.castro_ID].ID;
        bridge.addProductToCart("ACEFounder",castroID,45,31);
        bought = bridge.purchaseCart("ACEFounder","4580470048676846",654,12,12);
    }
}
