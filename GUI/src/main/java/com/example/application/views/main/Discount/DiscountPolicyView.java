package com.example.application.views.main.Discount;

import ServiceLayer.Objects.Policies.Discount.DiscountPolicyType;
import ServiceLayer.interfaces.SubscribedUserService;
import com.example.application.views.main.Discount.DiscountPolicies.DiscountPolicy;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DiscountPolicyView extends VerticalLayout {
    SubscribedUserService service;
    String currUser;
    Select<Integer> shopSelect;
    DiscountPolicy policy;
    public DiscountPolicyView(SubscribedUserService service, String currUser){
        this.service = service;
        this.currUser = currUser;
        var res = service.searchShops(s -> true, currUser);
        if(res.isOk()){
            List<Integer> shops = res.getElement().shops().stream().map(s -> s.shopId()).toList();
            shopSelect = createShopSelect(shops);
            add(shopSelect);
        }
    }

    private Select<Integer> createShopSelect(List<Integer> shops) {
        Select<Integer> shopSelect = new Select<>();
        shopSelect.setItems(shops);
        shopSelect.addValueChangeListener(e -> {
            removeAll();
            if(e.getValue() != null){
                DiscountContentCreator contentCreator = new DiscountContentCreator(service, e.getValue());
                add(shopSelect, contentCreator.getDiscounts().getLayout());
            }
        });
        return shopSelect;
    }
}
