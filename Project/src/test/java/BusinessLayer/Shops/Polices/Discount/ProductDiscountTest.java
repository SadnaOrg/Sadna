package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Products.Product;
import BusinessLayer.Shops.Shop;
import BusinessLayer.Users.Basket;
import BusinessLayer.Users.SubscribedUser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ProductDiscountTest {

    private ProductDiscount productDiscount;

    private Basket basket;

    @Before
    public void setUp() throws Exception {

        basket = new Basket(1);

    }

    @Test
    public void calculateDiscountOneProductDiscount() {
        basket.saveProducts(1,10,5);
        basket.saveProducts(2,100,15);
        productDiscount= new ProductDiscount(new DefaultDiscount(),1,0.1);
        Assert.assertEquals(0.1*10*5,productDiscount.calculateDiscount(basket),0.1);
    }

    @Test
    public void calculateDiscountDifferentProductDiscount() {
        basket.saveProducts(2,100,15);
        productDiscount= new ProductDiscount(new DefaultDiscount(),1,0.1);
        Assert.assertEquals(0,productDiscount.calculateDiscount(basket),0.1);
    }

    @Test
    public void calculateDiscountMultipleProducts() {
        basket.saveProducts(1,10,5);
        basket.saveProducts(2,100,15);
        productDiscount= new ProductDiscount(new ProductDiscount(new DefaultDiscount(),1,0.1),2,0.2);
        Assert.assertEquals(0.1*10*5+0.2*15*100,productDiscount.calculateDiscount(basket),0.1);
    }
}