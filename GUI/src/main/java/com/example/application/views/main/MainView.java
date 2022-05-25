package com.example.application.views.main;

import ServiceLayer.Result;
import ServiceLayer.SubscribedUserServiceImp;
import ServiceLayer.UserServiceImp;
import ServiceLayer.interfaces.SubscribedUserService;
import ServiceLayer.interfaces.UserService;
import com.example.application.Header.Header;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import static com.example.application.Header.SessionData.save;

@PageTitle("Main")
@Route(value = "")
public class MainView extends Header {

    private final UserService service = new UserServiceImp();
    private final HorizontalLayout guestLayout;
    private final Button registerButton = new Button("Register", e -> UI.getCurrent().navigate(RegisterView.class));
    private final Button loginButton = new Button("Login", e -> UI.getCurrent().navigate(LoginView.class));
    private final Button guestButton = new Button("Continue As Guest", e -> {
        Result res = service.loginSystem();
        if (res.isOk()) {
            UI.getCurrent().navigate(GuestActionView.class);
        }
    });

    public MainView() {
        save("service", service);
        content.setSizeFull();
        guestLayout = new HorizontalLayout();
        createButtons();
        content.setFlexGrow(1, guestLayout);
        guestLayout.setWidthFull();
        content.add(guestLayout);
    }

    private void createButtons() {
        loginButton.addThemeVariants(ButtonVariant.LUMO_LARGE);
        guestButton.setSizeFull();
        guestLayout.add(guestButton);
    }

}
