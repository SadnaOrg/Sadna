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
    private final SubscribedUser founder = new SubscribedUser("Founder Guy","Guy123456");
    private Shop s1;
    private Product p1;
    private Product p2;

    @Before
    public void setUp() throws Exception {
        s1 = new Shop(100, "shop","testing shop", founder);
        p1 = new Product(1, "a", 5, 100);
        p2 = new Product(2, "c", 15, 500);
        s1.addProduct(p1);
        s1.addProduct(p2);
        basket = new Basket(1);
        basket.saveProducts(1,10,5);
        basket.saveProducts(2,100,15);
    }


    @Test
    public void calculateDiscountOneProductDiscount() {
        shopDiscount= new ShopDiscount(new DefaultDiscount(),5,0.1);
        Assert.assertEquals(0.1*(10*5+ 100*15),shopDiscount.calculateDiscount(basket),0.1);
    }

    @Test
    public void calculateDiscountOneProductDiscountAndIgnoreOther() {
        shopDiscount= new ShopDiscount(new DefaultDiscount(),200,0.2);
        Assert.assertEquals(0,shopDiscount.calculateDiscount(basket),0.1);
    }
    @Test
    public void calculateDiscountMultipleProducts() {
        shopDiscount= new ShopDiscount(new DefaultDiscount(),50,0.2);
        Assert.assertEquals(0.2*10*5+0.2*15*100,shopDiscount.calculateDiscount(basket),0.1);
    }

}