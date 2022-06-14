package BusinessLayer.Shops.Polices.Purchase;

import BusinessLayer.Users.Basket;
import BusinessLayer.Users.SubscribedUser;
import BusinessLayer.Users.User;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

public class ValidateUserPurchase implements ValidatePurchasePolicy{

    private int age;
    private final int policyLogicId;

    public ValidateUserPurchase(int age) {
        this.age = age;
        this.policyLogicId = purchaseLogicId.incrementAndGet();
    }

    public ValidateUserPurchase(int policyLogicId, int age) {
        this.age = age;
        this.policyLogicId = policyLogicId;
    }

    @Override
    public boolean isValid(User u, Basket basket) {
        if(u instanceof SubscribedUser)
        {
            int yeardiff =LocalDate.now().getYear() -  ((SubscribedUser)u).getBirthDate().getYear();

            return yeardiff>=age;
        }
        return false;
    }

    @Override
    public LogicPurchasePolicy getLogicRule(int searchConnectId)
    {
        return null;
    }

    @Override
    public int getID(){return policyLogicId;}

    public int getAge() {
        return age;
    }

    public int getPolicyLogicId() {
        return policyLogicId;
    }
}
