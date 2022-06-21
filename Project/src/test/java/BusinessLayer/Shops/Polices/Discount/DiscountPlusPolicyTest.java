package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Users.Basket;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DiscountPlusPolicyTest {


    private DiscountPlusPolicy discountPlusPolicy;
    private DiscountOrPolicy discountOrPolicy;
    private DiscountPolicy shopDiscount;
    private DiscountPolicy shopDiscount2;
    private DiscountPred validateProductQuantityDiscount1;
    private DiscountPred validateProductQuantityDiscount2;

    private Basket basket;

    @Before
    public void setUp() throws Exception {

        basket = new Basket(1);
        basket.saveProducts(1,10,5,"meow");
        basket.saveProducts(2,100,15,"meow");
        shopDiscount= new ShopDiscount(5,0.1);
        shopDiscount2= new ProductDiscount(1,0.2);


//        Assert.assertEquals(0.1*(10*5+ 100*15),shopDiscount.calculateDiscount(basket),0.1);
    }

    @Test
    public void calculateDiscountWhenAllPredicateGood() {
        discountPlusPolicy = new  DiscountPlusPolicy(shopDiscount);
        Assert.assertEquals((10*5+100*15)*0.1,discountPlusPolicy.calculateDiscount(basket),0.1);
    }

    @Test
    public void calculateDiscountWhenOneWrong() {
        shopDiscount= new ShopDiscount(500,0.1);
        discountPlusPolicy = new  DiscountPlusPolicy(shopDiscount);
        discountPlusPolicy.add(shopDiscount2);
        Assert.assertEquals(10*5*0.2,discountPlusPolicy.calculateDiscount(basket),0.1);
    }

    @Test
    public void calculateDiscountWhenAllWrong() {
        shopDiscount= new ShopDiscount(500,0.1);
        shopDiscount2 = new ProductByQuantityDiscount(1,500,0.2);
        discountPlusPolicy = new  DiscountPlusPolicy(shopDiscount);
        discountPlusPolicy.add(shopDiscount2);
        Assert.assertEquals(0,discountPlusPolicy.calculateDiscount(basket),0.1);
    }


    @Test
    public void calculateDiscountInOrDiscountWrongAndOtherGood() {
        shopDiscount= new ShopDiscount(100,0.1);
        shopDiscount2 = new ProductByQuantityDiscount(1,500,0.2);
        discountPlusPolicy = new DiscountPlusPolicy(shopDiscount);
        ValidateBasketValueDiscount validateProductQuantityDiscount3= new ValidateBasketValueDiscount(100.0,false);
        ValidateProductQuantityDiscount validateProductQuantityDiscount4=new ValidateProductQuantityDiscount(2, 5, false) ;
        DiscountOrPolicy discountOrPolicy2 =new DiscountOrPolicy(validateProductQuantityDiscount3,shopDiscount2);
        discountOrPolicy2.add(validateProductQuantityDiscount4);
        discountPlusPolicy.add(discountOrPolicy2);
        Assert.assertEquals((10*5+100*15)*0.1,discountPlusPolicy.calculateDiscount(basket),0.1);

    }

    @Test
    public void calculateDiscountInOrDiscountWrong() {
        shopDiscount= new ShopDiscount(500,0.1);
        shopDiscount2 = new ProductByQuantityDiscount(1,500,0.2);
        discountPlusPolicy = new  DiscountPlusPolicy(shopDiscount);
        ValidateBasketValueDiscount validateProductQuantityDiscount3= new ValidateBasketValueDiscount(100.0,false);
        ValidateProductQuantityDiscount validateProductQuantityDiscount4=new ValidateProductQuantityDiscount(2, 5, false) ;
        DiscountOrPolicy discountOrPolicy2 =new DiscountOrPolicy(validateProductQuantityDiscount3,shopDiscount2);
        discountOrPolicy2.add(validateProductQuantityDiscount4);
        discountPlusPolicy.add(discountOrPolicy2);
        Assert.assertEquals(0,discountPlusPolicy.calculateDiscount(basket),0.1);

    }


    @Test
    public void calculateDiscountInAndDiscountGood() {
        discountPlusPolicy = new  DiscountPlusPolicy(shopDiscount);
        ValidateBasketValueDiscount validateProductQuantityDiscount3= new ValidateBasketValueDiscount(100.0,false);
        ValidateProductQuantityDiscount validateProductQuantityDiscount4=new ValidateProductQuantityDiscount(2, 5, false) ;
        DiscountAndPolicy discountAndPolicy =new DiscountAndPolicy(validateProductQuantityDiscount3,shopDiscount2);
        discountAndPolicy.add(validateProductQuantityDiscount4);
        discountPlusPolicy.add(discountAndPolicy);
        Assert.assertEquals((10*5+100*15)*0.1+10*5*0.2,discountPlusPolicy.calculateDiscount(basket),0.1);

    }

}