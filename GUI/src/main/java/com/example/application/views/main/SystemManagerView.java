package com.example.application.views.main;

import BusinessLayer.Users.SystemManager;
import ServiceLayer.Objects.*;
import ServiceLayer.SystemManagerServiceImp;
import ServiceLayer.interfaces.SubscribedUserService;
import ServiceLayer.interfaces.SystemManagerService;
import ServiceLayer.interfaces.UserService;
import com.example.application.Header.Header;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.DomEvent;
import com.vaadin.flow.dom.DomEventListener;
import com.vaadin.flow.router.Route;

import java.util.Collection;
import java.util.List;

import static com.example.application.Header.SessionData.Load;
import static com.example.application.Header.SessionData.save;
import static com.example.application.Utility.notifyError;
import static com.example.application.Utility.notifySuccess;

@Route("SystemManager")
public class SystemManagerView extends Header {
    private SystemManagerService systemManagerService;
    private SubscribedUserService subscribedUserService;
    private String currUser;

    private TextField shop;
    private TextField username;

    public SystemManagerView() {
        this.systemManagerService = (SystemManagerService) Load("system-manager-service");
        this.subscribedUserService = (SubscribedUserService) Load("subscribed-user-service");
        currUser = (String)Load("user-name");
        createTabs();
        Button logoutButton = new Button("Logout", e -> {
            subscribedUserService.logoutSystem();
            save("user-name", subscribedUserService.getUserInfo().getElement().username);
            UI.getCurrent().navigate(MainView.class);
        });
        logoutButton.getStyle().set("margin-left", "auto");
        addToNavbar(logoutButton);
    }

    private void createTabs() {
        tabs = new Tabs();
        addTabWithClickEvent("Shop Info", this::shopInfoEvent);
        addTabWithClickEvent("Manage Subscribed Users", this::subscribedUsersEvent);
        addTabWithClickEvent("View All Users Info", this::userInfoEvent);
        systemManagerEvent();
        tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        addToDrawer(tabs);
    }

    private void userInfoEvent(DomEvent domEvent) {
        Grid<SubscribedUserInfo> infoGrid = new Grid<>();
        var res = systemManagerService.getAllSubscribedUserInfo();
        if(res.isOk()){
            List<SubscribedUserInfo> usersInfo = res.getElement();
            infoGrid.addColumn(u -> u.subscribedUser().username).setHeader("Username");
            infoGrid.addColumn(SubscribedUserInfo::state).setHeader("State");
            infoGrid.setItems(usersInfo);
        }
        infoGrid.setWidthFull();
        setContent(infoGrid);
    }

    private void subscribedUsersEvent(DomEvent domEvent) {
        VerticalLayout layout = new VerticalLayout();
        Select<String> selectUser = new Select<>();
        Button removeButton = new Button("Remove User");
        selectUser.addValueChangeListener(e -> removeButton.setEnabled(selectUser.getValue() != null));
        removeButton.addClickListener(e -> {
           var res = systemManagerService.removeSubscribedUserFromSystem(selectUser.getValue());
           if(res.isOk()){
               notifySuccess("Removed User " + selectUser.getValue());
               setUsersToRemove(selectUser);
           }
           else {
               notifyError("Could not delete user " + selectUser.getValue());
           }
        });
        selectUser.setEmptySelectionAllowed(true);
        setUsersToRemove(selectUser);
        layout.add(selectUser, removeButton);
        setContent(layout);
    }

    private void setUsersToRemove(Select<String> selectUser) {
        var resUsers = systemManagerService.getAllSubscribedUserInfo();
        if (resUsers.isOk()) {
            selectUser.setItems(resUsers.getElement().stream().filter(u -> u.state() != UserState.REMOVED).map(u -> u.subscribedUser().username).toList());
        }
    }

    private void systemManagerEvent() {
        var resManager = subscribedUserService.manageSystemAsSystemManager();
        if(resManager.isOk() && resManager.getElement() != null){
            addTabWithClickEvent("System Manager Menu", e -> {
                save("user-name", subscribedUserService.getUserInfo().getElement());
                save("system-manager-service", systemManagerService);
                save("subscribed-user-service", subscribedUserService);
                UI.getCurrent().navigate(SubscribedUserView.class);
            });
        }
    }

    private void shopInfoEvent(DomEvent domEvent) {
        PurchaseHistoryForm form = new PurchaseHistoryForm();
        setContent(form);
    }
}
