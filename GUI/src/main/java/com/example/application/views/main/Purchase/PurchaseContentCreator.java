package com.example.application.views.main.Purchase;

import ServiceLayer.Objects.Policies.Purchase.*;
import ServiceLayer.Objects.Product;
import ServiceLayer.Response;
import ServiceLayer.interfaces.SubscribedUserService;
import com.example.application.views.main.Purchase.PurchasePolicies.*;
import com.example.application.views.main.Purchase.PurchasePolicies.ValidateUser;
import com.example.application.views.main.Purchase.PurchasePolicies.PurchasePolicy;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import static com.example.application.Utility.notifyError;
import static com.example.application.Utility.notifySuccess;

public class PurchaseContentCreator {
    private SubscribedUserService service;
    private Integer shopId;

    public PurchaseContentCreator(SubscribedUserService service, Integer shopId) {
        this.service = service;
        this.shopId = shopId;
    }

    public Component createAndPurchase(Integer parentId) {
        Button button = new Button("Create And Purchase");
        button.addClickListener(e -> {
            var res = service.createPurchaseAndPolicy(null, parentId, shopId);
            validateResult(res, "Added And Purchase Successfully!");
        });
        return button;
    }

    public Component createOrPurchase(Integer parentId) {
        Button button = new Button("Create Or Purchase");
        button.addClickListener(e -> {
            var res = service.createPurchaseOrPolicy(null, parentId, shopId);
            validateResult(res, "Added Or Purchase Successfully!");
        });
        return button;
    }

    public Component createValidateCategory(int parentId) {
        VerticalLayout layout = new VerticalLayout();
        Button button = new Button("Create Validate Category");
        TextField categoryField = createTextField("Category");
        IntegerField quantityField = createIntegerField("Quantity");
        Checkbox cantBeMoreBox = new Checkbox("Can't Be More");
        categoryField.addValueChangeListener(e -> button.setEnabled(!Objects.equals(categoryField.getValue(), "") && quantityField.getValue() != null && cantBeMoreBox.getValue() != null));
        quantityField.addValueChangeListener(e -> button.setEnabled(!Objects.equals(categoryField.getValue(), "") && quantityField.getValue() != null && cantBeMoreBox.getValue() != null));
        cantBeMoreBox.addValueChangeListener(e -> button.setEnabled(!Objects.equals(categoryField.getValue(), "") && quantityField.getValue() != null && cantBeMoreBox.getValue() != null));
        button.addClickListener(e -> {
            var res = service.createValidateCategoryPurchase(categoryField.getValue(), quantityField.getValue(), cantBeMoreBox.getValue(), parentId, shopId);
            validateResult(res, "Added Validate Category Successfully!");
        });
        layout.add(categoryField, quantityField, cantBeMoreBox, button);
        return layout;
    }

    public Component createValidateProduct(int parentId) {
        VerticalLayout layout = new VerticalLayout();
        Button button = new Button("Create Validate Product");
        Select<Integer> idField = createProductSelect();
        IntegerField quantityField = createIntegerField("Quantity");
        Checkbox cantBeMoreBox = new Checkbox("Can't Be More");
        idField.addValueChangeListener(e -> button.setEnabled(idField.getValue() != null && quantityField.getValue() != null && cantBeMoreBox.getValue() != null));
        quantityField.addValueChangeListener(e -> button.setEnabled(idField.getValue() != null && quantityField.getValue() != null && cantBeMoreBox.getValue() != null));
        cantBeMoreBox.addValueChangeListener(e -> button.setEnabled(idField.getValue() != null && quantityField.getValue() != null && cantBeMoreBox.getValue() != null));
        button.addClickListener(e -> {
            var res = service.createValidateProductPurchase(idField.getValue(), quantityField.getValue(), cantBeMoreBox.getValue(), parentId, shopId);
            validateResult(res, "Added Validate Product Successfully!");
        });
        layout.add(idField, quantityField, cantBeMoreBox, button);
        return layout;
    }

    public Component createValidateTimeStamp(int parentId) {
        VerticalLayout layout = new VerticalLayout();
        Button button = new Button("Create Validate Time Stamp");
        TimePicker timePicker = new TimePicker("Choose Time Stamp");
        timePicker.setClearButtonVisible(true);
        DatePicker datePicker = new DatePicker("Choose Date Stamp");
        datePicker.setClearButtonVisible(true);
        Checkbox cantBeMoreBox = new Checkbox("Can't Be More");
        timePicker.addValueChangeListener(e -> button.setEnabled((datePicker.getValue() != null ^ timePicker.getValue() != null) && cantBeMoreBox.getValue() != null));
        datePicker.addValueChangeListener(e -> button.setEnabled((datePicker.getValue() != null ^ timePicker.getValue() != null) && cantBeMoreBox.getValue() != null));
        cantBeMoreBox.addValueChangeListener(e -> button.setEnabled((datePicker.getValue() != null ^ timePicker.getValue() != null) && cantBeMoreBox.getValue() != null));
        button.addClickListener(e -> {
            if(datePicker.getValue() != null){
                var res = service.createValidateDateStampPurchase(datePicker.getValue(), parentId, shopId);
                validateResult(res, "Added Validate Time Stamp Successfully!");

            }
            else if(timePicker.getValue() != null){
                var res = service.createValidateTImeStampPurchase(timePicker.getValue(), cantBeMoreBox.getValue(), parentId, shopId);
                validateResult(res, "Added Validate Time Stamp Successfully!");
            }
        });
        layout.add(timePicker, datePicker, cantBeMoreBox, button);
        return layout;
    }

    public Component createValidateUserPurchase(int parentId) {
        VerticalLayout layout = new VerticalLayout();
        IntegerField ageField = createIntegerField("Age");
        Button button = new Button("Create Validate User Purchase");
        ageField.addValueChangeListener(e -> button.setEnabled(e.getValue() != null));
        button.addClickListener(e -> {
            var res = service.createValidateUserPurchase(ageField.getValue(), parentId, shopId);
            validateResult(res, "Added Validate User Purchase Successfully!");
        });
        layout.add(ageField, button);
        return layout;
    }

    private TextField createTextField(String text) {
        TextField field = new TextField(text);
        field.setClearButtonVisible(true);
        return field;
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

    public PurchasePolicy getPurchases() {
        var res = service.getPurchasePolicy(shopId);
        int id = findParentId(res.getElement());
        if(res.isOk() && id >= 0){
            return getPurchases(res.getElement(), findParentId(res.getElement()));
        }
        return null;
    }

    public int findParentId(ServiceLayer.Objects.Policies.Purchase.PurchasePolicy purchasePolicy){
        if(purchasePolicy instanceof ValidateTimeStampPurchase purchase) {
            return purchase.policyLogicId();
        }

        else if(purchasePolicy instanceof ValidateProductPurchase purchase) {
            return purchase.policyLogicId();
        }

        else if(purchasePolicy instanceof ValidateUserPurchase purchase) {
            return purchase.policyLogicId();
        }

        else if(purchasePolicy instanceof PurchaseAndPolicy purchase) {
            return purchase.policyLogicId();
        }

        else if(purchasePolicy instanceof PurchaseOrPolicy purchase) {
            return purchase.policyLogicId();
        }
        return -1;
    }

    public PurchasePolicy getPurchases(ServiceLayer.Objects.Policies.Purchase.PurchasePolicy purchasePolicy, int parentId){
        if(purchasePolicy instanceof ValidateProductPurchase purchase) {
            return new ValidateProduct(service, shopId, parentId, purchase.productId(), purchase.productQuantity(), purchase.cantbemore());
        }
        else if(purchasePolicy instanceof ValidateTimeStampPurchase purchase) {
            return new ValidateTimeStamp(service, shopId, parentId, purchase.localTime(), purchase.buybefore(), purchase.date());
        }
        else if(purchasePolicy instanceof ValidateUserPurchase purchase) {
            return new ValidateUser(service, shopId, parentId, purchase.age());
        }
        else if(purchasePolicy instanceof ValidateCategoryPurchase purchase) {
            return new ValidateCategory(service, shopId, parentId, purchase.productQuantity(), purchase.cantbemore(), purchase.category());
        }
        else if(purchasePolicy instanceof PurchaseAndPolicy purchase) {
            List<PurchasePolicy> policies = new ArrayList<>();
            for(var p : purchase.purchasePolicies()){
                policies.add(getPurchases(p));
            }
            return new AndPurchase(service, shopId, parentId, policies);
        }

        else if(purchasePolicy instanceof PurchaseOrPolicy purchase) {
            List<PurchasePolicy> policies = new ArrayList<>();
            for(var p : purchase.purchasePolicies()){
                policies.add(getPurchases(p));
            }
            return new OrPurchase(service, shopId, parentId, policies);
        }
        return null;
    }

    private PurchasePolicy getPurchases(ServiceLayer.Objects.Policies.Purchase.PurchasePolicy purchase) {
        return getPurchases(purchase, findParentId(purchase));
    }

    private Select<Integer> createProductSelect() {
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
