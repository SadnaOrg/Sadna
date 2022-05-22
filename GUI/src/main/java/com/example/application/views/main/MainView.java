package com.example.application.views.main;

import ServiceLayer.Result;
import ServiceLayer.UserServiceImp;
import ServiceLayer.interfaces.UserService;
import com.example.application.Header.Header;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import static com.example.application.Header.SessionData.save;

@PageTitle("Main")
@Route(value = "")
public class MainView extends Header {

    private final HorizontalLayout buttonLayout;
    private final HorizontalLayout guestLayout;
    private Button registerButton;
    private Button loginButton;
    private Button guestButton;

    public MainView() {
        save("service", service);
        content.setSizeFull();
        buttonLayout = new HorizontalLayout();
        guestLayout = new HorizontalLayout();
        createButtons();
        content.setFlexGrow(4, buttonLayout);
        content.setFlexGrow(1, guestLayout);
        buttonLayout.setWidthFull();
        guestLayout.setWidthFull();
        content.add(buttonLayout, guestLayout);
    }

    private void createButtons() {
        registerButton = new Button("Register", e -> UI.getCurrent().navigate(LoginView.class));
        registerButton.addThemeVariants(ButtonVariant.LUMO_LARGE);
        registerButton.setSizeFull();
        loginButton = new Button("Login", e -> UI.getCurrent().navigate(LoginView.class));
        loginButton.addThemeVariants(ButtonVariant.LUMO_LARGE);
        loginButton.setSizeFull();
        buttonLayout.add(registerButton, loginButton);
        guestButton = new Button("Continue As Guest", e -> {
            if (service.loginSystem().isOk())
                UI.getCurrent().navigate(ProductView.class);
        });
        loginButton.addThemeVariants(ButtonVariant.LUMO_LARGE);
        guestButton.setSizeFull();
        guestLayout.add(guestButton);
    }

}
