package ServiceLayer.Objects.Policies.Purchase;

import java.time.LocalDate;
import java.time.LocalTime;

public record ValidateTimeStampPurchase(int policyLogicId, LocalTime localTime, LocalDate date, boolean buybefore) implements PurchasePolicy{

    public ValidateTimeStampPurchase(BusinessLayer.Shops.Polices.Purchase.ValidateTImeStampPurchase validateTImeStampPurchase)
    {
        this(validateTImeStampPurchase.getPolicyLogicId(), validateTImeStampPurchase.getLocalTime(),validateTImeStampPurchase.getDate()
        , validateTImeStampPurchase.isBuybefore());
    }

}
