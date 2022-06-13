package com.example.application.views.main;

import ServiceLayer.Objects.*;
import ServiceLayer.interfaces.SystemManagerService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.dom.DomEvent;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static com.example.application.Utility.notifyError;
import static com.example.application.Utility.notifySuccess;

@Route("SystemManager")
public class SystemManagerView extends SubscribedUserView {
    SystemManagerService systemManagerService;
    public SystemManagerView() {
        super();
        try {
            var manager = service.manageSystemAsSystemManager();
            if (!manager.isOk()) {
                throw new IllegalStateException();
            }
            systemManagerService = manager.getElement();
        }
        catch (Exception e){
            UI.getCurrent().getPage().getHistory().go(-1);
            return;
        }
        createTabs();
        var name =service.getUserInfo();
        if(name.isOk())
            setName(name.getElement().username);
        registerToNotification(service);
    }

    private void createTabs() {
        addTabWithClickEvent("Shop Info", this::shopInfoEvent);
        addTabWithClickEvent("Manage Subscribed Users", this::subscribedUsersEvent);
        addTabWithClickEvent("View All Users Info", this::userInfoEvent);
    }

    private void userInfoEvent(DomEvent domEvent) {
        VerticalLayout layout = new VerticalLayout();
        ConcurrentHashMap<Tab, Component> tabMap = new ConcurrentHashMap<>();
        Tabs stateTabs = new Tabs();
        var res = systemManagerService.getAllSubscribedUserInfo();
        if(res.isOk()){
            List<SubscribedUserInfo> usersInfo = res.getElement();
            for(SubscribedUserInfo info : usersInfo){
                Tab tab = new Tab(info.state().name());
                stateTabs.add(tab);
                Grid<SubscribedUser> infoGrid = new Grid<>();
                infoGrid.addColumn(u -> u.username).setHeader("Username");
                infoGrid.setItems(info.subscribedUsers());
                tabMap.put(tab, infoGrid);
            }
            stateTabs.addSelectedChangeListener(e -> {
                layout.removeAll();
                layout.add(stateTabs);
                layout.add(tabMap.get(e.getSelectedTab()));
            });
        }
        layout.add(stateTabs);
        layout.add(tabMap.get(stateTabs.getSelectedTab()));
        setContent(layout);
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
        List<String> usernames = new ArrayList<>();
        if (resUsers.isOk()) {
            for(SubscribedUserInfo user : resUsers.getElement()){
                if(user.state() != UserState.REMOVED)
                    usernames.addAll(user.subscribedUsers().stream().map(u -> u.username).toList());
            }
            selectUser.setItems(usernames);
        }
    }

    private void shopInfoEvent(DomEvent domEvent) {
        PurchaseHistoryForm form = new PurchaseHistoryForm(systemManagerService);
        setContent(form);
    }
}
