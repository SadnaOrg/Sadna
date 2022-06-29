package com.example.application.views.main;

import BusinessLayer.Users.BaseActions.HistoryInfo;
import ServiceLayer.BaseActionType;
import ServiceLayer.Objects.*;
import ServiceLayer.interfaces.SubscribedUserService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import static com.example.application.Utility.notifySuccess;
import static com.example.application.Utility.notifyError;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public class ActionContentCreator {
    private SubscribedUserService service;
    private List<Integer> shops;
    private String currUser = null;

    public ActionContentCreator(SubscribedUserService service, List<Integer> shops) {
        this.service = service;
        var userInfo = service.getUserInfo();
        if(userInfo.isOk())
            currUser = userInfo.getElement().username;
        this.shops = shops;
    }

    public Component createCloseShop() {
        Select<Integer> selectShop = createShopSelect(Shop::isOpen);
        Button button = new Button("Close Shop");
        selectShop.addValueChangeListener(e -> button.setEnabled(selectShop.getValue() != null));
        button.addClickListener(e -> {
            var res = service.closeShop(selectShop.getValue());
            if(res.isOk()) {
                notifySuccess("removed owner successfully!");
                selectShop.setValue(null);
            }
            else
                notifyError(res.getMsg());
        });
        VerticalLayout layout = new VerticalLayout();
        layout.add(selectShop, button);
        return layout;
    }

    public Component createAssignShopOwner() {
        Select<Integer> shop = createShopSelect(s -> true);
        TextField shopOwnerField = new TextField("Assigning Shop Owner:");
        Button button = new Button("Assign Owner");
        shopOwnerField.addValueChangeListener(e -> button.setEnabled(shop.getValue() != null && !Objects.equals(shopOwnerField.getValue(), "")));
        shop.addValueChangeListener(e -> button.setEnabled(shop.getValue() != null && !Objects.equals(shopOwnerField.getValue(), "")));
        button.addClickListener(e -> {
            var res = service.assignShopOwner(shop.getValue(), shopOwnerField.getValue());
            if(res.isOk()) {
                notifySuccess("assigned owner successfully!");
                shop.setValue(null);
                shopOwnerField.setValue("");
            }
            else
                notifyError(res.getMsg());
        });
        VerticalLayout layout = new VerticalLayout();
        layout.add(shop, shopOwnerField, button);
        return layout;
    }

    public Component createAssignShopManager() {
        Select<Integer> shop = createShopSelect(s -> true);
        TextField shopManagerField = new TextField("Assigning Shop Manager:");
        Button button = new Button("Assign Manager");
        shopManagerField.addValueChangeListener(e -> button.setEnabled(shop.getValue() != null && !Objects.equals(shopManagerField.getValue(), "")));
        shop.addValueChangeListener(e -> button.setEnabled(shop.getValue() != null && !Objects.equals(shopManagerField.getValue(), "")));
        button.addClickListener(e -> {
            var res = service.assignShopManager(shop.getValue(), shopManagerField.getValue());
            if(res.isOk()) {
                notifySuccess("assigned manager successfully!");
                shop.setValue(null);
                shopManagerField.setValue("");
            }
            else
                notifyError(res.getMsg());
        });
        VerticalLayout layout = new VerticalLayout();
        layout.add(shop, shopManagerField, button);
        return layout;
    }

    public Component createReopenShop() {
        Select<Integer> selectShop = createShopSelect(s -> true);
        Button button = new Button("Reopen Shop");
        selectShop.addValueChangeListener(e -> button.setEnabled(selectShop.getValue() != null));
        button.addClickListener(e -> {
            var res = service.reopenShop(selectShop.getValue());
            if(res.isOk()) {
                notifySuccess("reopened shop successfully!");
                selectShop.setValue(null);
            }
            else
                notifyError(res.getMsg());
        });
        VerticalLayout layout = new VerticalLayout();
        layout.add(selectShop, button);
        return layout;
    }

    public Component createRemoveAdmin() {
        Select<Integer> shop = createShopSelect(s -> true);
        TextField removeAdminField = new TextField("Removing Admin:");
        Button button = new Button("Remove Admin");
        removeAdminField.addValueChangeListener(e -> button.setEnabled(shop.getValue() != null && !Objects.equals(removeAdminField.getValue(), "")));
        shop.addValueChangeListener(e -> button.setEnabled(shop.getValue() != null && !Objects.equals(removeAdminField.getValue(), "")));
        button.addClickListener(e -> {
            var res = service.removeAdmin(shop.getValue(), removeAdminField.getValue());
            if(res.isOk()) {
                notifySuccess("removed admin successfully!");
                shop.setValue(null);
                removeAdminField.setValue("");
            }
            else
                notifyError(res.getMsg());
        });
        VerticalLayout layout = new VerticalLayout();
        layout.add(shop, removeAdminField, button);
        return layout;
    }

    public Component createChangeManagerPermission() {
        Select<Integer> shop = createShopSelect(Shop::isOpen);
        CheckboxGroup<String> checkbox = new CheckboxGroup<>();
        Button button = new Button("Change Permissions");
        TextField adminField = new TextField("Admin name:");
        shop.addValueChangeListener(e -> {
            if(e.getValue() == null || Objects.equals(adminField.getValue(), "")) {
                checkbox.setItems();
                button.setEnabled(false);
            }
            else {
                checkbox.setItems(Arrays.stream(BaseActionType.values()).map(Enum::name).toList());
                checkbox.select((service.getMyInfo(e.getValue()).getElement().getPermissions().stream().map(Enum::name)).toList());
                button.setEnabled(true);
            }
        });
        adminField.addValueChangeListener(e -> {
            if(shop.getValue() == null || Objects.equals(e.getValue(), "")) {
                checkbox.setItems();
                button.setEnabled(false);
            }
            else {
                checkbox.setItems(Arrays.stream(BaseActionType.values()).map(Enum::name).toList());
                checkbox.select((service.getMyInfo(shop.getValue()).getElement().getPermissions().stream().map(Enum::name)).toList());
                button.setEnabled(true);
            }
        });
        button.addClickListener(e -> {
            var res = service.changeManagerPermission(shop.getValue(), adminField.getValue(), permissionsToInt(checkbox.getValue()));
            if(res.isOk()){
                notifySuccess("Permissions changed successfully!");
            }
            else{
                notifyError(res.getMsg());
            }
        });
        VerticalLayout layout = new VerticalLayout();
        layout.add(shop, adminField, checkbox, button);
        return layout;
    }

    /*public Component createRemoveShopOwner() {

    }*/

    private Collection<Integer> permissionsToInt(Set<String> value) {
        List<Integer> ints = new ArrayList<>();
        for(String str : value){
            ints.add(BaseActionType.valueOf(str).getCode());
        }
        return ints;
    }

    private Select<Integer> createShopSelect(Predicate<Shop> filter) {
        Select<Integer> selectShop = new Select<>();
        selectShop.setLabel("Select Shop");
        selectShop.setItems(filterShopIds(filter));
        return selectShop;
    }

    private List<Integer> filterShopIds(Predicate<Shop> shopPredicate){
        var res = service.searchShops(shopPredicate, currUser);
        if(!res.isOk())
            return null;
        return res.getElement().shops().stream().map(Shop::shopId).filter(id -> shops.contains(id)).toList();
    }

    public Component createRoleInfo() {

        var selectShop =  createShopSelect(Shop::isOpen);
        AtomicInteger shop= new AtomicInteger();
        var bttn = new Button("show role info");
        var layout = new VerticalLayout(selectShop,bttn);
        bttn.setEnabled(false);

        selectShop.addValueChangeListener(e -> {
            if(e.getValue()==null)
                bttn.setEnabled(false);
            else{
                shop.set(e.getValue());
                bttn.setEnabled(true);
            }
        });
        bttn.addClickListener(e->{
            var res = service.getAdministratorInfo(shop.get());
            if(res.isOk()){
                var grid = getAdministratorGrid(res.getElement().administrators());
                layout.removeAll();
                layout.add(grid);
            }
            else{
                notifyError(res.getMsg());
            }
        });
        return layout;
    }

    private Grid<Administrator> getAdministratorGrid(List<Administrator> administrators) {
        var g = new Grid<Administrator>();
        g.setWidthFull();
        g.addColumn(Administrator::getUsername).setHeader("user name ");
        g.addColumn(a -> {
            return switch (a.getType()) {
                case MANAGER -> "MANAGER";
                case OWNER -> "OWNER";
                case FOUNDER -> "FOUNDER";
            };
        }).setHeader("Administrator Type");
        g.addColumn(Administrator::getAppointer).setHeader("user name ");
        g.addComponentColumn(a->createPermisionSlider(a.getPermissions())).setHeader("permission");
        g.setItems(administrators);
        return g;
    }

    private Details createPermisionSlider(Collection<BaseActionType> permissions) {
        VerticalLayout content = new VerticalLayout();
        permissions.forEach(p ->{
           content.add(new Span(switch (p){
               case STOCK_MANAGEMENT -> "STOCK MANAGMENT";
               case SET_PURCHASE_POLICY -> "SET PURCHASE POLICY";
               case ASSIGN_SHOP_OWNER -> "ASSING SHOP OWNER";
               case REMOVE_SHOP_OWNER -> "REMOVE SHOP OWNER";
               case ASSIGN_SHOP_MANAGER -> "ASSIGN SHOP MANAGER";
               case CHANGE_MANAGER_PERMISSION -> "CHANGE MANAGER PERMISSION";
               case CLOSE_SHOP -> "CLOSE SHOP";
               case REOPEN_SHOP -> "REOPEN SHOP";
               case ROLE_INFO -> "ROLE INFO";
               case HISTORY_INFO -> "HISTORY INFO";
               case REMOVE_ADMIN -> "REMOVE ADMIN";
           }));
        });
        return new Details("permission", content);
    }

    public Component createHistoryInfo() {
        var selectShop =  createShopSelect(Shop::isOpen);
        AtomicInteger shop= new AtomicInteger();
        var bttn = new Button("show history info");
        var layout = new VerticalLayout(selectShop,bttn);
        bttn.setEnabled(false);

        selectShop.addValueChangeListener(e -> {
            if(e.getValue()==null)
                bttn.setEnabled(false);
            else{
                shop.set(e.getValue());
                bttn.setEnabled(true);
            }
        });
        bttn.addClickListener(e->{
            var res = service.getHistoryInfo(shop.get());
            if(res.isOk()){
                var grid = getHistoryInfoGrid(res.getElement().historyInfo());
                layout.removeAll();
                layout.add(grid);
            }
            else{
                notifyError(res.getMsg());
            }
        });
        return layout;
    }

    private Grid<PurchaseHistory> getHistoryInfoGrid(List<PurchaseHistory> historyInfo) {
        var g = new Grid<PurchaseHistory>();
        g.addColumn(PurchaseHistory::user).setHeader("user name");
        g.addColumn(p->p.shop().shopId()).setHeader("shop ID");
        g.addColumn(p->p.shop().shopName()).setHeader("shop name");
        g.addComponentColumn(p->getPurcasesSlider(p.purchases())).setHeader("purchasses");
        g.setItems(historyInfo);
        return g;
    }

    private Component getPurcasesSlider(List<Purchase> purchases) {
        var dialog = new Dialog();
        var g = new Grid<Purchase>();
        String pattern = "dd-MM-yyyy";
        DateFormat format = new SimpleDateFormat(pattern);
        g.addColumn(p->format.format(p.dateOfPurchase())).setHeader("date");
        g.addColumn(Purchase::transectionId).setHeader("transaction id");
        g.addComponentColumn(p->productPopUp(p.products()));
        return getButton(purchases,dialog,g,"Purchases History");
    }

    private Component productPopUp(List<ProductInfo> products) {
        var dialog = new Dialog();
        var g = new Grid<ProductInfo>();
        g.addColumn(ProductInfo::Id).setHeader("id");
        g.addColumn(ProductInfo::quantity).setHeader("quantity");
        g.addColumn(ProductInfo::price).setHeader("price");
        return getButton(products, dialog, g,"Products");
    }

    private <T> Button getButton(Collection<T> products, Dialog dialog, Grid<T> g,String s) {
        g.setItems(products);
        var closeButton = new Button("close");
        dialog.add(new H1(s), g,closeButton);
        dialog.setWidthFull();
        g.setWidthFull();
        var b = new Button(s);
        b.addClickListener(e-> dialog.open());
        closeButton.addClickListener(e-> dialog.close());
        return b;
    }
}
