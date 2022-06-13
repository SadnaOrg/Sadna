package com.example.application.views.main.Discount.DiscountPolicies;

import ServiceLayer.Response;
import ServiceLayer.interfaces.SubscribedUserService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;

import static com.example.application.Utility.notifyError;
import static com.example.application.Utility.notifySuccess;

public class ShopDiscount extends LeafPolicy{
    private double discount;
    private int basketQuantity;

    public ShopDiscount(SubscribedUserService service, int shopId, int parentId, double discount, int basketQuantity){
        super(service, shopId, parentId);
        this.discount = discount;
        this.basketQuantity = basketQuantity;
        layout.add(showShopDiscount());
    }

    private Component showShopDiscount() {
        VerticalLayout layout = new VerticalLayout();
        Label discountLabel = new Label("Discount: " + discount);
        Label basketLabel = new Label("Basket Quantity: " + basketQuantity);
        layout.add(discountLabel, basketLabel);
        return layout;
    }

    @Override
    public String toString() {
        return "Shop Discount";
    }
}
