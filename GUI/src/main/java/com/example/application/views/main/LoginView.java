package com.example.application.views.main;

import BusinessLayer.Users.SubscribedUser;
import ServiceLayer.Result;
import ServiceLayer.SubscribedUserServiceImp;
import ServiceLayer.interfaces.SubscribedUserService;
import ServiceLayer.interfaces.SystemManagerService;
import ServiceLayer.interfaces.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;


import static com.example.application.Header.SessionData.*;
import static com.example.application.Utility.*;

@Route("Login")
public class LoginView extends LoginOverlay {

    private final UserService service;
    private SubscribedUserService subscribedUserService;
    public LoginView() {
        service = (UserService)Load("service");
        setTitle(projectName);
        setDescription(projectDescription);
        setOpened(true);
        addLoginListener(e -> {
            var res = service.login(e.getUsername(), e.getPassword());
            if (res.isOk()) {
                notifySuccess("Login Succeeded!");
                subscribedUserService = res.getElement();
                var resManager = subscribedUserService.manageSystemAsSystemManager();
                if(resManager.isOk()){
                    SystemManagerService managerService = resManager.getElement();
                    save("system-manager-service", managerService);
                    save("user-name", e.getUsername());
                    save("subscribed-user-service",subscribedUserService);
                    UI.getCurrent().navigate(SystemManagerView.class);
                }
                else {
                    save("user-name", e.getUsername());
                    save("subscribed-user-service", subscribedUserService);
                    UI.getCurrent().navigate(SubscribedUserView.class);
                }
            }
            else {
                notifyError(res.getMsg());
                e.getSource().setEnabled(true);
            }
        });
        addForgotPasswordListener(e -> {
            Dialog dialog = new Dialog();
            VerticalLayout forgotLayout = new VerticalLayout();
            H1 remember = new H1("!תנסה להיזכר");
            Button button = new Button("!נזכרתי", e1 -> dialog.close());
            remember.setWidthFull();
            forgotLayout.add(remember, button);
            forgotLayout.setEnabled(true);
            dialog.add(forgotLayout);
            dialog.setOpened(true);
        });
    }
}