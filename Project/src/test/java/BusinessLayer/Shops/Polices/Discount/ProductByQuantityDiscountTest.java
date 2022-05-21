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
    }

    @Test
    public void calculateDiscountOneProductDiscount() {
        basket.saveProducts(1,10,5);
        basket.saveProducts(2,100,15);
        productByQuantityDiscount= new ProductByQuantityDiscount(new DefaultDiscount(),1,5,0.1);
        Assert.assertEquals(0.1*10*5,productByQuantityDiscount.calculateDiscount(basket),0.1);
    }

    @Test
    public void calculateDiscountDifferentProductDiscount() {
        basket.saveProducts(2,100,15);
        productByQuantityDiscount= new ProductByQuantityDiscount(new DefaultDiscount(),1,5,0.1);
        Assert.assertEquals(0,productByQuantityDiscount.calculateDiscount(basket),0.1);
    }

    @Test
    public void calculateDiscountOneProductDiscountAndIgnoreOther() {
        basket.saveProducts(1,10,5);
        basket.saveProducts(2,100,15);
        productByQuantityDiscount= new ProductByQuantityDiscount(new ProductByQuantityDiscount(new DefaultDiscount(),1,5,0.1),2,200,0.2);
        Assert.assertEquals(0.1*10*5,productByQuantityDiscount.calculateDiscount(basket),0.1);
    }
    @Test
    public void calculateDiscountMultipleProducts() {
        basket.saveProducts(1,10,5);
        basket.saveProducts(2,100,15);
        productByQuantityDiscount= new ProductByQuantityDiscount(new ProductByQuantityDiscount(new DefaultDiscount(),1,5,0.1),2,50,0.2);
        Assert.assertEquals(0.1*10*5+0.2*15*100,productByQuantityDiscount.calculateDiscount(basket),0.1);
    }
}