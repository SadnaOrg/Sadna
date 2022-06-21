package com.example.application.Parser;

import ServiceLayer.Response;
import ServiceLayer.Result;
import ServiceLayer.interfaces.SubscribedUserService;
import ServiceLayer.interfaces.UserService;
import com.google.gson.Gson;

import java.util.Objects;
import java.util.function.Function;

public abstract class ParsedLine {
     public abstract UserService act(UserService u) throws RuntimeException;

    public static String GetJson(ParsedLine line){
        var gson = new Gson();
        return getStr(line.getClass())+" : " + gson.toJson(line);
    }

    public static String getStr(Class<? extends ParsedLine> x) {
        if (Logout.class.equals(x)) {
            return "Logout";
        } else if (OpenShop.class.equals(x)) {
            return "Open shop";
        } else if (AddToCart.class.equals(x)) {
            return "Add product to cart";
        } else if (Login.class.equals(x)) {
            return "Login";
        } else if (RegisterLine.class.equals(x)) {
            return "Register";
        } else if (LoginAsGuest.class.equals(x)) {
            return "Login as guest";
        } else if (AddProductToShop.class.equals(x)) {
            return "Add product to shop";
        } else if (PurchaseCart.class.equals(x)) {
            return "Purchase cart";
        } else if (AssignOwner.class.equals(x)) {
            return "Assign Owner";
        } else if (AssignManager.class.equals(x)) {
            return "Assign Manager";
        } else if (RemoveProduct.class.equals(x)) {
            return "Remove Product";
        }
        throw new UnsupportedOperationException("function douse not exist in the grammar");

    }

    public static ParsedLine GetLine(String name, String json){
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
     public static  UserService getUserService(UserService u, Result res) {
        if(res.isOk())
            return u;
        throw new RuntimeException(res.getMsg());
    }
    public static<T>  UserService getUserServiceRes(UserService u, Response<T> res) {
        if(res.isOk())
            return u;
        throw new RuntimeException(res.getMsg());
    }
    public static  UserService getUserService(UserService u, Response<? extends UserService> res) {
        if(res.isOk())
            return res.getElement();
        throw new RuntimeException(res.getMsg());
    }

    public static UserService getIfSubUserService(UserService u, Function<SubscribedUserService, UserService> f){
         if(u instanceof SubscribedUserService service){
             return f.apply(service);
         }
         throw new RuntimeException("not logged in as Subscribed user");
    }

    @Override
    public String toString() {
        return GetJson(this);
    }
}
