package AcceptanceTests.Threads;

import AcceptanceTests.Bridge.SubscribedUserBridge;
import AcceptanceTests.DataObjects.Guest;
import AcceptanceTests.DataObjects.RegistrationInfo;
import AcceptanceTests.DataObjects.SubscribedUser;
import AcceptanceTests.DataObjects.User;
import AcceptanceTests.Tests.SubscribedUserTests;

public class UserBuysProduct extends Thread{
    private final SubscribedUser subscribedUser;
    private int shopID;
    private int productID;
    private boolean status;

    public UserBuysProduct(int shopID,int productID,SubscribedUser user){
        this.shopID = shopID;
        this.productID = productID;
        this.subscribedUser = user;
    }

    public boolean getStatus(){
        return status;
    }

    public void run(){
        SubscribedUserBridge bridge = SubscribedUserTests.getUserBridge();
        Guest g = bridge.visit();
        User user = bridge.login(g.ID,new RegistrationInfo(subscribedUser.username, subscribedUser.password));

        status = bridge.addProductToCart(user.ID,shopID,productID,1);

    }
}
