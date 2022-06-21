package com.SadnaORM.ShopImpl.ShopObjects.Policies;

import java.time.LocalDate;
import java.time.LocalTime;

public class ValidateTImeStampPurchaseDTO extends PurchasePolicyDTO{
    private LocalTime localTime;
    private LocalDate date;
    private boolean buybefore;

    public ValidateTImeStampPurchaseDTO(int ID, int shopID, LocalTime localTime, LocalDate date, boolean buybefore) {
        super(ID, shopID);
        this.localTime = localTime;
        this.date = date;
        this.buybefore = buybefore;
    }

    public LocalTime getLocalTime() {
        return localTime;
    }

    public void setLocalTime(LocalTime localTime) {
        this.localTime = localTime;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isBuybefore() {
        return buybefore;
    }

    public void setBuybefore(boolean buybefore) {
        this.buybefore = buybefore;
    }
}
