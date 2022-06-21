package com.SadnaORM.ShopImpl.ShopObjects.Policies;

public class ValidateUserPurchaseDTO extends PurchasePolicyDTO{
    private int age;

    public ValidateUserPurchaseDTO(int ID, int shopID, int age) {
        super(ID, shopID);
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
