package BusinessLayer.Shops;

import BusinessLayer.Products.Product;
import BusinessLayer.Users.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.runners.statements.Fail;

import javax.naming.NoPermissionException;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.fail;

public class ShopTest {

    private final SubscribedUser founder = createFounder();
    private Shop s1;
    private Product p1;
    private Product p2;

    @Before
    public void setUp() {
        s1 = createShop();
        p1 = createProduct();
        p2 = createDifferentProduct();
    }

    @Test
    public void addProduct() {
        Shop s1 = createShopWithProduct();
        Assert.assertEquals(1, s1.getProducts().size());
        try {
            s1.addProduct(p1);
        }
        catch (Exception e)
        {
            Assert.assertEquals(1, s1.getProducts().size());
        }
    }


    @Test
    public void removeProductSuccess() throws IllegalStateException {
        Shop s1 = createShopWithProduct();
        s1.removeProduct(p1.getID());
        Assert.assertEquals(0, s1.getProducts().size());
    }
    @Test
    public void removeWrongProduct() throws IllegalStateException {
        Shop s1 = createShopWithProduct();
        try {
            s1.removeProduct(p2.getID());
        } catch (Exception e) {
            Assert.assertEquals(1, s1.getProducts().size());
        }
    }
    @Test
    public void searchProducts() {
        Shop s = createShopWithTwoProducts();
        //TODO: Yuval u didnt write test;
    }

    @Test
    public void purchaseBasketSuccess() {
        Assert.assertNotEquals(purchaseBasketHelper(10), 0.0, 0.0);
    }
    @Test
    public void purchaseBasketFail() {
        try {
            purchaseBasketHelper(1000);
            fail("the product is out of stock shouldn't work");
        }
        catch (Exception e)
        {
            Assert.assertTrue(1000>s1.getProducts().get(p1.getID()).getQuantity());
        }
    }

    private double purchaseBasketHelper(int quantity) throws IllegalStateException {
        Shop s1 = createShopWithProduct();
        User u1 = new Guest("Yuval");
        u1.saveProducts(s1.getId(), p1.getID(), quantity);
        s1.addBasket(u1.getName(), u1.getBasket(s1.getId()));
        return s1.purchaseBasket(u1.getName());
    }
    @Test
    public void changeProductFail() {
        Shop s1 = createShopWithProduct();
        Product p2 = createDifferentProduct();
        try {
            s1.changeProduct(p2);
        }
        catch (Exception e)
        {
            Assert.assertNull(s1.getProducts().get(p2.getID()));
        }
    }

    @Test
    public void changeProductSuccess() {
        Shop s1 = createShopWithProduct();
        Product p2 = createChangedProduct();
        s1.changeProduct(p2);
        Assert.assertEquals(getProductFromShop(s1, p2.getID()).getName(), p2.getName());
        Assert.assertEquals(getProductFromShop(s1, p2.getID()).getPrice(), p2.getPrice(), 0.0);
        Assert.assertEquals(getProductFromShop(s1, p2.getID()).getQuantity(), p2.getQuantity());
    }


    private Shop createShopWithProduct() throws IllegalStateException {
        s1.addProduct(p1);
        return s1;
    }

    private Shop createShopWithTwoProducts() throws IllegalStateException {
        s1.addProduct(p1);
        s1.addProduct(p2);
        return s1;
    }

    private Shop createShop() {
        return new Shop(100, "shop", founder);
    }

    private Product createProduct() {
        return new Product(1, "a", 5, 100);
    }

    private Product createChangedProduct() {
        return new Product(1, "b", 10, 10);
    }

    private Product createDifferentProduct() {
        return new Product(2, "c", 15, 500);
    }

    private Product getProductFromShop(Shop s1, int id) {
        return s1.getProducts().get(id);
    }

    private User createUserWithItemInBasket(int shopid, int productid, int quantity)
    {
        User u = new Guest("Yuval");
        u.saveProducts(shopid, productid, quantity);
        return u;
    }

    private SubscribedUser createFounder() {
        return new SubscribedUser("Founder Guy","Guy123456");
    }
}