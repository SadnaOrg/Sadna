package com.example.application.ServletContextListeners;

import ServiceLayer.SystemManagerServiceImp;
import ServiceLayer.UserServiceImp;
import ServiceLayer.interfaces.SubscribedUserService;
import ServiceLayer.interfaces.SystemManagerService;
import ServiceLayer.interfaces.UserService;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 *
 * @author Basil Bourque
 */
@Component
public class WebAppListener implements VaadinServiceInitListener {
    @Override
    public void serviceInit(ServiceInitEvent serviceInitEvent) {
        //add here any code that is needed to initialize (DATABASE OR SOMETHING YOU KNOW)
    }
}