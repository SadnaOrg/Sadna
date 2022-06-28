package com.example.application.views.main.Discount.DiscountPred;

import ServiceLayer.Objects.Product;
import ServiceLayer.Response;
import ServiceLayer.interfaces.SubscribedUserService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;

import java.util.List;
import java.util.function.Predicate;

import static com.example.application.Utility.notifyError;
import static com.example.application.Utility.notifySuccess;
import static com.example.application.views.main.Discount.DiscountContentCreator.getNumberField;

public class DiscountPredCreator {
    private SubscribedUserService service;
    private Integer shopId;
    private int parentId;

    public DiscountPredCreator(SubscribedUserService service, Integer shopId, int parentId) {
        this.service = service;
        this.shopId = shopId;
        this.parentId = parentId;
    }

    public Component createValidateBasketQuantity() {
        VerticalLayout layout = new VerticalLayout();
        IntegerField selectProduct = createIntegerField("Enter Quantity");
        Checkbox checkbox = new Checkbox("Can't be more");
        Button button = new Button("Create Validate Basket Quantity");
        selectProduct.addValueChangeListener(e -> button.setEnabled(e.getValue() != null));
        button.addClickListener(e -> {
            if(selectProduct.getValue() != null && checkbox.getValue() != null) {
                var res = service.createValidateBasketQuantityDiscount(selectProduct.getValue(), checkbox.getValue(), parentId, shopId);
                validateResult(res, "Added Validate Basket Quantity Successfully!");
            }
            else{
                notifyError("Checkbox value is null");
            }
        });
        layout.add(selectProduct, checkbox, button);
        return layout;
    }

    public Component createValidateProductQuantity() {
        VerticalLayout layout = new VerticalLayout();
        Select<Integer> productIdSelect = createShopSelect();
        IntegerField quantityField = createIntegerField("Enter Quantity");
        Checkbox checkbox = new Checkbox("Can't be more");
        Button button = new Button("Create Validate Basket Quantity");
        quantityField.addValueChangeListener(e -> button.setEnabled(quantityField.getValue() != null && productIdSelect.getValue() != null && checkbox.getValue() != null));
        productIdSelect.addValueChangeListener(e -> button.setEnabled(quantityField.getValue() != null && productIdSelect.getValue() != null && checkbox.getValue() != null));
        checkbox.addValueChangeListener(e -> button.setEnabled(quantityField.getValue() != null && productIdSelect.getValue() != null && checkbox.getValue() != null));
        button.addClickListener(e -> {
            if(productIdSelect.getValue() != null && quantityField.getValue() != null && checkbox.getValue() != null) {
                var res = service.createValidateProductQuantityDiscount(productIdSelect.getValue(), quantityField.getValue(), checkbox.getValue(), parentId, shopId);
                validateResult(res, "Added Validate Product Quantity Successfully!");
            }
            else{
                notifyError("Checkbox value is null");
            }
        });
        layout.add(productIdSelect, quantityField, checkbox, button);
        return layout;
    }

    public Component createValidateBasketValue() {
        VerticalLayout layout = new VerticalLayout();
        NumberField quantityField = createNumberField("Enter Quantity", -1);
        Checkbox checkbox = new Checkbox("Can't be more");
        Button button = new Button("Create Validate Basket Quantity");
        quantityField.addValueChangeListener(e -> button.setEnabled(quantityField.getValue() != null && checkbox.getValue() != null));
        checkbox.addValueChangeListener(e -> button.setEnabled(quantityField.getValue() != null && checkbox.getValue() != null));
        button.addClickListener(e -> {
            if(quantityField.getValue() != null && checkbox.getValue() != null) {
                var res = service.createValidateBasketValueDiscount(quantityField.getValue(), checkbox.getValue(), parentId, shopId);
                validateResult(res, "Added Validate Basket Value Discount Successfully!");
            }
            else{
                notifyError("Checkbox value is null");
            }
        });
        layout.add(quantityField, checkbox, button);
        return layout;
    }

    private NumberField createNumberField(String label, int maxValue) {
        return getNumberField(label, maxValue);
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

    private void validateResult(Response<Integer> res, String successMessage) {
        if(res.isOk()){
            notifySuccess(successMessage);
            UI.getCurrent().getPage().reload();
        }
        else{
            notifyError(res.getMsg());
        }
    }
    private Select<Integer> createShopSelect() {
        Select<Integer> selectShop = new Select<>();
        selectShop.setLabel("Select Product");
        selectShop.setItems(filterShopIds());
        return selectShop;
    }

    private List<Integer> filterShopIds(){
        Predicate<Product> productPredicate = p -> true;
        var res = service.searchProducts(s -> s.shopId() == shopId, productPredicate);
        if(!res.isOk())
            return null;
        var myShop = res.getElement().shops().stream().filter(s -> s.shopId()==shopId).findFirst();
        if(myShop.isEmpty())
            return null;
        return myShop.get().shopProducts().stream().map(Product::productID).toList();
    }
}
