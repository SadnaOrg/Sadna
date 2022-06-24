package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Mappers.Converter;
import BusinessLayer.Shops.Shop;
import BusinessLayer.Users.Basket;

public class RelatedGroupDiscount implements DiscountPolicy{
    private int discountId;
    private String category;
    private double discount;

    public RelatedGroupDiscount(String category, double discount)
    {
        this.category =category;
        this.discount =discount;
        this.discountId = atomicDiscountID.incrementAndGet();
    }

    public RelatedGroupDiscount(int discountId,String category, double discount)
    {
        this.category =category;
        this.discount = discount;
        this.discountId = discountId;
    }



    @Override
    public double calculateDiscount(Basket basket) {
        double currentDiscountPrice=0;
        for (int pid:basket.getCategories().keySet()) {
            String productCategory =basket.getCategories().get(pid);
            if(productCategory!= null) {
                if (category.equals(productCategory)) {
                    currentDiscountPrice += discount * basket.getProducts().get(pid) * basket.getPrices().get(pid);
                }
            }
        }
        return currentDiscountPrice;
    }


    public void setCategory(String category)
    {
        this.category =category;
    }


    @Override
    public NumericDiscountRules getNumericRule(int searchConnectId) {
        return null;
    }

    @Override
    public LogicDiscountRules getLogicRule(int searchConnectId) {
        return null;
    }

    @Override
    public int getID(){
        return this.discountId;
    }

    @Override
    public boolean removeSonPredicate(int ID) {
        return false;
    }

    public int getDiscountId() {
        return discountId;
    }

    public String getCategory() {
        return category;
    }

    public double getDiscount() {
        return discount;
    }

    @Override
    public ORM.Shops.Discounts.DiscountPolicy toEntity(Converter c, ORM.Shops.Shop shop) {
        return c.toEntity(this,shop);
    }
}
