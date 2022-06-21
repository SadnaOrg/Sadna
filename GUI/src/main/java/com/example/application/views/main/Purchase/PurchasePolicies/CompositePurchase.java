package com.example.application.views.main.Purchase.PurchasePolicies;

import ServiceLayer.Objects.Policies.Purchase.PurchasePolicyType;
import ServiceLayer.interfaces.SubscribedUserService;
import com.example.application.views.main.Purchase.PurchaseContentCreator;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class CompositePurchase extends PurchasePolicy {
    protected Accordion policies = new Accordion();
    protected MenuBar menuBar;

    public CompositePurchase(SubscribedUserService service, int shopId, int parentId) {
        super(service, shopId, parentId);
        layout = new VerticalLayout();
        createAddPolicy();
    }

    protected void createAddPolicy() {
        menuBar = createMenuBar();
        for(var discountType : PurchasePolicyType.values()){
            MenuItem item = menuBar.addItem(discountType.name());
            item.addClickListener(itemClickEvent -> {
                layout.removeAll();
                layout.add(menuBar, createPolicy(discountType, shopId), policies);
            });
        }
        layout.removeAll();
        layout.add(menuBar, policies);
    }

    private MenuBar createMenuBar(){
        MenuBar menuBar = new MenuBar();
        menuBar.setMinWidth("var(--lumo-size-m)");
        menuBar.setWidthFull();
        menuBar.setOpenOnHover(true);
        return menuBar;
    }

    private Component createPolicy(PurchasePolicyType discountType, Integer shopId){
        PurchaseContentCreator purchaseContentCreator = new PurchaseContentCreator(service, shopId);
        Component component;
        switch (discountType){
            case AND -> component = purchaseContentCreator.createAndPurchase(parentId);
            case OR -> component = purchaseContentCreator.createOrPurchase(parentId);
            case VALIDATE_CATEGORY -> component = purchaseContentCreator.createValidateCategory(parentId);
            case VALIDATE_PRODUCT -> component = purchaseContentCreator.createValidateProduct(parentId);
            case VALIDATE_TIME_STAMP -> component = purchaseContentCreator.createValidateTimeStamp(parentId);
            case VALIDATE_USER_PURCHASE -> component = purchaseContentCreator.createValidateUserPurchase(parentId);
            default -> component = null;
        }
        return component;
    }
}
