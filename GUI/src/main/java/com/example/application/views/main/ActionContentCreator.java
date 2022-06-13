package com.example.application.views.main;

import ServiceLayer.BaseActionType;
import ServiceLayer.Objects.Product;
import ServiceLayer.Objects.Shop;
import ServiceLayer.interfaces.SubscribedUserService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import static com.example.application.Utility.notifySuccess;
import static com.example.application.Utility.notifyError;


import java.util.*;
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
}
