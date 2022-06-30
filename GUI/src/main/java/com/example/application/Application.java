package com.example.application;

import ServiceLayer.Log;
import ServiceLayer.Response;
import com.example.application.Parser.Grammer;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import org.junit.jupiter.params.shadow.com.univocity.parsers.common.Format;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import javax.management.Notification;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 *
 */
@SpringBootApplication
@Theme(value = "mytodo")
@PWA(name = "My Todo", shortName = "My Todo", offlineResources = {})
@NpmPackage(value = "line-awesome", version = "1.3.0")
@Push
public class Application extends SpringBootServletInitializer implements AppShellConfigurator {
    public static void main(String[] args) {
        if (args.length >= 1 ) {
            try {
                var g = Grammer.parse(Files.readString(Path.of(args[0])));
                args = new String[0];
                var u = Grammer.runLines(g);
                if (u == null)
                    return;
            } catch (IOException e) {
                Log.getInstance().error("Error in configure file :\n \t\t"+e.getMessage());
                e.printStackTrace();
                return;
            }
        }
         SpringApplication.run(Application.class, args);
    }
}

