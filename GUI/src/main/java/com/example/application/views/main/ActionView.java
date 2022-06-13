package com.example.application.views.main;

import ServiceLayer.BaseActionType;
import ServiceLayer.Objects.*;
import ServiceLayer.interfaces.SubscribedUserService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

public class ActionView extends VerticalLayout {
    SubscribedUserService service;
    String currUser;
    MenuBar menuBar;
    public ActionView(SubscribedUserService service, String currUser){
        this.service = service;
        this.currUser = currUser;
        menuBar = createMenuBar();
        ConcurrentHashMap<BaseActionType, MenuItem> actionMenuItemMap = new ConcurrentHashMap<>();
        ConcurrentHashMap<BaseActionType, List<Integer>> actionShopsMap = new ConcurrentHashMap<>();

        for(var actionType : BaseActionType.values()){
            if(actionType == BaseActionType.STOCK_MANAGEMENT)
                continue;
            MenuItem item = menuBar.addItem(actionType.name());
            actionMenuItemMap.put(actionType, item);
            actionShopsMap.put(actionType, new ArrayList<>());
        }

        setPermissionsForUser(actionShopsMap);
        removeAll();
        for(var item : actionMenuItemMap.keySet()){
            if(actionShopsMap.get(item).size() == 0) {
                menuBar.remove(actionMenuItemMap.get(item));
                actionShopsMap.remove(item);
                actionMenuItemMap.remove(item);
            } else {
                MenuItem menuItem = actionMenuItemMap.get(item);
                menuItem.addClickListener(e -> {
                    removeAll();
                    add(menuBar, createContent(item, actionShopsMap.get(item)));
                });
            }
        }
        add(menuBar);

    }

    private void setPermissionsForUser(ConcurrentHashMap<BaseActionType, List<Integer>> actionShopsMap) {
        Predicate<Shop> shopPredicate = s -> true;
        var res = service.searchShops(shopPredicate, currUser);
        if(res == null || !res.isOk()) {
            return;
        }
        var shops = res.getElement().shops();
        for(Shop shop : shops){
            var userInfo = service.getMyInfo(shop.shopId());
            if(!userInfo.isOk())
                continue;
            var permissions = userInfo.getElement().getPermissions();
            for(BaseActionType permission : permissions){
                if(permission == BaseActionType.STOCK_MANAGEMENT)
                    continue;
                actionShopsMap.get(permission).add(shop.shopId());
            }
        }
    }

    private MenuBar createMenuBar(){
        MenuBar menuBar = new MenuBar();
        menuBar.setMinWidth("var(--lumo-size-m)");
        menuBar.setWidthFull();
        menuBar.setOpenOnHover(true);
        return menuBar;
    }

    private Component createContent(BaseActionType actionType, List<Integer> shops){
        ActionContentCreator actionContentCreator = new ActionContentCreator(service, shops);
        Component component;
        switch (actionType){
            case ROLE_INFO -> component = actionContentCreator.createRoleInfo();
            case CLOSE_SHOP -> component = actionContentCreator.createCloseShop();
            case HISTORY_INFO -> component = actionContentCreator.createHistoryInfo();
//            case STOCK_MANAGEMENT -> component = actionContentCreator.createStockManagement();
            case ASSIGN_SHOP_OWNER -> component = actionContentCreator.createAssignShopOwner();
            case ASSIGN_SHOP_MANAGER -> component = actionContentCreator.createAssignShopManager();
//            case REMOVE_SHOP_OWNER -> component = actionContentCreator.createRemoveShopOwner();
//            case SET_PURCHASE_POLICY -> component = actionContentCreator.createSetPurchasePolicy();
            case CHANGE_MANAGER_PERMISSION -> component = actionContentCreator.createChangeManagerPermission();
            case REOPEN_SHOP -> component = actionContentCreator.createReopenShop();
            case REMOVE_ADMIN -> component = actionContentCreator.createRemoveAdmin();
            default -> component = null;
        }
        return component;
    }
}
