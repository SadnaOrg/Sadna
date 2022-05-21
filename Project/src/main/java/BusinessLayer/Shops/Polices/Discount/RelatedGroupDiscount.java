package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;

import java.util.ArrayList;
import java.util.Collection;

public class RelatedGroupDiscount extends DiscountPolicy{
    Collection<Integer> relatedProducts;
    double discount;

    public RelatedGroupDiscount(Collection<Integer> relatedProducts, double discount)
    {
        super();
        this.relatedProducts= new ArrayList<>();
        this.relatedProducts.addAll(relatedProducts);
        this.discount =discount;
    }


    @Override
    double calculateDiscount(Basket basket) {
        double currentDiscountPrice=0;
        for (int pid:basket.getProducts().keySet()) {
            if(relatedProducts.contains(pid))
            {
                currentDiscountPrice +=  discount*basket.getProducts().get(pid)*basket.getPrices().get(pid);
            }
        }
        return currentDiscountPrice+ this.discountPolicy.calculateDiscount(basket);
    }


    public void setProducts(Collection<Integer> newProducts)
    {
        this.relatedProducts= new ArrayList<>();
        this.relatedProducts.addAll(newProducts);
    }
}
