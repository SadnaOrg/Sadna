package BusinessLayer.Shops.Polices.Purchase;

import BusinessLayer.Users.Basket;
import BusinessLayer.Users.User;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

public class ValidateTImeStampPurchase implements ValidatePurchasePolicy{


    private LocalTime localTime =null;
    private LocalDate date= null;
    private boolean buybefore;

    public ValidateTImeStampPurchase(LocalDate date) {
        this.date = date;
    }

    public ValidateTImeStampPurchase(LocalTime localTime, boolean buybefore) {
        this.localTime = localTime;
        this.buybefore = buybefore;
    }

    @Override
    public boolean isValid(User u, Basket basket) {
        if(localTime!=null)
        {
            if(buybefore)
                return 0>= LocalTime.now().compareTo(localTime);
            else
                return 0<= LocalTime.now().compareTo(localTime);
        }
        if(date!=null)
        {
            return LocalDate.now().compareTo(date)==0;
        }
        return true;
    }
}
