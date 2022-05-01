package Threads;

import Bridge.SubscribedUserBridge;
import DataObjects.Guest;
import DataObjects.RegistrationInfo;
import DataObjects.SubscribedUser;
import DataObjects.User;
import Tests.SubscribedUserTests;

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
        User owner = bridge.login(g.ID,new RegistrationInfo(subscribedUser.username, subscribedUser.password));

        status = bridge.appointManager(shopID,owner.ID,SubscribedUserTests.getU3ID());
    }
}
