package com.example.application.Header;

import com.example.application.views.main.MainView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class Header extends AppLayout {
    protected VerticalLayout content = new VerticalLayout();

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