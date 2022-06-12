package BusinessLayer.Shops.Polices.Purchase;

import BusinessLayer.Users.Basket;
import BusinessLayer.Users.User;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

public class ValidateTImeStampPurchase implements ValidatePurchasePolicy{


    private final int policyLogicId;
    private LocalTime localTime =null;
    private LocalDate date= null;
    private boolean buybefore=false;

    public ValidateTImeStampPurchase(LocalDate date) {
        this.date = date;
        this.policyLogicId = purchaseLogicId.incrementAndGet();
    }

    public ValidateTImeStampPurchase(LocalTime localTime, boolean buybefore) {
        this.localTime = localTime;
        this.buybefore = buybefore;
        this.policyLogicId = purchaseLogicId.incrementAndGet();
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
    @Override
    public LogicPurchasePolicy getLogicRule(int searchConnectId)
    {
        return null;
    }

    @Override
    public int getID() {
        return this.policyLogicId;
    }
}
