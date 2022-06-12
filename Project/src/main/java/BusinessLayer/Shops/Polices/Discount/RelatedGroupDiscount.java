package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;

import java.util.ArrayList;
import java.util.Collection;

public class RelatedGroupDiscount implements DiscountPolicy{
    private int discountId;
    private Collection<Integer> relatedProducts;
    private double discount;

    public RelatedGroupDiscount(Collection<Integer> relatedProducts, double discount)
    {
        this.relatedProducts= new ArrayList<>();
        this.relatedProducts.addAll(relatedProducts);
        this.discount =discount;
        this.discountId = atomicDiscountID.incrementAndGet();
    }

    public RelatedGroupDiscount(int discountId,Collection<Integer> relatedProducts, double discount) {
        this.relatedProducts = relatedProducts;
        this.discount = discount;
        this.discountId = discountId;
    }

    @Override
    public double calculateDiscount(Basket basket) {
        double currentDiscountPrice=0;
        for (int pid:basket.getProducts().keySet()) {
            if(relatedProducts.contains(pid))
            {
                currentDiscountPrice +=  discount*basket.getProducts().get(pid)*basket.getPrices().get(pid);
            }
        }
        return currentDiscountPrice;
    }


    public void setProducts(Collection<Integer> newProducts)
    {
        this.relatedProducts= new ArrayList<>();
        this.relatedProducts.addAll(newProducts);
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
}
