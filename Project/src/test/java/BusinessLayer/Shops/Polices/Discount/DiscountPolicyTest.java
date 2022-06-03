package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DiscountPolicyTest {

    private DiscountPolicy discountPolicy;

    private Basket basket;
    @Before
    public void setUp() throws Exception {
        basket = new Basket(1);
        discountPolicy= new ProductDiscount(new ProductDiscount(new ProductDiscount(new ProductDiscount(new DefaultDiscount(),1,0.1),2,0.1),3,0.1),4,0.1);
    }
    @Test
    public void removeDiscountByIdInTheMiddle()
    {
        basket.saveProducts(3,10,5);
        basket.saveProducts(1,10,5);
        int idRemove =3;
        Assert.assertEquals(2*0.1*10*5,discountPolicy.calculateDiscount(basket),0.1);
        Assert.assertEquals(4,discountPolicy.getDiscountId(),0.1);
        DiscountPolicyInterface discountPolicyAfterRemove= discountPolicy.removeDiscountById(idRemove);
        Assert.assertEquals(discountPolicyAfterRemove,discountPolicy);
        Assert.assertEquals(0.1*10*5,discountPolicy.calculateDiscount(basket),0.1);
        Assert.assertEquals(3,discountPolicy.getDiscountId(),0.1);
    }

    @Test
    public void removeDiscountByIdInTheStart()
    {
        basket.saveProducts(4,10,5);
        basket.saveProducts(1,10,5);
        int idRemove =4;
        Assert.assertEquals(2*0.1*10*5,discountPolicy.calculateDiscount(basket),0.1);
        Assert.assertEquals(4,discountPolicy.getDiscountId(),0.1);
        DiscountPolicyInterface discountPolicyAfterRemove= discountPolicy.removeDiscountById(idRemove);
        Assert.assertNotEquals(discountPolicyAfterRemove,discountPolicy);
        Assert.assertNotEquals(0.1*10*5,discountPolicy.calculateDiscount(basket),0.1);
        Assert.assertEquals(0.1*10*5,discountPolicyAfterRemove.calculateDiscount(basket),0.1);
        Assert.assertEquals(3,discountPolicyAfterRemove.getDiscountId(),0.1);
    }
    @Test
    public void removeDiscountByIdOutOfRange()
    {
        basket.saveProducts(4,10,5);
        basket.saveProducts(1,10,5);

        Assert.assertEquals(2*0.1*10*5,discountPolicy.calculateDiscount(basket),0.1);
        Assert.assertEquals(4,discountPolicy.getDiscountId(),0.1);
        int idRemove =0;
        try {
            DiscountPolicyInterface discountPolicyAfterRemove = discountPolicy.removeDiscountById(idRemove);
            fail("should throw");
        }
        catch (IllegalStateException e)
        {
            Assert.assertEquals(e.getMessage(),"can't delete the default or not exist discount");
            Assert.assertEquals(2*0.1*10*5,discountPolicy.calculateDiscount(basket),0.1);
            Assert.assertEquals(4,discountPolicy.getDiscountId(),0.1);
        }
        idRemove =discountPolicy.getDiscountId()+1;
        try {
            DiscountPolicyInterface discountPolicyAfterRemove = discountPolicy.removeDiscountById(idRemove);
            fail("should throw");
        }
        catch (IllegalStateException e)
        {
            Assert.assertEquals(e.getMessage(),"can't delete the default or not exist discount");
            Assert.assertEquals(2*0.1*10*5,discountPolicy.calculateDiscount(basket),0.1);
            Assert.assertEquals(4,discountPolicy.getDiscountId(),0.1);
        }

    }
}