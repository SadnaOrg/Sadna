package main.java.ServiceLayer;

import main.java.BusinessLayer.Shops.PurchaseHistory;
import main.java.BusinessLayer.Shops.ShopInfo;
import main.java.BusinessLayer.Users.AdministratorInfo;
import main.java.BusinessLayer.Users.BaseActions.BaseActionType;
import main.java.BusinessLayer.Users.SubscribedUser;
import main.java.BusinessLayer.Users.SystemManager;
import main.java.ServiceLayer.interfaces.SubscribedUserService;
import main.java.ServiceLayer.interfaces.SystemManagerService;

import java.util.Collection;

public class SubscribedUserServiceImp extends UserServiceImp implements SubscribedUserService {
    SubscribedUser currUser;

    public SubscribedUserServiceImp(SubscribedUser currUser) {
        setCurrUser(currUser);
    }


    @Override
    public Response<SystemManagerService> manageSystemAsSystemManager(){
        return ifUserNotNullRes(()-> (currUser instanceof SystemManager) ? ((SystemManager) currUser) : null).safe(SystemManagerServiceImp::new);
    }

    @Override
    public Response<Boolean> logout(){///TODO: IMPLEMENT
        return ifUserNotNullRes(()->userController.logout(currUser.getUserName()));
    }

    @Override
    public Response<SubscribedUserService> login(String username, String password) {
        return Response.tryMakeResponse(()-> userController.login(username, password,currUser) ,"incorrect user name or password")
                .safe((u)->{this.setCurrUser(u);
                    Log.getInstance().event("login succeeded");
                    return this;});
    }

    @Override
    public Response<ShopInfo> openShop(String name){
        return ifUserNotNullRes(()-> {
            ShopInfo si = new ShopInfo(shopController.openShop(currUser,name));
            Log.getInstance().event("open shop succeeded");
            return si;
        });
    }

    @Override
    public Result assignShopManager(int shop, String userNameToAssign) {
        return ifUserNotNull(()-> {
            boolean res = userController.assignShopManager(currUser,shop,userNameToAssign);
            if (res)
                Log.getInstance().event("assign shop manager succeeded");
            else
                Log.getInstance().event("assign shop manager failed");
            return res;
        });
    }

    @Override
    public Result assignShopOwner(int shop, String userNameToAssign){
        return ifUserNotNull(()-> {
            boolean res = userController.assignShopOwner(currUser,shop,userNameToAssign);
            if (res)
                Log.getInstance().event("assign shop owner succeeded");
            else
                Log.getInstance().event("assign shop owner failed");
            return res;
        });
    }

    @Override
    public Result changeManagerPermission(int shop, String userNameToAssign, Collection<BaseActionType> types){
        return ifUserNotNull(()-> {
            boolean res = userController.changeManagerPermission(currUser,shop,userNameToAssign,types);
            if (res)
                Log.getInstance().event("change manager permission succeeded");
            else
                Log.getInstance().event("change manager permission failed");
            return res;
        });
    }

    @Override
    public Result closeShop(int shop){
        return ifUserNotNull(()-> {
            boolean res = userController.closeShop(currUser,shop);
            if (res)
                Log.getInstance().event("close shop succeeded");
            else
                Log.getInstance().event("close shop failed");
            return res;
        });
    }

    @Override
    public Response<Collection<AdministratorInfo>> getAdministratorInfo(int shop){
        return ifUserNotNullRes(()-> {
            Collection<AdministratorInfo> ai = userController.getAdministratorInfo(currUser,shop);
            Log.getInstance().event("assign shop owner succeeded");
            return ai;
        });
    }

    @Override
    public Response<Collection<PurchaseHistory>> getHistoryInfo(int shop){
        return ifUserNotNullRes(()-> {
            Collection<PurchaseHistory> ph = userController.getHistoryInfo(currUser,shop);
            Log.getInstance().event("assign shop owner succeeded");
            return ph;
        });
    }

    protected void setCurrUser(SubscribedUser currUser) {
        this.currUser = currUser;
        super.currUser = currUser;
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
