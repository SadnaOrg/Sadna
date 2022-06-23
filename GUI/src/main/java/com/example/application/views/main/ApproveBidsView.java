package com.example.application.views.main;

import ServiceLayer.Objects.ApproveBid;
import ServiceLayer.Objects.BidOffer;
import ServiceLayer.Objects.ProductInfo;
import ServiceLayer.Objects.Shop;
import ServiceLayer.interfaces.SubscribedUserService;
import com.example.application.views.main.Purchase.PurchaseContentCreator;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
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
import java.util.stream.Collectors;

import static com.example.application.Utility.notifyError;
import static com.example.application.Utility.notifySuccess;

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
        var g = new Grid<ApproveBid>(ApproveBid.class,false);
        g.addColumn(ApproveBid::productId).setHeader("product id");
        g.addColumn(ApproveBid::quantity).setHeader("quantity");
        g.addColumn(ApproveBid::price).setHeader("price");
        g.addColumn(ab->createBidsProgressBar(ab.administraitorsApproval())).setHeader("approval progress");
        g.addColumn(ab-> new Button("approve",(e)->{
            var res = service.approveBidOffer(currUser,ab.productId(),ab.shopId());
            if(res.isOk()){
                notifySuccess("approved bid successfully");
                UI.getCurrent().getPage().reload();
            }
            else
                notifyError(res.getMsg());
        })).setHeader("approved bid");
        return g;
    }

    private Component createBidsProgressBar(ConcurrentHashMap<String, Boolean> approvals) {
        Chart chart = new Chart(ChartType.SOLIDGAUGE);
        ListSeries series = new ListSeries("Approvals", (int) approvals.values().stream().filter(b -> b).count());
        Configuration conf = chart.getConfiguration();
        conf.setTitle("Solid Gauge");

        Pane pane = conf.getPane();
        //        pane.setSize("100%");           // For positioning tick labels
        //        pane.setCenter("50%", "70%"); // Move center lower
        //        pane.setStartAngle(-180);        // Make semi-circle
        //        pane.setEndAngle(180);

        Background bkg = new Background();
        bkg.setInnerRadius("60%");  // To make it an arc and not circle
        bkg.setOuterRadius("100%"); // Default - not necessary
        bkg.setShape(BackgroundShape.ARC);        // solid or arc
        pane.setBackground(bkg);


        YAxis yaxis = new YAxis();
        yaxis.setTitle("Approvals");
        yaxis.getTitle().setY(-80); // Move 70 px upwards from center

        // The limits are mandatory
        yaxis.setMin(0);
        yaxis.setMax(approvals.size());

        // Configure ticks and labels
        yaxis.setTickInterval(1);  // At 0, 100, and 200
        yaxis.getLabels().setY(-16); // Move 16 px upwards
        yaxis.setGridLineWidth(0); // Disable grid

        PlotOptionsSolidgauge options = new PlotOptionsSolidgauge();


        conf.addSeries(series);

        return chart;

    }

}
