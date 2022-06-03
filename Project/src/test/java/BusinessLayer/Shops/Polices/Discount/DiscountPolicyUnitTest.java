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

import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

public class DiscountPolicyUnitTest {

    private DiscountPolicy discountPolicy;
    @Mock
    private Basket basket;
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Before
    public void setUp() throws Exception {
        discountPolicy = new ProductDiscount(new ProductDiscount(new ProductDiscount(new ProductDiscount(new DefaultDiscount(), 1, 0.1), 2, 0.1), 3, 0.1), 4, 0.1);
    }

    @Test
    public void removeDiscountByIdInTheMiddle() {
        ConcurrentHashMap<Integer,Integer> pidquan = new ConcurrentHashMap<>();
        ConcurrentHashMap<Integer,Double> pidprice = new ConcurrentHashMap<>();
        pidquan.put(1,10);
        pidquan.put(3,10);
        pidprice.put(1,5.0);
        pidprice.put(3,5.0);
        when(basket.getProducts()).thenReturn(pidquan);
        when(basket.getPrices()).thenReturn(pidprice);
        int idRemove = 3;
        Assert.assertEquals(2 * 0.1 * 10 * 5, discountPolicy.calculateDiscount(basket), 0.1);
        Assert.assertEquals(4, discountPolicy.getDiscountId(), 0.1);
        DiscountPolicyInterface discountPolicyAfterRemove = discountPolicy.removeDiscountById(idRemove);
        Assert.assertEquals(discountPolicyAfterRemove, discountPolicy);
        Assert.assertEquals(0.1 * 10 * 5, discountPolicy.calculateDiscount(basket), 0.1);
        Assert.assertEquals(3, discountPolicy.getDiscountId(), 0.1);
    }

    @Test
    public void removeDiscountByIdInTheStart() {
        ConcurrentHashMap<Integer,Integer> pidquan = new ConcurrentHashMap<>();
        ConcurrentHashMap<Integer,Double> pidprice = new ConcurrentHashMap<>();
        pidquan.put(1,10);
        pidquan.put(4,10);
        pidprice.put(1,5.0);
        pidprice.put(4,5.0);
        when(basket.getProducts()).thenReturn(pidquan);
        when(basket.getPrices()).thenReturn(pidprice);
        int idRemove = 4;
        Assert.assertEquals(2 * 0.1 * 10 * 5, discountPolicy.calculateDiscount(basket), 0.1);
        Assert.assertEquals(4, discountPolicy.getDiscountId(), 0.1);
        DiscountPolicyInterface discountPolicyAfterRemove = discountPolicy.removeDiscountById(idRemove);
        Assert.assertNotEquals(discountPolicyAfterRemove, discountPolicy);
        Assert.assertNotEquals(0.1 * 10 * 5, discountPolicy.calculateDiscount(basket), 0.1);
        Assert.assertEquals(0.1 * 10 * 5, discountPolicyAfterRemove.calculateDiscount(basket), 0.1);
        Assert.assertEquals(3, discountPolicyAfterRemove.getDiscountId(), 0.1);
    }

    @Test
    public void removeDiscountByIdOutOfRange() {
        ConcurrentHashMap<Integer,Integer> pidquan = new ConcurrentHashMap<>();
        ConcurrentHashMap<Integer,Double> pidprice = new ConcurrentHashMap<>();
        pidquan.put(1,10);
        pidquan.put(4,10);
        pidprice.put(1,5.0);
        pidprice.put(4,5.0);
        when(basket.getProducts()).thenReturn(pidquan);
        when(basket.getPrices()).thenReturn(pidprice);

        Assert.assertEquals(2 * 0.1 * 10 * 5, discountPolicy.calculateDiscount(basket), 0.1);
        Assert.assertEquals(4, discountPolicy.getDiscountId(), 0.1);
        int idRemove = 0;
        try {
            DiscountPolicyInterface discountPolicyAfterRemove = discountPolicy.removeDiscountById(idRemove);
            fail("should throw");
        } catch (IllegalStateException e) {
            Assert.assertEquals(e.getMessage(), "can't delete the default or not exist discount");
            Assert.assertEquals(2 * 0.1 * 10 * 5, discountPolicy.calculateDiscount(basket), 0.1);
            Assert.assertEquals(4, discountPolicy.getDiscountId(), 0.1);
        }
        idRemove = discountPolicy.getDiscountId() + 1;
        try {
            DiscountPolicyInterface discountPolicyAfterRemove = discountPolicy.removeDiscountById(idRemove);
            fail("should throw");
        } catch (IllegalStateException e) {
            Assert.assertEquals(e.getMessage(), "can't delete the default or not exist discount");
            Assert.assertEquals(2 * 0.1 * 10 * 5, discountPolicy.calculateDiscount(basket), 0.1);
            Assert.assertEquals(4, discountPolicy.getDiscountId(), 0.1);
        }


    }
}
