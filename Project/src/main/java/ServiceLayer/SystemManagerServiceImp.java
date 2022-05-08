package main.java.ServiceLayer;

import main.java.BusinessLayer.Shops.PurchaseHistory;
import main.java.BusinessLayer.Users.SystemManager;
import main.java.ServiceLayer.interfaces.SystemManagerService;

import java.util.Collection;

public class SystemManagerServiceImp extends SubscribedUserServiceImp implements SystemManagerService {

    SystemManager currUser;

    public SystemManagerServiceImp(SystemManager currUser) {
        super(currUser);
        setCurrUser(currUser);
    }

    @Override
    public Response<Collection<PurchaseHistory>> getShopsAndUsersInfo(int shop, String userName){
        return ifUserNotNullRes(()-> {
            Collection<PurchaseHistory> ph = userController.getShopsAndUsersInfo(currUser,shop,userName);
            Log.getInstance().event("get shops and users info succeeded");
            return ph;
        });
    }

    @Override
    public Response<Collection<PurchaseHistory>> getShopsAndUsersInfo(String userName){
        return ifUserNotNullRes(()-> {
            Collection<PurchaseHistory> ph = userController.getShopsAndUsersInfo(currUser,userName);
            Log.getInstance().event("get shops and users info succeeded");
            return ph;
        });
    }

    @Override
    public Response<Collection<PurchaseHistory>> getShopsAndUsersInfo(int shop){
        return ifUserNotNullRes(()-> {
            Collection<PurchaseHistory> ph = userController.getShopsAndUsersInfo(currUser,shop);
            Log.getInstance().event("get shops and users info succeeded");
            return ph;
        });
    }

    @Override
    public Response<Collection<PurchaseHistory>> getShopsAndUsersInfo(){
        return ifUserNotNullRes(()-> {
            Collection<PurchaseHistory> ph = userController.getShopsAndUsersInfo(currUser);
            Log.getInstance().event("get shops and users info succeeded");
            return ph;
        });
    }

    protected void setCurrUser(SystemManager currUser) {
        super.setCurrUser(currUser);
        this.currUser=currUser;
    }



    private Result ifUserNotNull(MySupplier<Boolean> s){
        return Result.tryMakeResult((() -> currUser != null && !currUser.isLoggedIn()&& s.get()) ,"log in to the system first as a subscribed user");
    }

    private Result ifUserNull(MySupplier<Boolean> s){
        return Result.tryMakeResult((() -> currUser == null && s.get()) ,"logout from system first");
    }

    private <T> Response<T> ifUserNotNullRes(MySupplier<T> s){
        return Response.tryMakeResponse((() -> currUser == null|| !currUser.isLoggedIn() ? null:  s.get()),"log in to the system first as a subscribed user");
    }

    private <T> Response<T> ifUserNullRes(MySupplier<T> s){
        return Response.tryMakeResponse((() -> currUser != null ? null:  s.get()),"logout from system first");
    }
}
