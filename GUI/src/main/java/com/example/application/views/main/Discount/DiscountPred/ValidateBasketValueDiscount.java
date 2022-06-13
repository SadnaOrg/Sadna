package com.example.application.views.main.Discount.DiscountPred;

import ServiceLayer.interfaces.SubscribedUserService;
import com.vaadin.flow.component.html.Label;

public class ValidateBasketValueDiscount extends DiscountPred{
    private double basketValue;
    private boolean cantBeMore;
    private int ruleId;

    public ValidateBasketValueDiscount(SubscribedUserService service, int shopId, int parentId, double basketValue, boolean cantBeMore, int ruleId) {
        super(service, shopId, parentId);
        this.basketValue = basketValue;
        this.cantBeMore = cantBeMore;
        this.ruleId = ruleId;
    }

    private void createPredView() {
        layout.removeAll();
        Label basketValueLabel = new Label("Basket Value: " + basketValue);
        Label cantBeMoreLabel = new Label("Can't be more: " + cantBeMore);
        layout.add(basketValueLabel, cantBeMoreLabel);
    }

    @Override
    public String toString() {
        return "Validate Basket Value Discount";
    }
}
