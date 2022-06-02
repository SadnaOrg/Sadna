package com.example.application.views.main;

import ServiceLayer.Objects.Product;
import ServiceLayer.Objects.Shop;
import ServiceLayer.Result;
import ServiceLayer.interfaces.SubscribedUserService;
import com.example.application.Header.Header;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
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
    private Editor<Product> editor;

    private Grid.Column<Product> shopIDColumn;
    private Grid.Column<Product> productIDColumn;
    private Grid.Column<Product> nameColumn;
    private Grid.Column<Product> manufacturerColumn;
    private Grid.Column<Product> descriptionColumn;
    private Grid.Column<Product> quantityColumn;
    private Grid.Column<Product> priceColumn;
    private Grid.Column<Product> closeColumn;

    private int itemsSize;


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

    private void setEditorComponent() {
        AtomicInteger ID = new AtomicInteger();
        Binder<Product> binder = new Binder<>(Product.class);
        editor.setBinder(binder);
        editor.setBuffered(true);

        TextField nameField = new TextField("Name");
        nameField.setWidthFull();
        binder.forField(nameField)
                .bind(Product::name, (product, name) -> {
                    subscribedUserService.updateProductName(product.shopId(), product.productID(), name);
                    ID.set(product.shopId());
                });
        nameColumn.setEditorComponent(nameField);

        TextField descriptionField = new TextField("Description");
        descriptionField.setWidthFull();
        binder.forField(descriptionField)
                .bind(Product::description, (product, description) -> subscribedUserService.updateProductDescription(product.shopId(), product.productID(), description));
        descriptionColumn.setEditorComponent(descriptionField);

        NumberField quantityField = new NumberField("Quantity");
        quantityField.setMin(0);
        quantityField.setWidthFull();
        binder.forField(quantityField)
                .bind((product) -> (double) product.quantity(), (product, quantity) -> subscribedUserService.updateProductQuantity(product.shopId(), product.productID(), quantity.intValue()));
        quantityColumn.setEditorComponent(quantityField);

        NumberField priceField = new NumberField("Price");
        priceField.setMin(0);
        priceField.setWidthFull();
        binder.forField(priceField)
                .bind(Product::price, (product, price) -> subscribedUserService.updateProductPrice(product.shopId(), product.productID(), price));
        priceColumn.setEditorComponent(priceField);

        Button saveButton = new Button(VaadinIcon.CHECK.create(), e -> {
            editor.save();
            updateProductGrid(ID.get());
        });
        HorizontalLayout actions = new HorizontalLayout(saveButton);
        closeColumn.setEditorComponent(actions);
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
            Result res = subscribedUserService.addProductToShop(item.shopId(), name.getValue(), description.getValue(), manufacturer.getValue(), itemsSize, quantity.getValue().intValue(), price.getValue());
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
        shopIDColumn = shopProducts.addColumn(Product::shopId).setHeader("Shop ID").setSortable(true);
        productIDColumn = shopProducts.addColumn(Product::productID).setHeader("Product ID").setSortable(true);
        nameColumn = shopProducts.addColumn(Product::name).setHeader("Name").setSortable(true);
        manufacturerColumn = shopProducts.addColumn(Product::manufacturer).setHeader("Manufacturer").setSortable(true);
        descriptionColumn = shopProducts.addColumn(Product::description).setHeader("Description").setSortable(true);
        quantityColumn = shopProducts.addColumn(Product::quantity).setHeader("Quantity").setSortable(true);
        priceColumn = shopProducts.addColumn(Product::price).setHeader("price").setSortable(true);
        closeColumn = shopProducts.addComponentColumn(item -> {
            Button delete = new Button(VaadinIcon.CLOSE.create(), e -> {
                Result res = subscribedUserService.deleteProductFromShop(item.shopId(), item.productID());
                if (res.isOk()) {
                    updateProductGrid(item.shopId());
                    notifySuccess("Product Removed Successfully!");
                }
                else
                    notifyError(res.getMsg());
                });
            return delete;
        });
        editor = shopProducts.getEditor();
        setEditorComponent();
        shopProducts.addItemClickListener(item -> {
            if (editor.isOpen())
                editor.cancel();
            shopProducts.getEditor().editItem(item.getItem());
        });
    }

    private void updateProductGrid(int shopID) {
        Predicate<Shop> shopPredicate = shop -> shop.shopId() == shopID;
        Predicate<Product> productPredicate = product -> true;
        Collection<Product> products = getProducts(subscribedUserService.searchProducts(shopPredicate, productPredicate).getElement());
        itemsSize = products.size();
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
