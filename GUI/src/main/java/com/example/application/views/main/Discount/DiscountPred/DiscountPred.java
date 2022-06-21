package com.example.application.views.main.Discount.DiscountPred;

import ServiceLayer.Objects.Policies.Discount.DiscountPolicyType;
import ServiceLayer.Objects.Policies.Discount.DiscountPredType;
import ServiceLayer.interfaces.SubscribedUserService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class DiscountPred {
    protected SubscribedUserService service;
    protected int shopId;
    protected int parentId;
    protected VerticalLayout layout;

    public DiscountPred(SubscribedUserService service, int shopId, int parentId) {
        layout = new VerticalLayout();
        this.service = service;
        this.shopId = shopId;
        this.parentId = parentId;
        createAddPred();
    }

    protected void createAddPred() {
        MenuBar menuBar = createMenuBar();
        for(var predType : DiscountPredType.values()){
            MenuItem item = menuBar.addItem(predType.name());
            item.addClickListener(itemClickEvent -> {
                layout.removeAll();
                layout.add(menuBar, createPred(predType, shopId));
            });
        }
        layout.removeAll();
        layout.add(menuBar);
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        menuBar.setMinWidth("var(--lumo-size-m)");
        menuBar.setWidthFull();
        menuBar.setOpenOnHover(true);
        return menuBar;
    }

    private Component createPred(DiscountPredType discountType, Integer shopId){
        DiscountPredCreator discountContentCreator = new DiscountPredCreator(service, shopId, parentId);
        Component component;
        switch (discountType){
            case VALIDATE_BASKET_QUANTITY_DISCOUNT -> component = discountContentCreator.createValidateBasketQuantity();
            case VALIDATE_PRODUCT_QUANTITY_DISCOUNT -> component = discountContentCreator.createValidateProductQuantity();
            case VALIDATE_BASKET_VALUE_DISCOUNT -> component = discountContentCreator.createValidateBasketValue();
            default -> component = null;
        }
        return component;
    }

    public VerticalLayout getLayout() {
        return layout;
    }

    @Override
    public String toString() {
        return "Add Pred";
    }
}
