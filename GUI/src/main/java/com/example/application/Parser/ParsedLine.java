package com.example.application.Parser;

import ServiceLayer.interfaces.UserService;
import com.google.gson.Gson;

public interface ParsedLine {
     UserService act(UserService u) throws RuntimeException;
     static ParsedLine GetLine(String name ,String json){
         var gson = new Gson();
        return gson.fromJson(json, switch (name.trim()){
            case "Register" -> RegisterLine.class;
            case "Login as guest" -> LoginAsGuest.class;
            default -> {throw new UnsupportedOperationException("function "+name+" douse not exsist in the grammar");}
        });
     }
}
