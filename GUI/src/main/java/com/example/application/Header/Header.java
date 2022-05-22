package com.example.application.Header;

import ServiceLayer.UserServiceImp;
import ServiceLayer.interfaces.UserService;
import com.example.application.views.main.MainView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import static com.example.application.Names.projectName;

public class Header extends AppLayout {
    protected VerticalLayout content = new VerticalLayout();
    protected UserService service;

    public Header() {
        DrawerToggle toggle = new DrawerToggle();
        Button title = new Button("Superli", e -> UI.getCurrent().navigate(MainView.class));
        title.getStyle()
                .set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");
        addToNavbar(toggle, title);
        setContent(content);
    }
}