package AcceptanceTests.Threads;

import AcceptanceTests.Bridge.SubscribedUserBridge;
import AcceptanceTests.DataObjects.Guest;
import AcceptanceTests.DataObjects.RegistrationInfo;
import AcceptanceTests.DataObjects.SubscribedUser;
import AcceptanceTests.DataObjects.User;
import AcceptanceTests.Tests.SubscribedUserTests;

public class FounderAppointManager extends Thread{
    private final SubscribedUser subscribedUser;
    private int shopID;
    private boolean status;

    public FounderAppointManager(int shopID, SubscribedUser user){
        this.shopID = shopID;
        this.subscribedUser = user;
    }

    public boolean getStatus(){
        return status;
    }
// USING CASTRO FOUNDER
    public void run(){
        SubscribedUserBridge bridge = SubscribedUserTests.getUserBridge();
        bridge.appointManager(shopID,subscribedUser.name,SubscribedUserTests.getU3Name());
    }
}
