package ServiceLayer;

import BusinessLayer.Users.SubscribedUser;
import ServiceLayer.interfaces.SubscribedUserService;

import java.util.function.Supplier;

public class SubscribedUserServiceImp extends UserServiceImp implements SubscribedUserService {
    SubscribedUser currUser;

    public SubscribedUserServiceImp(SubscribedUser currUser) {
        setCurrUser(currUser);
    }


    @Override
    public Result logout(){///TODO: IMPLEMENT
        throw new UnsupportedOperationException("TODO: IMPLEMENT");
    }

    @Override
    public Response<SubscribedUserService> login(String username, String password) {
        return Response.tryMakeResponse(()-> userController.login(username, password,currUser) ,"incorrect user name or password")
                .safe((u)->{this.setCurrUser(u);return this;});
    }

    @Override
    public Result openShop(String name){
        return ifUserNotNull(()->{shopController.openShop(currUser,name);return true;});
    }



    private void setCurrUser(SubscribedUser currUser) {
        this.currUser = currUser;
        super.currUser = currUser;
    }

    private Result ifUserNotNull(Supplier<Boolean> s){
        return Result.tryMakeResult((() -> currUser != null && s.get()) ,"log in to the system first as a subscribed user");
    }

    private Result ifUserNull(Supplier<Boolean> s){
        return Result.tryMakeResult((() -> currUser == null && s.get()) ,"logout from system first");
    }

    private <T> Response<T> ifUserNotNullRes(Supplier<T> s){
        return Response.tryMakeResponse((() -> currUser == null ? null:  s.get()),"log in to the system first as a subscribed user");
    }

    private <T> Response<T> ifUserNullRes(Supplier<T> s){
        return Response.tryMakeResponse((() -> currUser != null ? null:  s.get()),"logout from system first");
    }
}
