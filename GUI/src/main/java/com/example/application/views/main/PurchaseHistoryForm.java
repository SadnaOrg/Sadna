package com.example.application.views.main;

import ServiceLayer.Objects.*;
import ServiceLayer.Response;
import ServiceLayer.interfaces.SystemManagerService;
import ServiceLayer.interfaces.UserService;
import com.example.application.Header.Header;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.application.Header.SessionData.Load;

public class PurchaseHistoryForm extends Header {
    SystemManagerService service;
    String username;
    VerticalLayout layout = new VerticalLayout();
    Select<Integer> shopField = createShopField("Shop Name");
    TextField userField = createTextField("User Name", username);
    Button searchButton = new Button("Search Products");
    Accordion purchaseHistory = new Accordion();

    public PurchaseHistoryForm(SystemManagerService service) {
        this.service = service;
        this.username = (String) Load("user-name");
        searchButton.setEnabled(true);
        searchButton.addClickListener(click -> searchPurchaseHistory());
        layout.add(userField, shopField, searchButton, purchaseHistory);
        setContent(layout);
        registerToNotification(service);
    }

    private void searchPurchaseHistory() {
        var oldPurchaseHistory = purchaseHistory;
        purchaseHistory = new Accordion();
        Response<PurchaseHistoryInfo> res;
        if(!Objects.equals(userField.getValue(), "") && shopField.getValue() != null){
            res = service.getShopsAndUsersInfo(shopField.getValue(), userField.getValue());
        }
        else if(!Objects.equals(userField.getValue(), "")){
            res = service.getShopsAndUsersInfo(userField.getValue());
        }
        else if(shopField.getValue() != null){
            res = service.getShopsAndUsersInfo(userField.getValue());
        }
        else{
            res = service.getShopsAndUsersInfo();
        }
        if(res != null && res.isOk()){
            var history = res.getElement();
            for(PurchaseHistory purchase : history.historyInfo()){
                String summary = "Purchase history of user: " + purchase.user() + " in shop " + purchase.shop().shopName() + ":";
                purchaseHistory.add(summary, createPurchaseHistoryComponent(purchase));
            }
        }
        purchaseHistory.setWidthFull();
        layout.remove(oldPurchaseHistory);
        layout.add(purchaseHistory);
        setContent(layout);
    }

    private Component createPurchaseHistoryComponent(PurchaseHistory purchase) {
        VerticalLayout layout = new VerticalLayout();
        Accordion purchases = new Accordion();
        for(Purchase p : purchase.purchases()){
            purchases.add(p.dateOfPurchase().toString(), createProductInfoComponent(p.products()));
        }
        purchases.setWidthFull();
        layout.add(purchases);
        layout.setWidthFull();
        return layout;
    }

    private Component createProductInfoComponent(List<ProductInfo> products) {
        Grid<Product> productGrid = createProductGrid();
        List<Product> productList = new ArrayList<>();
        for(ProductInfo p : products){
            var res = service.searchProducts(sp -> sp.shopId() == p.shopId(), pp -> pp.productID() == p.Id());
            if(res.isOk()){
                var shopRes = res.getElement().shops();
                if(shopRes.size() == 1){
                    var prodRes = shopRes.stream().toList().get(0).shopProducts();
                    if(prodRes.size() == 1){
                        var product = prodRes.stream().toList().get(0);
                        productList.add(product);
                    }
                }
            }
        }
        productGrid.setItems(productList);
        productGrid.setWidthFull();
        return productGrid;
    }

    private Grid<Product> createProductGrid() {
        Grid<Product> grid = new Grid<>();
        grid.addColumn(Product::name).setHeader("Product Name");
        grid.addColumn(Product::price).setHeader("Price");
        grid.addColumn(Product::quantity).setHeader("Quantity");
        grid.addColumn(Product::description).setHeader("Description");
        grid.addColumn(Product::manufacturer).setHeader("Manufacturer");
        return grid;
    }

    private TextField createTextField(String username, String placeholder) {
        TextField field = new TextField();
        field.setLabel(username);
        field.setClearButtonVisible(true);
        field.setPlaceholder(placeholder);
        return field;
    }

    private Select<Integer> createShopField(String label) {
        Select<Integer> select = new Select<>();
        select.setLabel(label);
        select.setEmptySelectionAllowed(true);
        if(service != null) {
            var res = service.searchProducts(sp -> true, pp -> true);
            if (res.isOk()) {
                List<Integer> ids = res.getElement().shops().stream().map(Shop::shopId).toList();
                select.setItems(ids);
            }
        }
        return select;
    }
}
