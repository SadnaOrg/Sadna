package com.example.application.views.main;

import ServiceLayer.Objects.ApproveBid;
import ServiceLayer.Objects.BidOffer;
import ServiceLayer.Objects.Shop;
import ServiceLayer.interfaces.SubscribedUserService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.example.application.Utility.notifyError;
import static com.example.application.Utility.notifySuccess;
import static com.example.application.views.main.ApproveAssignmentsView.createProgressBar;

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

    private Grid<ApproveBid> createBidsGrid(Collection<BidOffer> bids) {
        var c = new LinkedList<ApproveBid>();
        for (var bid :bids) {
            c.addAll(bid.approvals().values().stream().filter(ab->!ab.administraitorsApproval().getOrDefault(currUser,true)).collect(Collectors.toList()));
        }
        var g = new Grid<ApproveBid>(ApproveBid.class,false);
        g.addColumn(ApproveBid::userName).setHeader("from");
        g.addColumn(ApproveBid::productId).setHeader("product id");
        g.addColumn(ApproveBid::quantity).setHeader("quantity");
        g.addColumn(ApproveBid::price).setHeader("price");
        g.addComponentColumn(ab-> createProgressBar(ab.administraitorsApproval())).setHeader("approval progress");
        g.addComponentColumn(ab->{
        var b =new Button("approve",(e)->{
            var res = service.approveBidOffer(ab.userName(),ab.productId(),ab.shopId());
            if(res.isOk()){
                notifySuccess("approved bid successfully");
                UI.getCurrent().getPage().reload();
            }
            else
                notifyError(res.getMsg());
        });
        b.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        return b;
        }).setHeader("approved bid");
        g.addComponentColumn(bid -> {
            var b = new Button("decline", (e) -> {
                var res = service.declineBidOffer(bid.userName(),bid.productId(),bid.shopId());
                if (res.isOk()) {
                    notifySuccess("decline bid successfully");
                    UI.getCurrent().getPage().reload();
                } else
                    notifyError(res.getMsg());
            });
            b.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
            return b;
        }).setHeader("decline bid");
        g.setItems(c);
        return g;
    }

    private Component createBidsProgressBar1(ConcurrentHashMap<String, Boolean> approvals) {
        return createProgressBar(approvals);

    }

}
