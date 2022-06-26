package com.example.application.views.main.Discount.DiscountPolicies;

import ServiceLayer.Objects.Policies.Discount.DiscountPolicyType;
import ServiceLayer.interfaces.SubscribedUserService;
import com.example.application.views.main.Discount.DiscountContentCreator;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class CompositePolicy extends DiscountPolicy{
    protected Accordion policies = new Accordion();
    protected MenuBar menuBar;

    public CompositePolicy(SubscribedUserService service, int shopId, int parentId) {
        super(service, shopId, parentId);
        layout = new VerticalLayout();
        createAddPolicy();
    }

    protected void createAddPolicy() {
        menuBar = createMenuBar();
        for(var discountType : DiscountPolicyType.values()){
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

    private Component createPolicy(DiscountPolicyType discountType, Integer shopId){
        DiscountContentCreator discountContentCreator = new DiscountContentCreator(service, shopId);
        Component component;
        switch (discountType){
            case AND -> component = discountContentCreator.createAndDiscount(parentId);
            case OR -> component = discountContentCreator.createOrDiscount(parentId);
            case XOR -> component = discountContentCreator.createXorDiscount(parentId);
            case PLUS -> component = discountContentCreator.createPlusDiscount(parentId);
            case MAX -> component = discountContentCreator.createMaxDiscount(parentId);
            case PRODUCT_QUANTITY -> component = discountContentCreator.createProductByQuantityDiscount(parentId);
            case PRODUCT -> component = discountContentCreator.createProductDiscount(parentId);
            case QUANTITY_IN_PRICE -> component = discountContentCreator.createProductQuantityInPriceDiscount(parentId);
//            case CATEGORY -> component = discountContentCreator.createChangeManagerPermission();
            case SHOP_DISCOUNT -> component = discountContentCreator.createShopDiscount(parentId);
            default -> component = null;
        }
        return component;
    }
}
