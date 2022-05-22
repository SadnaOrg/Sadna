package com.example.application.views.main;

import ServiceLayer.Objects.Shop;
import ServiceLayer.interfaces.UserService;
import com.example.application.Header.Header;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.router.Route;

import static com.example.application.Header.SessionData.show;

@Route("Shop")
public class ShopView extends Header {

    public ShopView() {
        service = (UserService)show("service");
        Grid<Shop> shops = new Grid<>();
        shops.addColumn(Shop::shopId).setHeader("ID");
        shops.addColumn(Shop::shopName).setHeader("Name");
        shops.addColumn(Shop::shopDescription).setHeader("Description");
        content.add(shops);
    }
}