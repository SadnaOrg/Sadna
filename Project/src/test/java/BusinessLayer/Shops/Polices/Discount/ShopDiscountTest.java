package BusinessLayer.Shops.Polices.Discount;

import BusinessLayer.Products.Product;
import BusinessLayer.Shops.Shop;
import BusinessLayer.Users.Basket;
import BusinessLayer.Users.SubscribedUser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ShopDiscountTest {

    private ShopDiscount shopDiscount;

    private Basket basket;

    @Before
    public void setUp() throws Exception {

        basket = new Basket(1);
        basket.saveProducts(1,10,5);
        basket.saveProducts(2,100,15);
    }


    @Test
    public void calculateDiscountOneProductDiscount() {
        shopDiscount= new ShopDiscount(5,0.1);
        Assert.assertEquals(0.1*(10*5+ 100*15),shopDiscount.calculateDiscount(basket),0.1);
    }

    @Test
    public void calculateDiscountOneProductDiscountAndIgnoreOther() {
        shopDiscount= new ShopDiscount(200,0.2);
        Assert.assertEquals(0,shopDiscount.calculateDiscount(basket),0.1);
    }
    @Test
    public void calculateDiscountMultipleProducts() {
        shopDiscount= new ShopDiscount(50,0.2);
        Assert.assertEquals(0.2*10*5+0.2*15*100,shopDiscount.calculateDiscount(basket),0.1);
    }

}