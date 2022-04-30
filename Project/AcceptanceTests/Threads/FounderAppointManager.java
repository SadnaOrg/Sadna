package Threads;

import Bridge.SubscribedUserBridge;
import Mocks.Guest;
import Mocks.RegistrationInfo;
import Mocks.SubscribedUser;
import Mocks.User;
import Tests.SubscribedUserTests;

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
        User founder = bridge.login(g.ID,new RegistrationInfo(subscribedUser.username, subscribedUser.password));

        bridge.appointManager(shopID,founder.ID,SubscribedUserTests.getU3ID());
    }
}
