package com.example.application.views.main;

import ServiceLayer.Objects.Product;
import ServiceLayer.Objects.Shop;
import ServiceLayer.Result;
import ServiceLayer.interfaces.UserService;
import com.example.application.Header.Header;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import java.util.Collection;
import java.util.function.Predicate;

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
    private Dialog emptyProductSection;


    public ProductView(UserService service) {
        this.service = service;
        content.add(createFilterBy());
        productGrid = new Grid<>();
        productGrid.addColumn(Product::name).setHeader("Name").setSortable(true);
        productGrid.addColumn(Product::description).setHeader("Description").setSortable(true);
        productGrid.addColumn(Product::price).setHeader("Price").setSortable(true).setTextAlign(ColumnTextAlign.END);
        productGrid.addItemClickListener(e -> itemClicked(e.getItem()));
        createProductList();
        content.add(productGrid);
        registerToNotification(service);
    }

    private void createProductList() {
        Predicate<Shop> shopPredicate = shop -> true;
        Predicate<Product> prodPredicate = product -> true;
        Collection<Product> productList = getProducts(service.searchProducts(shopPredicate, prodPredicate).getElement());
        productGrid.setItems(productList);
        if (productList.size() == 0)
            createEmptyProductSectionDialog();
    }

    private void createEmptyProductSectionDialog() {
        emptyProductSection = new Dialog();
        emptyProductSection.add(new H1("There are currently no products available!"));
        emptyProductSection.setDraggable(true);
        emptyProductSection.open();
    }

    private HorizontalLayout createFilterBy() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setAlignItems(Alignment.BASELINE);
        textField.setPlaceholder("Name");
        filterBy.setItems("Name", "Description");
        filterBy.addValueChangeListener(e -> {
            if (e.getValue().equals("Name"))
                filterByName(textField.getValue());
            else
                filterByDescription(textField.getValue());
        });
        textField.addValueChangeListener(e -> {
            if (filterBy.getValue().equals("Name"))
                filterByName(textField.getValue());
            else
                filterByDescription(textField.getValue());
        });
        layout.add(textField, filterByLabel, filterBy);
        return layout;
    }

    private void filterByDescription(String desc) {
        Predicate<Shop> shopPredicate = shop -> shop.shopDescription().contains(desc);
        Predicate<Product> productPredicate = product -> true;
        Collection<Product> products = service.searchProducts(shopPredicate, productPredicate).getElement().shops().stream().map(Shop::shopProducts).flatMap(Collection::stream).toList();
        productGrid.setItems(products);
    }

    private void filterByName(String name) {
        Predicate<Shop> shopPredicate = shop -> shop.shopName().contains(name);
        Predicate<Product> productPredicate = product -> true;
        Collection<Product> products = service.searchProducts(shopPredicate, productPredicate).getElement().shops().stream().map(Shop::shopProducts).flatMap(Collection::stream).toList();
        productGrid.setItems(products);
    }

    private IntegerField createIntegerField(String label) {
        IntegerField basketField = new IntegerField(label);
        basketField.setWidthFull();
        basketField.setClearButtonVisible(true);
        basketField.setMin(0);
        basketField.setValue(1);
        basketField.setHasControls(true);
        return basketField;
    }
    private NumberField createNumberField(String label, int maxValue) {
        NumberField amount = new NumberField(label);
        amount.setAutofocus(true);
        amount.setWidthFull();
        amount.setClearButtonVisible(true);
        amount.setMin(0);
        if(maxValue != -1)
            amount.setMax(maxValue);
        return amount;
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
        HorizontalLayout row5 = new HorizontalLayout();
        row4.setJustifyContentMode(JustifyContentMode.CENTER);
        row4.setAlignItems(Alignment.BASELINE);
        addToCartButton = new Button("Add To Cart", e -> addToCart(p, amount.getValue()));
        var addBidButton = new Button("Add Bid", e -> {
            var diag = new Dialog();
            var integerField = createNumberField("enter total bid price",-1);
            var add = new Button("add bid",(event)->{
                if(integerField.getValue()!=null&&amount.getValue()!=null)
                {
                     var res = service.saveProductsAsBid(p.shopId(),p.productID(), amount.getValue(),integerField.getValue());
                     if(res.isOk()) {
                         notifySuccess("add bid to basket, wait to approve");
                        diag.close();
                     }else
                         notifyError(res.getMsg());
                }else
                    notifyError("please enter total bid price");
            });
            diag.add(integerField,add);
            diag.open();
        });

        addToCartButton.setWidthFull();
        setAmountField(p);
        row4.add(amount, addToCartButton);
        productInfo.add(row1, row2, row3, row4,addBidButton);
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
