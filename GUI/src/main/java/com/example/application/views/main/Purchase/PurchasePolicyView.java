package com.example.application.views.main.Purchase;

import ServiceLayer.Objects.Policies.Purchase.PurchasePolicy;
import ServiceLayer.Objects.Shop;
import ServiceLayer.interfaces.SubscribedUserService;
import com.example.application.views.main.Discount.DiscountContentCreator;
import com.example.application.views.main.Discount.DiscountPolicies.DiscountPolicy;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;

import java.util.List;

public class PurchasePolicyView extends VerticalLayout {
    SubscribedUserService service;
    String currUser;
    Select<Integer> shopSelect;
    public PurchasePolicyView(SubscribedUserService service, String currUser){
        this.service = service;
        this.currUser = currUser;
        var res = service.searchShops(s -> true, currUser);
        if(res.isOk()){
            List<Integer> shops = res.getElement().shops().stream().map(Shop::shopId).toList();
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
                PurchaseContentCreator contentCreator = new PurchaseContentCreator(service, e.getValue());
                add(shopSelect, contentCreator.getPurchases().getLayout());
            }
        });
        return shopSelect;
    }
}
