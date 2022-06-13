package com.example.application.views.main;

import ServiceLayer.Objects.*;
import ServiceLayer.interfaces.UserService;
import com.example.application.Header.Header;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
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
import static com.example.application.Utility.notifyError;
import static com.example.application.Utility.notifySuccess;


@Route("Guest")
public class GuestActionView extends Header {
    protected UserService service;
    protected String currUser;
    protected Button logoutButton = new Button("Logout");

    public GuestActionView() {
        super();
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
        var name =service.getUserInfo();
        if(name.isOk())
            setName(name.getElement().username);
        registerToNotification(service);
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
                PaymentForm layout = new PaymentForm(service);
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

    private Grid<Basket> createCartGrid(){
        var grid = new Grid<>(Basket.class, false);
        grid.addColumn(Basket::shopId).setHeader("Shop ID");
        grid.addColumn(b->b.productsID().size()).setHeader("Product count");
        return grid;
    }

    private void checkCartEvent(DomEvent event) {
        Cart cart;
        var cartres = service.showCart();
        var layout = new VerticalLayout();
        layout.setWidthFull();
        var title = new H1("Your cart:");
        if (cartres.isOk()) {
            cart = cartres.getElement();

            Accordion accordion = new Accordion();
            accordion.setWidthFull();
            for (var b : cart.baskets()){
                var p = createProductInfoGrid();
                p.setItems(b.productsID());
                var v = new VerticalLayout(p, new Label("bascket price :"+service.getBasketPrice(b.shopId()).getElement()));
                AccordionPanel shopPannel = accordion.add("shop id : "+b.shopId(),v);
                shopPannel.addThemeVariants(DetailsVariant.FILLED);
                p.setWidthFull();
            }
            var totalPrice = new Label("total :"+service.getCartPrice().getElement());
            layout.add(title,accordion,totalPrice);

        } else {
            notifyError(cartres.getMsg());
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
        prodGrid.addComponentColumn(this::removeProductFromCart).setHeader("");
        prodGrid.setWidthFull();
        return prodGrid;
    }
    private Button removeProductFromCart(ProductInfo p){
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("Delete \"Report Q4\"?");
        dialog.setText("Are you sure you want to permanently delete this item?");

        dialog.setCancelable(true);
        dialog.addCancelListener(event -> dialog.close());

        dialog.setConfirmText("Delete");
        dialog.setConfirmButtonTheme("error primary");
        dialog.addConfirmListener(event -> {
            var res = service.removeProduct(p.shopId(),p.Id());
            if(res.isOk())
                notifySuccess("product remove successfully");
            else notifyError(res.getMsg());
            dialog.close();
            UI.getCurrent().getPage().reload();
        });

        var b = new Button(("remove"));
        b.addClickListener(e->{
            dialog.open();
        });
        return  b;
    }
}