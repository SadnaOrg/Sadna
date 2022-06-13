package com.example.application.views.main;

import BusinessLayer.Users.SubscribedUser;
import ServiceLayer.Objects.Notification;
import ServiceLayer.Result;
import ServiceLayer.SubscribedUserServiceImp;
import ServiceLayer.interfaces.SubscribedUserService;
import ServiceLayer.interfaces.UserService;
import com.example.application.Header.Header;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import static com.example.application.Header.SessionData.Load;
import static com.example.application.Header.SessionData.save;
import static com.example.application.Utility.*;

@Route("Register")
public class RegisterView extends Header {
    private UserService service;
   // private SubscribedUserService subscribedUserService;
    private final H1 registerLabel = new H1("Register");
    private final TextField userName = new TextField("Username: ");
    private final PasswordField password = new PasswordField("Password: ");
    private final PasswordField confirmPassword = new PasswordField("Confirm Password: ");
    private final DatePicker birthDate = new DatePicker("Pick your birthday");

    private final Button registerButton = new Button("Register", e -> {
        if (password.getValue().equals(confirmPassword.getValue()) && birthDate.getValue() != null) {
            Date date = Date.from(birthDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            var res = service.registerToSystem(userName.getValue(), password.getValue(), date);
            if (res.isOk()) {
                notifySuccess("Registration Succeeded!");
                UI.getCurrent().navigate(GuestActionView.class);
            }
            else {
                notifyError(res.getMsg());
            }
        }
        else{
            notifyError("The passwords you entered don't match!");
        }
    });

    private VerticalLayout layout = new VerticalLayout();

    public RegisterView() {

        service = (UserService)Load("service");
        createButtons();
        setLayout();
        layout.setSizeFull();
        content.add(layout);
        registerToNotification(service);
    }

    private void setLayout() {
        layout.add(registerLabel, userName, password, confirmPassword, birthDate, registerButton);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
    }

    private void createButtons() {
        userName.setPlaceholder("Name");
        password.setPlaceholder("Password");
        confirmPassword.setPlaceholder("Confirm Password");
        userName.setWidthFull();
        password.setWidthFull();
        confirmPassword.setWidthFull();
        userName.setClearButtonVisible(true);
        password.setClearButtonVisible(true);
        confirmPassword.setClearButtonVisible(true);
    }
}
