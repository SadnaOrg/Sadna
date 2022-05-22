package com.example.application.views.main;

import ServiceLayer.UserServiceImp;
import ServiceLayer.interfaces.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinService;


import static com.example.application.Header.SessionData.save;
import static com.example.application.Header.SessionData.show;
import static com.example.application.Names.projectDescription;
import static com.example.application.Names.projectName;

@Route("Login")
public class LoginView extends LoginOverlay {

    private final UserService service;
    public LoginView() {
        service = (UserService)show("service");
        setTitle(projectName);
        setDescription(projectDescription);
        setOpened(true);
        addLoginListener(e -> {
            Notification notification = new Notification();
            UserService service = new UserServiceImp();
                if (service.login(e.getUsername(), e.getPassword()).isOk()) {
                    notification.setText("Login Succeeded");
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    UI.getCurrent().navigate(ProductView.class);
                }
                else {
                    notification.setText("Login Failed");
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            e.getSource().setEnabled(true);
            notification.setPosition(Notification.Position.BOTTOM_CENTER);
            notification.setOpened(true);
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