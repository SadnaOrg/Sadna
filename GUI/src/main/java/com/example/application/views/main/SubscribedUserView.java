package com.example.application.views.main;

import BusinessLayer.Shops.ShopInfo;
import ServiceLayer.Objects.Product;
import ServiceLayer.Objects.Shop;
import ServiceLayer.Objects.ShopsInfo;
import ServiceLayer.Result;
import ServiceLayer.SubscribedUserServiceImp;
import ServiceLayer.interfaces.SubscribedUserService;
import ServiceLayer.interfaces.UserService;
import com.example.application.Header.Header;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import java.util.Collection;
import java.util.function.Predicate;

import static com.example.application.Header.SessionData.Load;
import static com.example.application.Header.SessionData.save;
import static com.example.application.Utility.*;

@Route("SubscribedUser")
public class SubscribedUserView extends Header {

    private final SubscribedUserService subscribedUserService;
    private VerticalLayout openShopLayout = new VerticalLayout();
    private final Dialog dialog = new Dialog();
    private Button logoutButton;
    private Grid<Shop> shops;

    private Grid<Product> shopProducts;


    public SubscribedUserView() {
        subscribedUserService = (SubscribedUserService)Load("service");
        logoutButton = new Button("Logout", e -> {
            var service = subscribedUserService.logout();

            if (service.isOk()) {
                save("user-name", null);
                save("service",service.getElement());
                UI.getCurrent().navigate(MainView.class);
            }
        });
        logoutButton.getStyle().set("margin-left", "auto");
        addToNavbar(logoutButton);
        createDialogLayout();
        Button openShopMenu = new Button("Open Shop", e -> dialog.open());
        openShopMenu.setWidthFull();
        openShopLayout.add(openShopMenu);
        createShopGrid();
        createProductGrid();
        shops.addItemClickListener(e -> itemClicked(e.getItem()));
        content.add(openShopLayout, shops);
    }

    private void itemClicked(Shop item) {
        Dialog addProductDialog = new Dialog();
        VerticalLayout addProductLayout = new VerticalLayout();
        HorizontalLayout addLayout = new HorizontalLayout();
        TextField name = new TextField("Name");
        TextField description = new TextField("Description");
        TextField manufacturer = new TextField("Manufacturer");
        NumberField quantity = new NumberField("Quantity");
        NumberField price = new NumberField("Price");
        Button add = new Button("Add", e -> {
            Result res = subscribedUserService.addProductToShop(item.shopId(), name.getValue(), description.getValue(), manufacturer.getValue(), shopProducts.getPageSize(), quantity.getValue().intValue(), price.getValue());
            if (res.isOk()) {
                updateProductGrid(item.shopId());
                notifySuccess("Product Successfully Added!");
            }
            else
                notifyError(res.getMsg());
        });
        addLayout.add(name, description, manufacturer, quantity, price, add);
        addProductLayout.add(addLayout, shopProducts);
        addProductDialog.add(addProductLayout);
        updateProductGrid(item.shopId());
        addProductLayout.add(shopProducts);
        addProductDialog.open();
    }

    private void createProductGrid() {
        shopProducts = new Grid<>();
        shopProducts.addColumn(Product::shopId).setHeader("Shop ID").setSortable(true);
        shopProducts.addColumn(Product::productID).setHeader("Product ID").setSortable(true);
        shopProducts.addColumn(Product::name).setHeader("Name").setSortable(true);
        shopProducts.addColumn(Product::manufacturer).setHeader("Manufacturer").setSortable(true);
        shopProducts.addColumn(Product::description).setHeader("Description").setSortable(true);
        shopProducts.addColumn(Product::quantity).setHeader("Quantity").setSortable(true);
        shopProducts.addColumn(Product::price).setHeader("price").setSortable(true);
        shopProducts.addColumn(item -> new Button("Delete", e -> {
            Result res = subscribedUserService.removeProduct(item.shopId(), item.productID());
            if (res.isOk()) {
                updateProductGrid(item.shopId());
                notifySuccess("Product Removed Successfully!");
            }
            else
                notifyError(res.getMsg());
        }));
    }

    private void updateProductGrid(int shopID) {
        Predicate<Shop> shopPredicate = shop -> shop.shopId() == shopID;
        Predicate<Product> productPredicate = product -> true;
        Collection<Product> products = getProducts(subscribedUserService.searchProducts(shopPredicate, productPredicate).getElement());
        shopProducts.setItems(products);
    }

    private void createShopGrid() {
        shops = new Grid<>();
        shops.addColumn(Shop::shopId).setHeader("ID").setSortable(true);
        shops.addColumn(Shop::shopName).setHeader("Name").setSortable(true);
        shops.addColumn(Shop::shopDescription).setHeader("Description").setSortable(true);
        updateGrid();
    }

    private void updateGrid() {
        Collection<Shop> shopItems = subscribedUserService.receiveInformation().getElement().shops();
        shops.setItems(shopItems);
    }

    private void createDialogLayout() {
        dialog.setCloseOnEsc(true);
        dialog.setDraggable(true);
        dialog.setSizeFull();
        VerticalLayout dialogLayout = new VerticalLayout();
        TextField shopName = new TextField("Shop Name");
        shopName.setWidthFull();
        TextField shopDescription = new TextField("Shop Description");
        shopDescription.setWidthFull();
        dialogLayout.setFlexGrow(1, shopName);
        dialogLayout.setFlexGrow(4, shopDescription);
        Button openShopButton = new Button("Open Shop", e -> {
            if (!shopName.isInvalid() && !shopDescription.isInvalid()) {
                if (subscribedUserService.openShop(shopName.getValue(), shopDescription.getValue()).isOk())
                    updateGrid();
                dialog.close();
            }
        });
        openShopButton.setWidthFull();
        dialogLayout.add(shopName, shopDescription, openShopButton);
        dialog.add(dialogLayout);
    }

}
