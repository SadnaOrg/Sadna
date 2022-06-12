package com.example.application.Header;

import ServiceLayer.interfaces.UserService;
import com.example.application.views.main.MainView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.dom.DomEventListener;

import static com.example.application.Header.SessionData.Load;

public class Header extends AppLayout {
    protected VerticalLayout content = new VerticalLayout();
    protected Tabs tabs;

    protected Button title;
    public Header() {
        DrawerToggle toggle = new DrawerToggle();
        tabs = new Tabs();
        title = new Button("Superli");
        title.getStyle()
                .set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");
        addToNavbar(toggle, title);
        setContent(content);
    }

    protected void addTabWithClickEvent(String name, DomEventListener listener) {
        Tab tab = new Tab(name);
        tab.getElement().addEventListener("click", listener);
        tabs.add(tab);
    }

    protected void registerToNotification(){
        var service = (UserService)Load("service");
        service.registerToNotifier(not->{
            Dialog dialog = new Dialog();
            VerticalLayout notificationLayout = new VerticalLayout();
            H1 title = new H1("You receive a notification:");
            var content = new H2(not.Content());
            Button button = new Button("close", e1 -> dialog.close());
            title.setWidthFull();
            content.setWidthFull();
            notificationLayout.add(title,content, button);
            notificationLayout.setEnabled(true);
            dialog.add(notificationLayout);
            dialog.setOpened(true);
            dialog.setCloseOnEsc(false);
            dialog.setCloseOnOutsideClick(false);
            dialog.open();
            return true;
        });
        service.getDelayNotification();
    }
}