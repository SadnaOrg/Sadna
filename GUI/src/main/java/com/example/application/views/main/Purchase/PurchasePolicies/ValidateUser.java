package com.example.application.views.main.Purchase.PurchasePolicies;

import ServiceLayer.interfaces.SubscribedUserService;
import com.example.application.views.main.Purchase.PurchasePolicies.PurchasePolicy;
import com.vaadin.flow.component.html.Label;

public class ValidateUser extends LeafPurchase {
    private int age;

    public ValidateUser(SubscribedUserService service, int shopId, int parentId, int age) {
        super(service, shopId, parentId);
        this.age = age;
        createValidateView();
    }

    @Override
    public void createValidateView() {
        layout.removeAll();
        Label ageLabel = new Label("Age: " + age);
        layout.add(ageLabel);
    }

    @Override
    public String toString() {
        return "Validate User Purchase";
    }
}