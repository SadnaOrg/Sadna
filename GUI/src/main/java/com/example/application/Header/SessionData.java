package com.example.application.Header;

import com.vaadin.flow.server.VaadinService;

//Remove comment to preserve UI value when reloading
//@PreserveOnRefresh
public class SessionData {

    public static Object Load(String sessionName) {
        return VaadinService.getCurrentRequest().getWrappedSession()
                .getAttribute(sessionName);
    }

    public static void save(String sessionName, Object data) {
        VaadinService.getCurrentRequest().getWrappedSession()
                .setAttribute(sessionName, data);
    }
}