package ServiceLayer;

import BusinessLayer.Users.BaseActions.BaseActionType;
import BusinessLayer.Users.SubscribedUser;
import BusinessLayer.Users.SystemManager;
import ServiceLayer.Objects.AdministratorInfo;
import ServiceLayer.Objects.PurchaseHistoryInfo;
import ServiceLayer.Objects.Shop;
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
        return ifUserNotNullRes(()-> (currUser instanceof SystemManager) ? ((SystemManager) currUser) : null,"switch to system manager "+currUser.getUserName()).safe(SystemManagerServiceImp::new);
    }

    @Override
    public Result logout(){
        return ifUserNotNullRes(()->facade.logout(currUser.getUserName()),"logout");
    }

    @Override
    public Response<SubscribedUserService> login(String username, String password) {
        return Response.tryMakeResponse(()-> facade.login(username, password,currUser),"login - user "+username ,"incorrect user name or password")
                .safe((u)->{this.setCurrUser(u);
                    return this;},"login","");
    }

    @Override
    public Response<Shop> openShop(String name){
        return ifUserNotNullRes(()->new Shop(facade.openShop(currUser,name)),"open shop succeeded");
    }

    @Override
    public Result assignShopManager(int shop, String userNameToAssign) {
        return ifUserNotNull(()-> facade.assignShopManager(currUser,shop,userNameToAssign),(currUser.getUserName() + "assign "+userNameToAssign+" to shop manager "));
    }

    @Override
    public Result assignShopOwner(int shop, String userNameToAssign){
        return ifUserNotNull(()->  facade.assignShopOwner(currUser,shop,userNameToAssign),(currUser.getUserName() + "assign "+userNameToAssign+" to shop owner "));
    }

    @Override
    public Result changeManagerPermission(int shop, String userNameToAssign, Collection<BaseActionType> types){
        return ifUserNotNull(()->  facade.changeManagerPermission(currUser,shop,userNameToAssign,types),"change manager permission succeeded");
    }

    @Override
    public Result closeShop(int shop){
        return ifUserNotNull(()-> facade.closeShop(currUser,shop),"close shop succeeded");
    }

    @Override
    public Response<AdministratorInfo> getAdministratorInfo(int shop){
        return ifUserNotNullRes(()-> new AdministratorInfo(facade.getAdministratorInfo(currUser,shop)), currUser.getUserName()+"get administrator info ");
    }

    @Override
    public Response<PurchaseHistoryInfo> getHistoryInfo(int shop){
        return ifUserNotNullRes(()->  new PurchaseHistoryInfo(facade.getHistoryInfo(currUser,shop)), currUser.getUserName()+" get history info");
    }

    protected void setCurrUser(SubscribedUser currUser) {
        this.currUser = currUser;
        super.currUser = currUser;
    }

    private Result ifUserNotNull(MySupplier<Boolean> s,String eventName){
        return Result.tryMakeResult((() -> currUser != null && !currUser.isLoggedIn()&& s.get()) ,eventName,"log in to the system first as a subscribed user");
    }

    private Result ifUserNull(MySupplier<Boolean> s,String eventName){
        return Result.tryMakeResult((() -> currUser == null && s.get()) ,eventName,"logout from system first");
    }

    private <T> Response<T> ifUserNotNullRes(MySupplier<T> s,String eventName){
        return Response.tryMakeResponse((() -> currUser == null|| !currUser.isLoggedIn() ? null:  s.get()),eventName,"log in to the system first as a subscribed user");
    }

    private <T> Response<T> ifUserNullRes(MySupplier<T> s,String eventName){
        return Response.tryMakeResponse((() -> currUser != null ? null:  s.get()),eventName ,"logout from system first");
    }


}
