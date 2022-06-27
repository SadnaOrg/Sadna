package com.example.application.views.main;

import ServiceLayer.Objects.ApproveBid;
import ServiceLayer.Objects.BidOffer;
import ServiceLayer.Objects.HeskemMinui;
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
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.example.application.Utility.notifyError;
import static com.example.application.Utility.notifySuccess;



public class ApproveAssignmentsView extends VerticalLayout {
    SubscribedUserService service;
    String currUser;

    public ApproveAssignmentsView(SubscribedUserService service, String currUser) {
        this.service = service;
        this.currUser = currUser;
        var res = service.getHeskemeyMinui();
        if (res.isOk()) {
            add(creatHeskemDisplay(res.getElement().stream().filter(h->!h.approvals().getOrDefault(currUser,true)).collect(Collectors.toList())));
        } else
            add(new Label("there is no assignments to approve"));
    }

    private Component creatHeskemDisplay(Collection<HeskemMinui> list) {
        var layout = new VerticalLayout();
        layout.setWidthFull();
        layout.add(new H1("Your assignments to approve:"), createHeskemsGrid(list));
        return layout;
    }

    private Grid<HeskemMinui> createHeskemsGrid(Collection<HeskemMinui> heskemim) {
        var g = new Grid<HeskemMinui>(HeskemMinui.class, false);
        g.addColumn(HeskemMinui::shopId).setHeader("Shop id").setSortable(true).setComparator(Comparator.comparingInt(HeskemMinui::shopId));
        g.addColumn(HeskemMinui::adminToAssign).setHeader("To assign");
        g.addColumn(HeskemMinui::appointer).setHeader("Appoint by");
        g.addComponentColumn(heskem -> createProgressBar(heskem.approvals())).setHeader("approval progress");
        g.addComponentColumn(heskem -> {
            var b = new Button("approve", (e) -> {
                var res = service.approveHeskemMinui(heskem.shopId(),heskem.adminToAssign());
                if (res.isOk()) {
                    notifySuccess("approved assign successfully");
                    UI.getCurrent().getPage().reload();
                } else
                    notifyError(res.getMsg());
            });
            b.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
            return b;
        }).setHeader("approve ");
        g.addComponentColumn(heskem -> {
            var b = new Button("decline", (e) -> {
                var res = service.declineHeskemMinui(heskem.shopId(),heskem.adminToAssign());
                if (res.isOk()) {
                    notifySuccess("decline assign successfully");
                    UI.getCurrent().getPage().reload();
                } else
                    notifyError(res.getMsg());
            });
            b.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
            return b;
        }).setHeader("decline ");
        g.setItems(heskemim);
        return g;
    }


    static Component createProgressBar(ConcurrentHashMap<String, Boolean> approvals) {
        var x = (int) approvals.values().stream().filter(b -> b).count();
        ProgressBar progressBar = new ProgressBar();
        progressBar.setMin(0);
        progressBar.setMax(approvals.size());
        progressBar.setValue(x);
        var lable = new Label("progress (" + x + "/" + approvals.size() + ")");
        var layer = new HorizontalLayout(lable, progressBar);
        layer.setWidthFull();
        return layer;
    }
}



