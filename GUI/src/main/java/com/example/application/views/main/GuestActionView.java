package com.example.application.views.main;

import ServiceLayer.Objects.*;
import ServiceLayer.interfaces.UserService;
import com.example.application.Header.Header;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.dom.DomEvent;
import com.vaadin.flow.router.Route;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.example.application.Header.SessionData.Load;
import static com.example.application.Header.SessionData.save;


@Route("Guest")
public class GuestActionView extends Header {
    protected UserService service;
    protected String currUser;
    protected Button logoutButton = new Button("Logout");

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
            logoutButton.addClickListener(e -> {
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
        addTabWithClickEvent("Check Cart", this::checkCartEvent);
        addTabWithClickEvent("Search Product by Shop", this::searchProductsEvent);
        addTabWithClickEvent("Buy Cart", this::buyCartEvent);
        addTabWithClickEvent("Add Products to Cart", this::productEvent);
        tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        return tabs;
    }

    private void productEvent(DomEvent domEvent) {
        ProductView productView = new ProductView();
        setContent(productView);
    }

    private void buyCartEvent(DomEvent event) {
        var res = service.showCart();
        if(res.isOk()) {
            Collection<ProductInfo> basketProductsIDs = res.getElement().baskets().stream().map(Basket::productsID).flatMap(Collection::stream).toList();
            if (basketProductsIDs.size() > 0) {
                PaymentForm layout = new PaymentForm();
                setContent(layout);
                return;
            }
        }
        setContent(new Label("No cart to buy"));
    }

    private void searchProductsEvent(DomEvent event) {
        Predicate<Shop> shopFilter = shop -> true;
        Predicate<Product> productFilter = product -> true;
        Grid<Shop> grid = createShopGrid();
        Grid<ProductInfo> prodGrid = createProductInfoGrid();
        VerticalLayout totalLayout = new VerticalLayout();
        FormLayout textFieldLayout = createTextFieldSearchLayout(grid, prodGrid);
        HorizontalLayout horizontalLayout = createGridLayout(shopFilter, productFilter, grid, prodGrid);
        horizontalLayout.setWidthFull();
        totalLayout.add(textFieldLayout);
        totalLayout.add(horizontalLayout);
        setContent(totalLayout);
    }

    private HorizontalLayout createGridLayout(Predicate<Shop> shopFilter, Predicate<Product> productFilter, Grid<Shop> grid, Grid<ProductInfo> prodGrid) {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        ShopsInfo shop = service.searchProducts(shopFilter, productFilter).getElement();
        if (shop != null) {
            Collection<Shop> shops = shop.shops();
            grid.setItems(shops);
            grid.addItemClickListener(item -> {
                Shop curr = shops.stream()
                        .filter(prod -> item.getItem().equals(prod))
                        .findAny()
                        .orElse(null);
                if(curr != null){
                    Collection<Product> products = curr.shopProducts();
                    Stream<ProductInfo> newProducts = products.stream().map(product -> new ProductInfo(product.shopId(), product.productID(), product.quantity(), product.price()));
                    prodGrid.setItems(newProducts.toList());
                }
            });
            horizontalLayout.add(grid, prodGrid);
        } else {
            Label label = new Label("No Cart");
            horizontalLayout.add(label);
        }
        return horizontalLayout;
    }

    private FormLayout createTextFieldSearchLayout(Grid<Shop> grid, Grid<ProductInfo> prodGrid) {
        TextField shopText = new TextField("Filter by Shop");
        TextField productText = new TextField("Filter by Product");
        FormLayout formLayout = new FormLayout();
        formLayout.add(shopText, productText);
        shopText.setValueChangeMode(ValueChangeMode.LAZY);
        productText.setValueChangeMode(ValueChangeMode.LAZY);
        shopText.addValueChangeListener(e -> {
            Predicate<Shop> shopFilter = shop -> shop.shopDescription().contains(e.getValue()) ||
                    shop.shopName().contains(e.getValue()) || String.valueOf(shop.shopId()).contains(e.getValue());
            Predicate<Product> productFilter = product -> String.valueOf(product.productID()).contains(productText.getValue()) ||
                    String.valueOf(product.quantity()).contains(productText.getValue()) || String.valueOf(product.price()).contains(productText.getValue()) ||
                    product.description().contains(productText.getValue());
            ShopsInfo shop = service.searchProducts(shopFilter, productFilter).getElement();
            updateList(grid, prodGrid, shop);
        });
        productText.addValueChangeListener(e -> {
            Predicate<Shop> shopFilter = shop -> shop.shopDescription().contains(shopText.getValue()) ||
                    shop.shopName().contains(shopText.getValue()) || String.valueOf(shop.shopId()).contains(shopText.getValue());
            Predicate<Product> productFilter = product -> String.valueOf(product.productID()).contains(e.getValue()) ||
                    String.valueOf(product.quantity()).contains(e.getValue()) || String.valueOf(product.price()).contains(e.getValue()) ||
                    product.description().contains(e.getValue());
            ShopsInfo shop = service.searchProducts(shopFilter, productFilter).getElement();
            updateList(grid, prodGrid, shop);
        });
        return formLayout;
    }

    private void updateList(Grid<Shop> grid, Grid<ProductInfo> prodGrid, ShopsInfo shop) {
        if (shop != null) {
            grid.setItems(shop.shops());
            prodGrid.setItems();
        }
    }

    private Grid<Shop> createShopGrid(){
        Grid<Shop> grid = new Grid<>(Shop.class, false);
        grid.addColumn(Shop::shopName).setHeader("Shop Name");
        grid.addColumn(Shop::shopId).setHeader("Shop Id");
        grid.addColumn(Shop::shopDescription).setHeader("Shop Id");
        grid.setWidthFull();
        return grid;
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
        prodGrid.setWidthFull();
        return prodGrid;
    }
}