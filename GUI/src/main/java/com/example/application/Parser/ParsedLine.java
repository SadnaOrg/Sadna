package com.example.application.Parser;

import ServiceLayer.Response;
import ServiceLayer.Result;
import ServiceLayer.interfaces.SubscribedUserService;
import ServiceLayer.interfaces.UserService;
import com.google.gson.Gson;

import java.util.Objects;
import java.util.function.Function;

public interface ParsedLine {
     UserService act(UserService u) throws RuntimeException;
     static ParsedLine GetLine(String name ,String json){
         var gson = new Gson();
        return gson.fromJson(json, switch (name.trim()){
            case "Logout" -> Logout.class;
            case "Open shop" -> OpenShop.class;
            case "Add product to cart"-> AddToCart.class;
            case "Login" -> Login.class;
            case "Register" -> RegisterLine.class;
            case "Login as guest" -> LoginAsGuest.class;
            case "Add product to shop" -> AddProductToShop.class;
            case "Purchase cart"-> PurchaseCart.class;
            case "Assign Owner"-> AssignOwner.class;
            case "Assign Manager"-> AssignManager.class;
            case "Remove Product"-> RemoveProduct.class;
            default -> {throw new UnsupportedOperationException("function "+name+" douse not exist in the grammar");}
        });
     }
     static  UserService getUserService(UserService u, Result res) {
        if(res.isOk())
            return u;
        throw new RuntimeException(res.getMsg());
    }
    static<T>  UserService getUserServiceRes(UserService u, Response<T> res) {
        if(res.isOk())
            return u;
        throw new RuntimeException(res.getMsg());
    }
    static  UserService getUserService(UserService u, Response<? extends UserService> res) {
        if(res.isOk())
            return res.getElement();
        throw new RuntimeException(res.getMsg());
    }

    static UserService getIfSubUserService(UserService u, Function<SubscribedUserService, UserService> f){
         if(u instanceof SubscribedUserService service){
             return f.apply(service);
         }
         throw new RuntimeException("not logged in as Subscribed user");
    }
}
