package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DiscountXorPolicyTest {



    private DiscountXorPolicy discountXorPolicy;

    private DiscountPolicy shopDiscount;
    private DiscountPolicy shopDiscount2;
    private DiscountPred validateProductQuantityDiscount1;
    private DiscountPred validateProductQuantityDiscount2;

    private Basket basket;

    @Before
    public void setUp() throws Exception {

        basket = new Basket(1);
        basket.saveProducts(1,10,5);
        basket.saveProducts(2,100,15);
        shopDiscount= new ShopDiscount(5,0.1);
        shopDiscount2= new ShopDiscount(100,0.2);


//        Assert.assertEquals(0.1*(10*5+ 100*15),shopDiscount.calculateDiscount(basket),0.1);
    }
    @Test
    public void calculateDiscountWhenAllPredicateIsTrueSoDr2() {
        validateProductQuantityDiscount1=new ValidateBasketQuantityDiscount(100, false) ;
        discountXorPolicy = new  DiscountXorPolicy(shopDiscount,shopDiscount2,validateProductQuantityDiscount1);
        Assert.assertEquals((10*5+100*15)*0.1,discountXorPolicy.calculateDiscount(basket),0.1);
    }
    @Test
    public void calculateDiscountWhenAllPredicateIsFalseSoDr2() {
        validateProductQuantityDiscount1=new ValidateBasketQuantityDiscount(100, true) ;
        discountXorPolicy = new  DiscountXorPolicy(shopDiscount,shopDiscount2,validateProductQuantityDiscount1);
        Assert.assertEquals((10*5+100*15)*0.2,discountXorPolicy.calculateDiscount(basket),0.1);
    }


    @Test
    public void calculateDiscountWhenAllWrong() {
        shopDiscount= new ShopDiscount(200,0.1);
        shopDiscount2= new ShopDiscount(200,0.2);
        validateProductQuantityDiscount1=new ValidateBasketQuantityDiscount(100, true) ;
        discountXorPolicy = new  DiscountXorPolicy(shopDiscount,shopDiscount2,validateProductQuantityDiscount1);
        Assert.assertEquals(0,discountXorPolicy.calculateDiscount(basket),0.1);

    }

    @Test
    public void calculateDiscountInOrDiscountWrong() {
        validateProductQuantityDiscount1=new ValidateProductQuantityDiscount(1, 5, true) ;
        validateProductQuantityDiscount2=new ValidateProductQuantityDiscount(2, 5, true) ;
        discountXorPolicy = new  DiscountXorPolicy(shopDiscount,shopDiscount2,validateProductQuantityDiscount1);
        discountXorPolicy.add(validateProductQuantityDiscount2);
        ValidateBasketValueDiscount validateProductQuantityDiscount3= new ValidateBasketValueDiscount(100.0,false);
        ValidateProductQuantityDiscount validateProductQuantityDiscount4=new ValidateProductQuantityDiscount(2, 5, false) ;
        DiscountOrPolicy discountOrPolicy2 =new DiscountOrPolicy(validateProductQuantityDiscount3,discountXorPolicy);
        discountOrPolicy2.add(validateProductQuantityDiscount4);
        Assert.assertEquals(0,discountOrPolicy2.calculateDiscount(basket),0.1);

    }


    @Test
    public void calculateDiscountInOrDiscountGood() {
        validateProductQuantityDiscount1=new ValidateProductQuantityDiscount(1, 5, false) ;
        validateProductQuantityDiscount2=new ValidateProductQuantityDiscount(2, 5, false) ;
        discountXorPolicy = new  DiscountXorPolicy(shopDiscount,shopDiscount2,validateProductQuantityDiscount1);
        discountXorPolicy.add(validateProductQuantityDiscount2);
        ValidateBasketValueDiscount validateProductQuantityDiscount3= new ValidateBasketValueDiscount(100.0,false);
        ValidateProductQuantityDiscount validateProductQuantityDiscount4=new ValidateProductQuantityDiscount(2, 5, false) ;
        DiscountAndPolicy discountAndPolicy =new DiscountAndPolicy(validateProductQuantityDiscount3,discountXorPolicy);
        discountAndPolicy.add(validateProductQuantityDiscount4);
        Assert.assertEquals((10*5+100*15)*0.1,discountAndPolicy.calculateDiscount(basket),0.1);

    }

}