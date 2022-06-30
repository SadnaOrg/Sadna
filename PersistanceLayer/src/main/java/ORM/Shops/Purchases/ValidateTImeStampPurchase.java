package ORM.Shops.Purchases;


import ORM.Shops.Shop;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "ValidateTImeStampPurchase")
public class ValidateTImeStampPurchase extends PurchasePolicy{
    private LocalTime localTime;
    private LocalDate date;
    private boolean buybefore;

    public ValidateTImeStampPurchase() {
    }

    public ValidateTImeStampPurchase(LocalTime localTime, LocalDate date, boolean buybefore) {
        this.localTime = localTime;
        this.date = date;
        this.buybefore = buybefore;
    }

    public ValidateTImeStampPurchase(Shop shop, int ID, LocalTime localTime, LocalDate date, boolean buybefore) {
        super(shop, ID);
        this.localTime = localTime;
        this.date = date;
        this.buybefore = buybefore;
    }

    public LocalTime getLocalTime() {
        return localTime;
    }

    public LocalDate getDate() {
        return date;
    }

    public boolean isBuybefore() {
        return buybefore;
    }
}