package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DiscountOrPolicyTest {



    private DiscountOrPolicy discountOrPolicy;

    private DiscountPolicy shopDiscount;
    private DiscountPred validateProductQuantityDiscount1;
    private DiscountPred validateProductQuantityDiscount2;

    private Basket basket;

    @Before
    public void setUp() throws Exception {

        basket = new Basket(1);
        basket.saveProducts(1,10,5);
        basket.saveProducts(2,100,15);
        shopDiscount= new ShopDiscount(5,0.1);

//        Assert.assertEquals(0.1*(10*5+ 100*15),shopDiscount.calculateDiscount(basket),0.1);
    }

    @Test
    public void calculateDiscountWhenAllPredicateGood() {
        validateProductQuantityDiscount1=new ValidateProductQuantityDiscount(1, 5, false) ;
        validateProductQuantityDiscount2=new ValidateProductQuantityDiscount(2, 5, false) ;
        discountOrPolicy = new  DiscountOrPolicy(validateProductQuantityDiscount1,shopDiscount);
        discountOrPolicy.add(validateProductQuantityDiscount2);
        Assert.assertEquals((10*5+100*15)*0.1,discountOrPolicy.calculateDiscount(basket),0.1);
    }

    @Test
    public void calculateDiscountWhenOneWrong() {
        validateProductQuantityDiscount1=new ValidateProductQuantityDiscount(1, 5, true) ;
        validateProductQuantityDiscount2=new ValidateProductQuantityDiscount(2, 5, false) ;
        discountOrPolicy = new  DiscountOrPolicy(validateProductQuantityDiscount1,shopDiscount);
        discountOrPolicy.add(validateProductQuantityDiscount2);
        Assert.assertEquals((10*5+100*15)*0.1,discountOrPolicy.calculateDiscount(basket),0.1);
    }

    @Test
    public void calculateDiscountWhenAllWrong() {
        validateProductQuantityDiscount1=new ValidateProductQuantityDiscount(1, 5, true) ;
        validateProductQuantityDiscount2=new ValidateProductQuantityDiscount(2, 5, true) ;
        discountOrPolicy = new  DiscountOrPolicy(validateProductQuantityDiscount1,shopDiscount);
        discountOrPolicy.add(validateProductQuantityDiscount2);
        Assert.assertEquals(0,discountOrPolicy.calculateDiscount(basket),0.1);
    }

    @Test
    public void calculateDiscountInOrDiscountWrong() {
        validateProductQuantityDiscount1=new ValidateProductQuantityDiscount(1, 5, true) ;
        validateProductQuantityDiscount2=new ValidateProductQuantityDiscount(2, 5, true) ;
        discountOrPolicy = new  DiscountOrPolicy(validateProductQuantityDiscount1,shopDiscount);
        discountOrPolicy.add(validateProductQuantityDiscount2);
        ValidateBasketValueDiscount validateProductQuantityDiscount3= new ValidateBasketValueDiscount(100.0,false);
        ValidateProductQuantityDiscount validateProductQuantityDiscount4=new ValidateProductQuantityDiscount(2, 5, false) ;
        DiscountOrPolicy discountOrPolicy2 =new DiscountOrPolicy(validateProductQuantityDiscount3,discountOrPolicy);
        discountOrPolicy2.add(validateProductQuantityDiscount4);
        Assert.assertEquals(0,discountOrPolicy2.calculateDiscount(basket),0.1);

    }


    @Test
    public void calculateDiscountInOrDiscountGood() {
        validateProductQuantityDiscount1=new ValidateProductQuantityDiscount(1, 5, false) ;
        validateProductQuantityDiscount2=new ValidateProductQuantityDiscount(2, 5, false) ;
        discountOrPolicy = new  DiscountOrPolicy(validateProductQuantityDiscount1,shopDiscount);
        discountOrPolicy.add(validateProductQuantityDiscount2);
        ValidateBasketValueDiscount validateProductQuantityDiscount3= new ValidateBasketValueDiscount(100.0,false);
        ValidateProductQuantityDiscount validateProductQuantityDiscount4=new ValidateProductQuantityDiscount(2, 5, false) ;
        DiscountAndPolicy discountAndPolicy =new DiscountAndPolicy(validateProductQuantityDiscount3,discountOrPolicy);
        discountAndPolicy.add(validateProductQuantityDiscount4);
        Assert.assertEquals((10*5+100*15)*0.1,discountAndPolicy.calculateDiscount(basket),0.1);

    }

}