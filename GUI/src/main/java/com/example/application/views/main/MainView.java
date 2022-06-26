package com.example.application.views.main;

import BusinessLayer.Users.SystemManager;
import ServiceLayer.Result;
import ServiceLayer.SubscribedUserServiceImp;
import ServiceLayer.SystemManagerServiceImp;
import ServiceLayer.UserServiceImp;
import ServiceLayer.interfaces.SubscribedUserService;
import ServiceLayer.interfaces.SystemManagerService;
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
    private final Button guestButton = new Button("Continue As Guest", e -> {
        Result res = service.loginSystem();
        if (res.isOk() || (service.logoutSystem().isOk()&&service.loginSystem().isOk())) {
            UI.getCurrent().navigate(GuestActionView.class);
        }
    });

    public MainView() {
        //initialize server!!!
        save("service", service);
        content.setSizeFull();
        guestLayout = new HorizontalLayout();
        createButtons();
        content.setFlexGrow(1, guestLayout);
        guestLayout.setWidthFull();
        content.add(guestLayout);
        this.registerToNotification(service);
    }

    private void createButtons() {
        guestButton.setSizeFull();
        guestLayout.add(guestButton);
    }

}
