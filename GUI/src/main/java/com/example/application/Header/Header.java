package com.example.application.Header;

import com.example.application.views.main.MainView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class Header extends AppLayout {
    protected VerticalLayout content = new VerticalLayout();

    protected Button title;
    public Header() {
        DrawerToggle toggle = new DrawerToggle();
        title = new Button("Superli");
        title.getStyle()
                .set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");
        addToNavbar(toggle, title);
        setContent(content);
    }
}