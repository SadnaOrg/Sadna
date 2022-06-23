package com.example.application.views.main;

import ServiceLayer.Objects.BidOffer;
import ServiceLayer.Objects.ProductInfo;
import ServiceLayer.Objects.Shop;
import ServiceLayer.interfaces.SubscribedUserService;
import com.example.application.views.main.Purchase.PurchaseContentCreator;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ApproveBidsView extends VerticalLayout {
    SubscribedUserService service;
    String currUser;

    public ApproveBidsView(SubscribedUserService service, String currUser) {
        this.service = service;
        this.currUser = currUser;
        var res = service.getBidsToApprove();
        if(res.isOk()){
            add(creatBidDisplay(res.getElement()));
        }
        else
            add(new Label("there is no bid to approve"));
    }

    private Component creatBidDisplay(ConcurrentHashMap<Shop, Collection<BidOffer>> map) {
        var layout =  new VerticalLayout();
        layout.setWidthFull();
        var accordion = new Accordion();
        accordion.setWidthFull();
        for( var e :map.entrySet()){
             var bids = createBidsGrid(e.getValue());
             var bidsPannel = accordion.add("shop id : "+e.getKey().shopId(),new VerticalLayout(bids));
            bidsPannel.addThemeVariants(DetailsVariant.FILLED);
            bids.setWidthFull();
        }
        layout.add(new H1("your bids to approve:"),accordion);
        return layout;
    }

    private Grid<BidOffer> createBidsGrid(Collection<BidOffer> bids) {
        var g = new Grid<BidOffer>(BidOffer.class,false);
        g.addColumn(BidOffer::products).setHeader("Products");
        return g;
    }

}
