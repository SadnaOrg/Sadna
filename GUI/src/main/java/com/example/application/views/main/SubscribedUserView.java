package com.example.application.views.main;

import ServiceLayer.interfaces.UserService;
import com.example.application.Header.Header;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.router.Route;


import static com.example.application.Header.SessionData.Load;

@Route("SubscribedUser")
public class SubscribedUserView extends Header {

    private final UserService service;

    private Button browseProducts;
    private Button logoutButton;

    public SubscribedUserView() {
        service = (UserService)Load("service");
        logoutButton = new Button("Logout", e -> {
            String username = (String)Load("user-name");
            service.logoutSystem();
            UI.getCurrent().navigate(MainView.class);
        });
        browseProducts = new Button("Products", e -> UI.getCurrent().navigate(ProductView.class));
        logoutButton.getStyle().set("margin-left", "auto");
        addToNavbar(logoutButton);
    }
}
