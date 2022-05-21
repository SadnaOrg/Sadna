package ServiceLayer;

import BusinessLayer.Products.Users.SystemManager;
import ServiceLayer.Objects.PurchaseHistoryInfo;
import ServiceLayer.interfaces.SystemManagerService;

public class SystemManagerServiceImp extends SubscribedUserServiceImp implements SystemManagerService {

    SystemManager currUser;

    public SystemManagerServiceImp(SystemManager currUser) {
        super(currUser);
        setCurrUser(currUser);
    }

    @Override
    public Response<PurchaseHistoryInfo> getShopsAndUsersInfo(int shop, String userName) {
        return ifUserNotNullRes(() -> new PurchaseHistoryInfo(facade.getShopsAndUsersInfo(currUser, shop, userName)), "get shops and users info succeeded");

    }

    @Override
    public Response<PurchaseHistoryInfo> getShopsAndUsersInfo(String userName) {
        return ifUserNotNullRes(() ->  new PurchaseHistoryInfo(facade.getShopsAndUsersInfo(currUser, userName)), "get shops and users info succeeded");
    }

    @Override
    public Response<PurchaseHistoryInfo> getShopsAndUsersInfo(int shop) {
        return ifUserNotNullRes(() ->  new PurchaseHistoryInfo(facade.getShopsAndUsersInfo(currUser, shop)), "get shops and users info succeeded");
    }

    @Override
    public Response<PurchaseHistoryInfo> getShopsAndUsersInfo() {
        return ifUserNotNullRes(() ->  new PurchaseHistoryInfo(facade.getShopsAndUsersInfo(currUser)), "get shops and users info succeeded");
    }

    protected void setCurrUser(SystemManager currUser) {
        super.setCurrUser(currUser);
        this.currUser = currUser;
    }


    private Result ifUserNotNull(MySupplier<Boolean> s, String eventName) {
        return Result.tryMakeResult((() -> currUser != null && !currUser.isLoggedIn() && s.get()), eventName, "log in to the system first as a subscribed user");
    }

    private Result ifUserNull(MySupplier<Boolean> s, String eventName) {
        return Result.tryMakeResult((() -> currUser == null && s.get()), eventName, "logout from system first");
    }

    private <T> Response<T> ifUserNotNullRes(MySupplier<T> s, String eventName) {
        return Response.tryMakeResponse((() -> currUser == null || !currUser.isLoggedIn() ? null : s.get()), eventName, "log in to the system first as a subscribed user");
    }

    private <T> Response<T> ifUserNullRes(MySupplier<T> s, String eventName) {
        return Response.tryMakeResponse((() -> currUser != null ? null : s.get()), eventName, "logout from system first");
    }
}
