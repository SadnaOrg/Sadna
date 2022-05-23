package com.example.application.views.main;

import ServiceLayer.Objects.Product;
import ServiceLayer.Result;
import ServiceLayer.interfaces.UserService;
import com.example.application.Header.Header;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import static com.example.application.Header.SessionData.Load;
import static com.example.application.Utility.*;

@Route("Products")
public class ProductView extends Header {

    private final UserService service;
    private Grid<Product> productGrid;
    private final IntegerField amount = new IntegerField("Quantity");
    private Button addToCartButton;
    private final TextField textField = new TextField("Name");
    private final H5 filterByLabel = new H5("filter by:");
    private final Select<String> filterBy = new Select<>();


    public ProductView() {
        service = (UserService)Load("service");
        content.add(createFilterBy());
        productGrid = new Grid<>();
        productGrid.addColumn(Product::name).setHeader("Name").setSortable(true);
        productGrid.addColumn(Product::description).setHeader("Description").setSortable(true);
        productGrid.addColumn(Product::price).setHeader("Price").setSortable(true).setTextAlign(ColumnTextAlign.END);
        productGrid.addItemClickListener(e -> itemClicked(e.getItem()));
        Product[] productList = new Product[]{new Product(1, 1, "P1", "", 5.0, 10), new Product(2, 2, "P2", "", 5.0, 10)};
        productGrid.setItems(productList);
        content.add(productGrid);
    }

    private HorizontalLayout createFilterBy() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setAlignItems(Alignment.BASELINE);
        textField.setPlaceholder("Name");
        filterBy.setItems("Name", "Description");
        filterBy.addValueChangeListener(e -> {
            if (e.getValue().equals("Name"))
                Notification.show("Name");
            else
                Notification.show("Description");
        });
        layout.add(textField, filterByLabel, filterBy);
        return layout;
    }

    private void addProductDetails(Product p) {
        Dialog dialog = new Dialog();
        VerticalLayout productInfo = new VerticalLayout();
        HorizontalLayout row1 = new HorizontalLayout();
        row1.add("Shop ID: " + p.shopId());
        HorizontalLayout row2 = new HorizontalLayout();
        row2.add("ProductID: " + p.productID());
        HorizontalLayout row3 = new HorizontalLayout();
        row3.add("Quantity: " + p.quantity());
        HorizontalLayout row4 = new HorizontalLayout();
        row4.setJustifyContentMode(JustifyContentMode.CENTER);
        row4.setAlignItems(Alignment.BASELINE);
        addToCartButton = new Button("Add To Cart", e -> addToCart(p, amount.getValue()));
        addToCartButton.setWidthFull();
        setAmountField(p);
        row4.add(amount, addToCartButton);
        productInfo.add(row1, row2, row3, row4);
        dialog.add(productInfo);
        dialog.setOpened(true);
    }

    private void setAmountField(Product p) {
        amount.setHelperText("Select a number between [1-" + p.quantity() + "]");
        amount.setAutofocus(true);
        amount.setWidthFull();
        amount.setClearButtonVisible(true);
        amount.setMin(1);
        amount.setMax(p.quantity());
        amount.setValue(1);
        amount.setHasControls(true);
    }

    private void addToCart(Product p, int quantity) {
        if (amount.isInvalid()) {
            notifyError("Invalid Quantity!");
        }
        else {
            Result res = service.saveProducts(p.shopId(), p.productID(), quantity);
            notifyIsOk(res, "Adding to cart succeeded!");
        }
    }

    private void itemClicked(Product p) {
        addProductDetails(p);
        productGrid.setDetailsVisible(p,
                !productGrid.isDetailsVisible(p));
    }
}
