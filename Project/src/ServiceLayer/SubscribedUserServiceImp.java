package ServiceLayer;

import BusinessLayer.Shops.PurchaseHistory;
import BusinessLayer.Shops.ShopInfo;
import BusinessLayer.Users.AdministratorInfo;
import BusinessLayer.Users.BaseActions.BaseActionType;
import BusinessLayer.Users.SubscribedUser;
import BusinessLayer.Users.SystemManager;
import ServiceLayer.interfaces.SubscribedUserService;
import ServiceLayer.interfaces.SystemManagerService;

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
    public Result logout(){///TODO: IMPLEMENT
        return ifUserNotNullRes(()->userController.logout(currUser.getUserName()));
    }

    @Override
    public Response<SubscribedUserService> login(String username, String password) {
        return Response.tryMakeResponse(()-> userController.login(username, password,currUser) ,"incorrect user name or password")
                .safe((u)->{this.setCurrUser(u);return this;});
    }

    @Override
    public Response<ShopInfo> openShop(String name){
        return ifUserNotNullRes(()->new ShopInfo(shopController.openShop(currUser,name)));
    }

    @Override
    public Result assignShopManager(int shop, String userNameToAssign){
        return ifUserNotNull(()-> userController.assignShopManager(currUser,shop,userNameToAssign));
    }

    @Override
    public Result assignShopOwner(int shop, String userNameToAssign){
        return ifUserNotNull(()-> userController.assignShopOwner(currUser,shop,userNameToAssign));
    }

    @Override
    public Result changeManagerPermission(int shop, String userNameToAssign, Collection<BaseActionType> types){
        return ifUserNotNull(()-> userController.changeManagerPermission(currUser,shop,userNameToAssign,types));
    }

    @Override
    public Result closeShop(int shop){
        return ifUserNotNull(()-> userController.closeShop(currUser,shop));
    }

    @Override
    public Response<Collection<AdministratorInfo>> getAdministratorInfo(int shop){
        return ifUserNotNullRes(()-> userController.getAdministratorInfo(currUser,shop));
    }

    @Override
    public Response<Collection<PurchaseHistory>> getHistoryInfo(int shop){
        return ifUserNotNullRes(()-> userController.getHistoryInfo(currUser,shop));
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
