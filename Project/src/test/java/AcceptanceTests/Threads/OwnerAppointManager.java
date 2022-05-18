package AcceptanceTests.Threads;

import AcceptanceTests.Bridge.SubscribedUserBridge;
import AcceptanceTests.DataObjects.Guest;
import AcceptanceTests.DataObjects.RegistrationInfo;
import AcceptanceTests.DataObjects.SubscribedUser;
import AcceptanceTests.DataObjects.User;
import AcceptanceTests.Tests.SubscribedUserTests;

public class OwnerAppointManager extends Thread{
    private final SubscribedUser subscribedUser;
    private int shopID;
    private boolean status;

    public OwnerAppointManager(int shopID,SubscribedUser user){
        this.shopID = shopID;
        this.subscribedUser = user;
    }

    public boolean getStatus(){
        return status;
    }

    public void run(){
        SubscribedUserBridge bridge = SubscribedUserTests.getUserBridge();
        Guest g = bridge.visit();
        User owner = bridge.login(g.name,new RegistrationInfo(subscribedUser.name, subscribedUser.password));

        status = bridge.appointManager(shopID,owner.name,SubscribedUserTests.getU3Name());
    }
}
