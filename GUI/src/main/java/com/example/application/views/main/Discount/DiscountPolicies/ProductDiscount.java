package com.example.application.views.main.Discount.DiscountPolicies;

import ServiceLayer.interfaces.SubscribedUserService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ProductDiscount extends LeafPolicy{
    private int productID;
    private double price;

    public ProductDiscount(SubscribedUserService service, int shopId, int parentId, int productID, double price) {
        super(service, shopId, parentId);
        this.productID = productID;
        this.price = price;
        layout.add(showProductQuantityInPriceDiscount());
    }

    private Component showProductQuantityInPriceDiscount(){
        VerticalLayout layout = new VerticalLayout();
        Label productLabel = new Label("Product ID: " + productID);
        Label priceLabel = new Label("Price for Quantity: " + price);
        layout.add(productLabel, priceLabel);
        return layout;
    }

    @Override
    public String toString() {
        return "Product Quantity In Price Discount";
    }
}
