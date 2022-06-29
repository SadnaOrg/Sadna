package ServiceLayer;

import BusinessLayer.Users.SystemManager;
import ServiceLayer.Objects.PurchaseHistoryInfo;
import ServiceLayer.Objects.Statistic;
import ServiceLayer.Objects.SubscribedUserInfo;
import ServiceLayer.interfaces.SystemManagerService;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
    @Override
    public Response<List<SubscribedUserInfo>> getAllSubscribedUserInfo(){
        return ifUserNotNullRes(()-> facade.getSubscribedUserInfo(currUser.getUserName()).entrySet().stream().map(SubscribedUserInfo::new).collect(Collectors.toList()),"get subscribed user info");
    }

    protected void setCurrUser(SystemManager currUser) {
        super.setCurrUser(currUser);
        this.currUser = currUser;
    }

    @Override
    public Result removeSubscribedUserFromSystem(String userToRemove){
        return ifUserNotNull(()->facade.removeSubscribedUserFromSystem(currUser,userToRemove),"removed "+ userToRemove +" from system");
    }

    @Override
    public Response<Collection<Statistic>> getStatistic(LocalDate from, LocalDate to){
        return ifUserNotNullRes(()->facade.getStatistic(from,to).stream().map(Statistic::new).collect(Collectors.toList()),"get statistic");
    }

    @Override
    public Response<Statistic> getStatistic(LocalDate day){
        return ifUserNotNullRes(()->new Statistic(facade.getStatistic(day)),"get statistic");
    }


    private Result ifUserNotNull(MySupplier<Boolean> s, String eventName) {
        return Result.tryMakeResult((() -> currUser != null && currUser.isLoggedIn() && s.get()), eventName, "log in to the system first as a subscribed user");
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
