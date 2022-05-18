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

    public void run(){
        SubscribedUserBridge bridge = SubscribedUserTests.getUserBridge();
        Guest g = bridge.visit();
        User founder = bridge.login(g.name,new RegistrationInfo(subscribedUser.name, subscribedUser.password));

        bridge.appointManager(shopID,founder.name,SubscribedUserTests.getU3Name());
        bridge.exit(subscribedUser.name);
    }
}
