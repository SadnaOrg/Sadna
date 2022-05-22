package com.example.application.Header;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class Header extends VerticalLayout {
    private H1 header;
    public Header() {
        header = new H1("Superli");
        add(header);
    }
}