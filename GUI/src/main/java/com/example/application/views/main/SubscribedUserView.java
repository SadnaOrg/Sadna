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
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.dom.DomEvent;
import com.vaadin.flow.router.Route;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

import static com.example.application.Header.SessionData.Load;
import static com.example.application.Header.SessionData.save;
import static com.example.application.Utility.*;

@Route("SubscribedUser")
public class SubscribedUserView extends GuestActionView {
    protected SubscribedUserService service;

    private final Dialog dialog = new Dialog();
    private Grid<Shop> shops;

    private Grid<Product> shopProducts;
    private Editor<Product> editor;

    private Grid.Column<Product> productIDColumn;
    private Grid.Column<Product> nameColumn;
    private Grid.Column<Product> manufacturerColumn;
    private Grid.Column<Product> descriptionColumn;
    private Grid.Column<Product> quantityColumn;
    private Grid.Column<Product> priceColumn;
    private Grid.Column<Product> closeColumn;

    private int itemsSize = 0;


    public SubscribedUserView() {
        service = null;
        try {
            service = (SubscribedUserService) Load("service");
            if(service == null)
                throw new IllegalStateException();
        }
        catch(Exception e) {
            UI.getCurrent().getPage().getHistory().go(-1);
            return;
        }
        logoutButton.addClickListener(e -> {
            service.logout();
            save("user-name", null);
            UI.getCurrent().navigate(MainView.class);
        });
        createOpenShop();
        createTabs();
        registerToNotification();
    }

    private void createOpenShop() {
        content.removeAll();
        createDialogLayout();
        createOpenShopButton();
        createShopGrid();
        createProductGrid();
        shops.addItemClickListener(e -> itemClicked(e.getItem()));
        content.add(shops);
        setContent(content);
    }

    private void createOpenShopButton() {
        Button openShopMenu = new Button("Open Shop", e -> dialog.open());
        openShopMenu.setWidthFull();
        content.add(openShopMenu);
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
                    service.updateProductName(product.shopId(), product.productID(), name);
                    ID.set(product.shopId());
                });
        nameColumn.setEditorComponent(nameField);

        TextField descriptionField = new TextField("Description");
        descriptionField.setWidthFull();
        binder.forField(descriptionField)
                .bind(Product::description, (product, description) -> service.updateProductDescription(product.shopId(), product.productID(), description));
        descriptionColumn.setEditorComponent(descriptionField);

        NumberField quantityField = new NumberField("Quantity");
        quantityField.setMin(0);
        quantityField.setWidthFull();
        binder.forField(quantityField)
                .bind((product) -> (double) product.quantity(), (product, quantity) -> service.updateProductQuantity(product.shopId(), product.productID(), quantity.intValue()));
        quantityColumn.setEditorComponent(quantityField);

        NumberField priceField = new NumberField("Price");
        priceField.setMin(0);
        priceField.setWidthFull();
        binder.forField(priceField)
                .bind(Product::price, (product, price) -> service.updateProductPrice(product.shopId(), product.productID(), price));
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
            Result res = service.addProductToShop(item.shopId(), name.getValue(), description.getValue(), manufacturer.getValue(), itemsSize, quantity.getValue().intValue(), price.getValue());
            itemsSize += 1;
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
        Grid.Column<Product> shopIDColumn = shopProducts.addColumn(Product::shopId).setHeader("Shop ID").setSortable(true);
        productIDColumn = shopProducts.addColumn(Product::productID).setHeader("Product ID").setSortable(true);
        nameColumn = shopProducts.addColumn(Product::name).setHeader("Name").setSortable(true);
        manufacturerColumn = shopProducts.addColumn(Product::manufacturer).setHeader("Manufacturer").setSortable(true);
        descriptionColumn = shopProducts.addColumn(Product::description).setHeader("Description").setSortable(true);
        quantityColumn = shopProducts.addColumn(Product::quantity).setHeader("Quantity").setSortable(true);
        priceColumn = shopProducts.addColumn(Product::price).setHeader("price").setSortable(true);
        closeColumn = shopProducts.addComponentColumn(item -> new Button(VaadinIcon.CLOSE.create(), e -> {
            Result res = service.deleteProductFromShop(item.shopId(), item.productID());
            if (res.isOk()) {
                updateProductGrid(item.shopId());
                notifySuccess("Product Removed Successfully!");
            }
            else
                notifyError(res.getMsg());
            }));
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
        Collection<Product> products = getProducts(service.searchProducts(shopPredicate, productPredicate).getElement());
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
        Collection<Shop> shopItems = service.receiveInformation().getElement().shops();
        shops.setItems(shopItems);
    }

    private void createDialogLayout() {
        dialog.removeAll();
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
                if (service.openShop(shopName.getValue(), shopDescription.getValue()).isOk())
                    updateGrid();
                dialog.close();
            }
        });
        openShopButton.setWidthFull();
        dialogLayout.add(shopName, shopDescription, openShopButton);
        dialog.add(dialogLayout);
    }

    private void createTabs(){
        addTabWithClickEvent("Open Shop", e -> createOpenShop());
        addTabWithClickEvent("Manage Actions", this::createActionView);
        tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        addToDrawer(tabs);
    }

    private void createActionView(DomEvent domEvent) {
        ActionView view = new ActionView(service, currUser);
        setContent(view);
    }
}
