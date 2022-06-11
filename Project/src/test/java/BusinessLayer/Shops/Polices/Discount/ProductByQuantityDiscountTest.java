package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Products.Product;
import BusinessLayer.Shops.Shop;
import BusinessLayer.Users.Basket;
import BusinessLayer.Users.SubscribedUser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ProductByQuantityDiscountTest {

    private ProductByQuantityDiscount productByQuantityDiscount;

    private Basket basket;


    @Before
    public void setUp() throws Exception {
        basket = new Basket(1);
    }

    @Test
    public void calculateDiscountOneProductDiscount() {
        basket.saveProducts(1,10,5);
        basket.saveProducts(2,100,15);
        productByQuantityDiscount= new ProductByQuantityDiscount(1,5,0.1);
        Assert.assertEquals(0.1*10*5,productByQuantityDiscount.calculateDiscount(basket),0.1);
    }

    @Test
    public void calculateDiscountDifferentProductDiscount() {
        basket.saveProducts(2,100,15);
        productByQuantityDiscount= new ProductByQuantityDiscount(1,5,0.1);
        Assert.assertEquals(0,productByQuantityDiscount.calculateDiscount(basket),0.1);
    }

    @Test
    public void calculateDiscountOneProductDiscountAndIgnoreOther() {
        basket.saveProducts(1,10,5);
        basket.saveProducts(2,100,15);
        productByQuantityDiscount= new ProductByQuantityDiscount(2,200,0.2);
        Assert.assertEquals(0,productByQuantityDiscount.calculateDiscount(basket),0.1);
    }

}