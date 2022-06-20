package com.example.application.views.main.Purchase.PurchasePolicies;

import ServiceLayer.interfaces.SubscribedUserService;
import com.vaadin.flow.component.html.Label;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ValidateTimeStamp extends LeafPurchase{
    private LocalTime localTime;
    private boolean buyBefore;
    private LocalDate date;

    public ValidateTimeStamp(SubscribedUserService service, int shopId, int parentId, LocalTime localTime, boolean buyBefore, LocalDate date) {
        super(service, shopId, parentId);
        this.localTime = localTime;
        this.buyBefore = buyBefore;
        this.date = date;
        createValidateView();
    }

    @Override
    public void createValidateView() {
        layout.removeAll();
        if(localTime != null) {
            Label localTimeLabel = new Label("Local Time: " + localTime.format(DateTimeFormatter.ISO_LOCAL_TIME));
            layout.add(localTimeLabel);
        }
        if(date != null) {
            Label dateLabel = new Label("Date: " + date.format(DateTimeFormatter.ISO_LOCAL_DATE));
            layout.add(dateLabel);
        }
        Label buyBeforeLabel = new Label("Buy Before: " + buyBefore);
        layout.add(buyBeforeLabel);
    }

    @Override
    public String toString() {
        return "Validate Time Stamp Purchase";
    }
}
