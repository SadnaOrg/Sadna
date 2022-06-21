package com.example.application.views.main.Discount.DiscountPred;

import ServiceLayer.interfaces.SubscribedUserService;
import com.vaadin.flow.component.html.Label;

public class ValidateBasketQuantityDiscount extends DiscountPred{
    private int basketQuantity;
    private boolean cantBeMore;
    private int ruleId;

    public ValidateBasketQuantityDiscount(SubscribedUserService service, int shopId, int parentId, int basketQuantity, boolean cantBeMore, int ruleId) {
        super(service, shopId, parentId);
        this.basketQuantity = basketQuantity;
        this.cantBeMore = cantBeMore;
        this.ruleId = ruleId;
        createPredView();
    }

    private void createPredView() {
        layout.removeAll();
        Label basketQuantityLabel = new Label("Basket Quantity: " + basketQuantity);
        Label cantBeMoreLabel = new Label("Can't be more: " + cantBeMore);
        layout.add(basketQuantityLabel, cantBeMoreLabel);
    }

    @Override
    public String toString() {
        return "Validate Basket Quantity Discount";
    }
}
