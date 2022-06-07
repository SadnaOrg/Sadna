package com.example.application;

import ServiceLayer.Objects.Product;
import ServiceLayer.Objects.Shop;
import ServiceLayer.Objects.ShopsInfo;
import ServiceLayer.Result;
import com.example.application.views.main.ProductView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

import java.util.Collection;
import java.util.function.Predicate;

public class Utility {
    public static final String projectName = "Superli";
    public static final String projectDescription = "Made by Maor, Yuval, Michael, Tal and Guy";

    public static void notifyError(String message) {
        Notification notification = new Notification(message);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notification.setPosition(Notification.Position.BOTTOM_CENTER);
        notification.setDuration(5000);
        notification.open();
    }

    public static void notifySuccess(String message) {
        Notification notification = new Notification(message);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.setPosition(Notification.Position.BOTTOM_CENTER);
        notification.setDuration(5000);
        notification.open();
    }

    public static void notifyIsOk(Result res, String successMsg) {
        if (res.isOk())
            notifySuccess(successMsg);
        else
            notifyError(res.getMsg());
    }

    public static Collection<Product> getProducts(ShopsInfo shopsInfo) {
        return shopsInfo.shops().stream().map(Shop::shopProducts).flatMap(Collection::stream).toList();
    }
}