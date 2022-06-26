package com.example.application.views.main.Purchase.PurchasePolicies;

import ServiceLayer.interfaces.SubscribedUserService;
import com.vaadin.flow.component.html.Label;

public class ValidateCategory extends LeafPurchase{
    private int productQuantity;
    private boolean cantBeMore;
    private String category;

    public ValidateCategory(SubscribedUserService service, int shopId, int parentId, int productQuantity, boolean cantBeMore, String category) {
        super(service, shopId, parentId);
        this.productQuantity = productQuantity;
        this.cantBeMore = cantBeMore;
        this.category = category;
        createValidateView();
    }

    @Override
    public void createValidateView() {
        layout.removeAll();
        Label quantityLabel = new Label("Quantity: " + productQuantity);
        Label cantBeMoreLabel = new Label("Can't be more: " + cantBeMore);
        Label idLabel = new Label("Category: " + category);
        layout.add(idLabel, quantityLabel, cantBeMoreLabel);
    }

    @Override
    public String toString() {
        return "Validate Category Purchase";
    }
}
