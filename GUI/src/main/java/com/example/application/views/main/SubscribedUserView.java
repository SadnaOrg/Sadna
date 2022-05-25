package com.example.application.views.main;

import BusinessLayer.Shops.ShopInfo;
import ServiceLayer.Objects.Shop;
import ServiceLayer.Objects.ShopsInfo;
import ServiceLayer.SubscribedUserServiceImp;
import ServiceLayer.interfaces.SubscribedUserService;
import ServiceLayer.interfaces.UserService;
import com.example.application.Header.Header;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import java.util.Collection;

import static com.example.application.Header.SessionData.Load;
import static com.example.application.Header.SessionData.save;

@Route("SubscribedUser")
public class SubscribedUserView extends Header {

    private final SubscribedUserService subscribedUserService;
    private VerticalLayout openShopLayout = new VerticalLayout();
    private final Dialog dialog = new Dialog();
    private Button openShop;
    private Button logoutButton;
    private Grid<Shop> shops;


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
        content.add(openShopLayout, shops);
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
