package com.example.application.views.main.Purchase.PurchasePolicies;

import ServiceLayer.interfaces.SubscribedUserService;
import com.vaadin.flow.component.html.Label;

public class ValidateProduct extends LeafPurchase{
    private int productId;
    private int productQuantity;
    private boolean cantBeMore;

    public ValidateProduct(SubscribedUserService service, int shopId, int parentId, int productId, int productQuantity, boolean cantBeMore) {
        super(service, shopId, parentId);
        this.productId = productId;
        this.productQuantity = productQuantity;
        this.cantBeMore = cantBeMore;
        createValidateView();
    }

    @Override
    public void createValidateView() {
        layout.removeAll();
        Label idLabel = new Label("Id: " + productId);
        Label quantityLabel = new Label("Quantity: " + productQuantity);
        Label cantBeMoreLabel = new Label("Can't be more: " + cantBeMore);
        layout.add(idLabel, quantityLabel, cantBeMoreLabel);
    }

    @Override
    public String toString() {
        return "Validate Product Purchase";
    }
}
