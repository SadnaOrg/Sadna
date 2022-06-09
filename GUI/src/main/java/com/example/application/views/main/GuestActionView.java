package com.example.application.views.main;

import BusinessLayer.Products.ProductFilters;
import BusinessLayer.Shops.ShopFilters;
import ServiceLayer.Objects.*;
import ServiceLayer.UserServiceImp;
import ServiceLayer.interfaces.UserService;
import com.example.application.Header.Header;
import com.helger.commons.annotation.Nonempty;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.dom.DomEvent;
import com.vaadin.flow.dom.DomEventListener;
import com.vaadin.flow.router.Route;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.example.application.Header.SessionData.Load;
import static com.example.application.Header.SessionData.save;


@Route("Guest")
public class GuestActionView extends Header {
    UserService service;
    String currUser;

    public GuestActionView() {
        currUser = (String)Load("user-name");
        service = (UserService)Load("service");
        service.loginSystem();
        tabs = getTabs();
        addToDrawer(tabs);
        if (currUser == null) {
            Button registerButton = new Button("Register", event -> UI.getCurrent().navigate(RegisterView.class));
            registerButton.getStyle().set("margin-left", "auto");
            Button loginButton = new Button("Login", event -> UI.getCurrent().navigate(LoginView.class));
            addToNavbar(registerButton, loginButton);
        }
        else {
            Button logoutButton = new Button("Logout", e -> {
                service.logoutSystem();
                save("user-name", null);
                UI.getCurrent().navigate(MainView.class);
            });
            logoutButton.getStyle().set("margin-left", "auto");
            addToNavbar(logoutButton);
        }
        registerToNotification();
    }

    private Tabs getTabs() {
        tabs = new Tabs();
//        addTabWithClickEvent("Login", event -> UI.getCurrent().navigate(LoginView.class));
//        addTabWithClickEvent("Register", event -> UI.getCurrent().navigate(RegisterView.class));
        addTabWithClickEvent("Check Cart", this::checkCartEvent);
        addTabWithClickEvent("Search Products", this::searchProductsEvent);
        Collection<ProductInfo> basketProductsIDs = service.showCart().getElement().baskets().stream().map(Basket::productsID).flatMap(Collection::stream).toList();
        if (basketProductsIDs.size() > 0) {
            addTabWithClickEvent("Buy Cart", this::buyCartEvent);
        }
        addTabWithClickEvent("Products", this::productEvent);
        //addTab("Info on Shops and Products");
        addTabWithClickEvent("Exit", event -> {
            if(service.logoutSystem().isOk()) {
                save("user-name", null);
                UI.getCurrent().navigate(MainView.class);
            }
        });
        tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        return tabs;
    }

    private void productEvent(DomEvent domEvent) {
        ProductView productView = new ProductView();
        setContent(productView);
    }

    private void buyCartEvent(DomEvent event) {
        PaymentForm layout = new PaymentForm();
        setContent(layout);
    }

    private void searchProductsEvent(DomEvent event) {
        HorizontalLayout layout = new HorizontalLayout();
        Predicate<Shop> shopFilter = shop -> true;
        Predicate<Product> productFilter = product -> true;

        ShopsInfo shop = service.searchProducts(shopFilter, productFilter).getElement();
        if (shop != null) {
            Grid<Shop> grid = createShopGrid();
            Grid<ProductInfo> prodGrid = createProductInfoGrid();
            Collection<Shop> shops = shop.shops();
            grid.addItemClickListener(item -> {
                Shop curr = shops.stream()
                        .filter(prod -> item.getItem().equals(prod))
                        .findAny()
                        .orElse(null);
                if(curr != null){
                    Collection<ServiceLayer.Objects.Product> products = curr.shopProducts();
                    Stream<ProductInfo> newProducts = products.stream().map(product -> new ProductInfo(product.shopId(), product.productID(), product.quantity(), product.price()));
                    prodGrid.setItems(newProducts.toList());
                    layout.replace(layout.getComponentAt(1), prodGrid);
                }
            });
            grid.setItems(shops);
            layout.add(grid);
            layout.add(prodGrid);
        } else {
            Label label = new Label("No Cart");
            layout.add(label);
        }
        setContent(layout);
    }

    private void checkCartEvent(DomEvent event) {
        HorizontalLayout layout = new HorizontalLayout();
        Cart cart = service.showCart().getElement();
        if (cart != null) {
            Collection<Basket> basket = cart.baskets();
            Grid<Basket> grid = new Grid<>(Basket.class, false);
            grid.addColumn(Basket::shopId).setHeader("Shops");
            Grid<ProductInfo> prodGrid = createProductInfoGrid();
            grid.addItemClickListener(item -> {
                Basket curr = basket.stream()
                    .filter(prod -> item.getItem().equals(prod))
                    .findAny()
                    .orElse(null);
                if (curr != null) {
                    Collection<ProductInfo> products = curr.productsID();
                    prodGrid.setItems(products);
                    layout.replace(layout.getComponentAt(1), prodGrid);
                }
            });
            grid.setItems(basket);
            layout.add(grid);
            layout.add(prodGrid);
        } else {
            Label label = new Label("No Cart");
            layout.add(label);
        }
        setContent(layout);
    }

    private Grid<ProductInfo> createProductInfoGrid(){
        Grid<ProductInfo> prodGrid = new Grid<>(ProductInfo.class, false);
        prodGrid.addColumn(ProductInfo::Id).setHeader("Product ID");
        prodGrid.addColumn(ProductInfo::price).setHeader("Price");
        prodGrid.addColumn(ProductInfo::quantity).setHeader("Quantity");
        prodGrid.addColumn(ProductInfo::shopId).setHeader("Shop ID");
        return prodGrid;
    }

    private Grid<Shop> createShopGrid(){
        Grid<Shop> grid = new Grid<>(Shop.class, false);
        grid.addColumn(Shop::shopName).setHeader("Shop Name");
        return grid;
    }
}