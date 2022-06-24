package BusinessLayer.Shops.Polices.Purchase;

import BusinessLayer.Mappers.ShopMappers.Converter;
import BusinessLayer.Users.Basket;
import BusinessLayer.Users.User;
import com.SadnaORM.ShopImpl.ShopObjects.Policies.PurchasePolicyDTO;

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

    public ValidateTImeStampPurchase(int policyLogicId, LocalTime localTime, LocalDate date, boolean buybefore) {
        this.policyLogicId = policyLogicId;
        this.localTime = localTime;
        this.date = date;
        this.buybefore = buybefore;
    }

    public ValidateTImeStampPurchase(LocalTime localTime, LocalDate date, boolean buybefore) {
        this.policyLogicId = purchaseLogicId.incrementAndGet();
        this.localTime = localTime;
        this.date = date;
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
    @Override
    public LogicPurchasePolicy getLogicRule(int searchConnectId)
    {
        return null;
    }

    @Override
    public int getID() {
        return this.policyLogicId;
    }

    public int getPolicyLogicId() {
        return policyLogicId;
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

    @Override
    public PurchasePolicyDTO conversion(Converter c) {
        return c.convert(this);
    }
}
