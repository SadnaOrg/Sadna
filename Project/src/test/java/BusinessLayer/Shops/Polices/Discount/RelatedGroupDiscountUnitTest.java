package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import static org.mockito.Mockito.when;

public class RelatedGroupDiscountUnitTest {


    private RelatedGroupDiscount relatedGroupDiscount;
    @Mock
    private Basket basket;
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();



    @Test
    public void calculateDiscountOneProductDiscount() {
        ConcurrentHashMap<Integer,Integer> pidquan = new ConcurrentHashMap<>();
        ConcurrentHashMap<Integer,Double> pidprice = new ConcurrentHashMap<>();
        pidquan.put(1,10);
        pidquan.put(2,100);
        pidprice.put(1,5.0);
        pidprice.put(2,15.0);
        when(basket.getProducts()).thenReturn(pidquan);
        when(basket.getPrices()).thenReturn(pidprice);
        Collection<Integer> pids= new ArrayList<>();
        pids.add(1);
        relatedGroupDiscount= new RelatedGroupDiscount(pids,0.1);
        Assert.assertEquals(0.1*10*5,relatedGroupDiscount.calculateDiscount(basket),0.1);
    }

    @Test
    public void calculateDiscountDifferentProductDiscount() {
        ConcurrentHashMap<Integer,Integer> pidquan = new ConcurrentHashMap<>();
        ConcurrentHashMap<Integer,Double> pidprice = new ConcurrentHashMap<>();
        pidquan.put(2,100);
        pidprice.put(2,15.0);
        when(basket.getProducts()).thenReturn(pidquan);
        when(basket.getPrices()).thenReturn(pidprice);
        Collection<Integer> pids= new ArrayList<>();
        pids.add(1);
        relatedGroupDiscount= new RelatedGroupDiscount(pids,0.1);
        Assert.assertEquals(0,relatedGroupDiscount.calculateDiscount(basket),0.1);
    }

    @Test
    public void calculateDiscountMultipleProducts() {
        ConcurrentHashMap<Integer,Integer> pidquan = new ConcurrentHashMap<>();
        ConcurrentHashMap<Integer,Double> pidprice = new ConcurrentHashMap<>();
        pidquan.put(1,10);
        pidquan.put(2,100);
        pidprice.put(1,5.0);
        pidprice.put(2,15.0);
        when(basket.getProducts()).thenReturn(pidquan);
        when(basket.getPrices()).thenReturn(pidprice);
        Collection<Integer> pids= new ArrayList<>();
        pids.add(1);
        pids.add(2);
        relatedGroupDiscount= new RelatedGroupDiscount(pids,0.1);
        Assert.assertEquals(0.1*10*5+0.1*15*100,relatedGroupDiscount.calculateDiscount(basket),0.1);
    }

}
