package com.example.application.views.main.Discount.DiscountPred;

import ServiceLayer.interfaces.SubscribedUserService;
import com.vaadin.flow.component.html.Label;

public class ValidateProductQuantityDiscount extends DiscountPred{
    private int productId;
    private int productQuantity;
    private boolean cantBeMore;
    private int ruleId;

    public ValidateProductQuantityDiscount(SubscribedUserService service, int shopId, int parentId, int productId, int productQuantity, boolean cantBeMore, int ruleId) {
        super(service, shopId, parentId);
        this.productId = productId;
        this.productQuantity = productQuantity;
        this.cantBeMore = cantBeMore;
        this.ruleId = ruleId;
        createPredView();
    }

    private void createPredView() {
        layout.removeAll();
        Label productLabel = new Label("Product Id: " + productId);
        Label quantityLabel = new Label("Quantity: " + productQuantity);
        Label cantBeMoreLabel = new Label("Can't be more: " + cantBeMore);
        layout.add(productLabel, quantityLabel, cantBeMoreLabel);
    }

    @Override
    public String toString() {
        return "Validate Product Quantity Discount";
    }
}
