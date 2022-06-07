package com.example.application.views.main;

import ServiceLayer.Objects.*;
import ServiceLayer.interfaces.SystemManagerService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.dom.DomEvent;
import com.vaadin.flow.router.Route;

import java.util.List;

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
    }

    private void createTabs() {
        addTabWithClickEvent("Shop Info", this::shopInfoEvent);
        addTabWithClickEvent("Manage Subscribed Users", this::subscribedUsersEvent);
        addTabWithClickEvent("View All Users Info", this::userInfoEvent);
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

    private void shopInfoEvent(DomEvent domEvent) {
        PurchaseHistoryForm form = new PurchaseHistoryForm(systemManagerService);
        setContent(form);
    }
}
