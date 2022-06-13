package com.example.application.views.main.Discount.DiscountPolicies;

import ServiceLayer.interfaces.SubscribedUserService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ProductQuantityInPriceDiscount extends LeafPolicy{
    private int productID;
    private int quantity;
    private double priceForQuantity;

    public ProductQuantityInPriceDiscount(SubscribedUserService service, int shopId, int parentId, int productID, int quantity, double priceForQuantity) {
        super(service, shopId, parentId);
        this.productID = productID;
        this.quantity = quantity;
        this.priceForQuantity = priceForQuantity;
        layout.add(showProductQuantityInPriceDiscount());
    }

    private Component showProductQuantityInPriceDiscount(){
        VerticalLayout layout = new VerticalLayout();
        Label productLabel = new Label("Product ID: " + productID);
        Label quantityLabel = new Label("Quantity: " + quantity);
        Label priceLabel = new Label("Price for Quantity: " + priceForQuantity);
        layout.add(productLabel, quantityLabel, priceLabel);
        return layout;
    }

    @Override
    public String toString() {
        return "Product Quantity In Price Discount";
    }
}
