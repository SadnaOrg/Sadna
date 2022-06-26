package com.example.application.Parser;

import ServiceLayer.Result;
import ServiceLayer.interfaces.UserService;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class RegisterLine extends ParsedLine {
    String name;
    String password;
    String bDay;
   static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MM-yyyy");
    public RegisterLine(String name, String password, String bDay) {
        this.name = name;
        this.password = password;
        this.bDay = bDay;
    }


    @Override
    public UserService act(UserService u) throws RuntimeException {

        var res = u.registerToSystem(name,password,Date.from(LocalDate.parse(bDay, formatter).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        return ParsedLine.getUserService(u, res);
    }



    @Override
    public String toString() {
        return "RegisterLine{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", bDay='" + bDay + '\'' +
                '}';
    }
}
