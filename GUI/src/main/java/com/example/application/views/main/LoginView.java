package com.example.application.views.main;

import ServiceLayer.UserServiceImp;
import ServiceLayer.interfaces.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;


import static com.example.application.Names.projectDescription;
import static com.example.application.Names.projectName;

@Route("Login")
public class LoginView extends LoginOverlay {
    public LoginView() {
        setTitle(projectName);
        setDescription(projectDescription);
        setOpened(true);
        addLoginListener(e -> {
            Notification notification = new Notification();
            UserService service = new UserServiceImp();
                if ("abc".equals(e.getUsername()) && "abc".equals(e.getPassword())) {
                    notification.setText("Login Succeeded");
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                }
                else {
                    notification.setText("Login Failed");
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            e.getSource().setEnabled(true);
            notification.setPosition(Notification.Position.BOTTOM_CENTER);
                notification.setDuration(5);
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