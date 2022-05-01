package ServiceLayer;

import BusinessLayer.Products.Product;
import BusinessLayer.Products.ProductFilters;
import BusinessLayer.Shops.Shop;
import BusinessLayer.Shops.ShopFilters;
import BusinessLayer.Users.Guest;
import BusinessLayer.Users.SubscribedUser;
import BusinessLayer.Users.User;
import ServiceLayer.interfaces.GeneralService;
import ServiceLayer.interfaces.ShopService;
import ServiceLayer.interfaces.SystemService;
import ServiceLayer.interfaces.UserService;

import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

public class GeneralServiceImp extends UserServiceImp implements GeneralService {
    SubscribedUser currUser;

    public GeneralServiceImp(SubscribedUser currUser) {
        setCurrUser(currUser);
    }


    @Override
    public Result logout(){///TODO: IMPLEMENT
        throw new UnsupportedOperationException("TODO: IMPLEMENT");
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
