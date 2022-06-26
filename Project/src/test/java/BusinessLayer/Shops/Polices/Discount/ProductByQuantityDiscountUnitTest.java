package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.concurrent.ConcurrentHashMap;

import static org.mockito.Mockito.when;

public class ProductByQuantityDiscountUnitTest {


    private ProductByQuantityDiscount productByQuantityDiscount;

    @Mock
    private Basket basket;
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Before
    public void setUp() throws Exception {
    }

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
        productByQuantityDiscount= new ProductByQuantityDiscount(1,5,0.1);
        Assert.assertEquals(0.1*10*5,productByQuantityDiscount.calculateDiscount(basket),0.1);
    }

    @Test
    public void calculateDiscountDifferentProductDiscount() {
        ConcurrentHashMap<Integer,Integer> pidquan = new ConcurrentHashMap<>();
        ConcurrentHashMap<Integer,Double> pidprice = new ConcurrentHashMap<>();

        pidquan.put(2,100);
        pidprice.put(2,15.0);
        when(basket.getProducts()).thenReturn(pidquan);
        when(basket.getPrices()).thenReturn(pidprice);
        productByQuantityDiscount= new ProductByQuantityDiscount(1,5,0.1);
        Assert.assertEquals(0,productByQuantityDiscount.calculateDiscount(basket),0.1);
    }

    @Test
    public void calculateDiscountOneProductDiscountAndIgnoreOther() {
        ConcurrentHashMap<Integer,Integer> pidquan = new ConcurrentHashMap<>();
        ConcurrentHashMap<Integer,Double> pidprice = new ConcurrentHashMap<>();
        pidquan.put(1,10);
        pidquan.put(2,100);
        pidprice.put(1,5.0);
        pidprice.put(2,15.0);
        when(basket.getProducts()).thenReturn(pidquan);
        when(basket.getPrices()).thenReturn(pidprice);

        productByQuantityDiscount= new ProductByQuantityDiscount(2,200,0.2);
        Assert.assertEquals(0,productByQuantityDiscount.calculateDiscount(basket),0.1);
    }
}
