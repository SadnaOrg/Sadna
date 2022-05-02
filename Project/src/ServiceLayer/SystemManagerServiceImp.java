package ServiceLayer;

import BusinessLayer.Shops.PurchaseHistory;
import BusinessLayer.Users.SystemManager;
import ServiceLayer.interfaces.SystemManagerService;

import java.util.Collection;

public class SystemManagerServiceImp extends SubscribedUserServiceImp implements SystemManagerService {

    SystemManager currUser;

    public SystemManagerServiceImp(SystemManager currUser) {
        super(currUser);
        setCurrUser(currUser);
    }

    @Override
    public Response<Collection<PurchaseHistory>> getShopsAndUsersInfo(int shop, String userName){
        return ifUserNotNullRes(()->userController.getShopsAndUsersInfo(currUser,shop,userName));
    }

    @Override
    public Response<Collection<PurchaseHistory>> getShopsAndUsersInfo(String userName){
        return ifUserNotNullRes(()->userController.getShopsAndUsersInfo(currUser,userName));
    }

    @Override
    public Response<Collection<PurchaseHistory>> getShopsAndUsersInfo(int shop){
        return ifUserNotNullRes(()->userController.getShopsAndUsersInfo(currUser,shop));
    }

    @Override
    public Response<Collection<PurchaseHistory>> getShopsAndUsersInfo(){
        return ifUserNotNullRes(()->userController.getShopsAndUsersInfo(currUser));
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
