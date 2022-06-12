package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Products.Product;
import BusinessLayer.Shops.Shop;
import BusinessLayer.Users.Basket;
import BusinessLayer.Users.SubscribedUser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.*;

public class RelatedGroupDiscountTest {

    private RelatedGroupDiscount relatedGroupDiscount;

    private Basket basket;


    @Before
    public void setUp() throws Exception {

        basket = new Basket(1);

    }

    @Test
    public void calculateDiscountOneProductDiscount() {
        basket.saveProducts(1,10,5);
        basket.saveProducts(2,100,15);
        Collection<Integer> pids= new ArrayList<>();
        pids.add(1);
        relatedGroupDiscount= new RelatedGroupDiscount(pids,0.1);
        Assert.assertEquals(0.1*10*5,relatedGroupDiscount.calculateDiscount(basket),0.1);
    }

    @Test
    public void calculateDiscountDifferentProductDiscount() {
        basket.saveProducts(2,100,15);
        Collection<Integer> pids= new ArrayList<>();
        pids.add(1);
        relatedGroupDiscount= new RelatedGroupDiscount(pids,0.1);
        Assert.assertEquals(0,relatedGroupDiscount.calculateDiscount(basket),0.1);
    }

    @Test
    public void calculateDiscountMultipleProducts() {
        basket.saveProducts(1,10,5);
        basket.saveProducts(2,100,15);
        Collection<Integer> pids= new ArrayList<>();
        pids.add(1);
        pids.add(2);
        relatedGroupDiscount= new RelatedGroupDiscount(pids,0.1);
        Assert.assertEquals(0.1*10*5+0.1*15*100,relatedGroupDiscount.calculateDiscount(basket),0.1);
    }


}