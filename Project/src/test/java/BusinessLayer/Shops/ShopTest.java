package BusinessLayer.Shops;

import BusinessLayer.Products.Product;
import BusinessLayer.Products.ProductFilters;
import BusinessLayer.Users.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.runners.statements.Fail;

import javax.naming.NoPermissionException;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ShopTest {

    private final SubscribedUser founder = new SubscribedUser("Founder Guy","Guy123456");
    private Shop s1;
    private Product p1;
    private Product p2;

    @Before
    public void setUp() {
        s1 = new Shop(100, "shop", founder);
        p1 = new Product(1, "a", 5, 100);
        p2 = new Product(2, "c", 15, 500);
    }

    @Test
    public void closeShopCheckWhenClosedAddProduct() {
        s1.addProduct(p1);
        assertTrue(s1.close());
        try {
            s1.addProduct(p2);
        }
        catch (IllegalStateException e)
        {
            Assert.assertEquals(e.getMessage(),"The shop is closed");
            assertTrue(s1.open());
            Assert.assertEquals(1, s1.getProducts().size());
            Assert.assertEquals(p1.getName(), s1.getProducts().get(p1.getID()).getName());

        }
    }


    @Test
    public void closeShopCheckWhenClosedChangeProduct() {
        s1.addProduct(p1);
        assertTrue(s1.close());
        try {
            p2 = new Product(1, "c", 15, 500);
            s1.changeProduct(p2);
        }
        catch (IllegalStateException e)
        {
            Assert.assertEquals(e.getMessage(),"The shop is closed");
            assertTrue(s1.open());
            Assert.assertEquals(p1.getName(),s1.getProducts().get(p2.getID()).getName());
        }
    }

    @Test
    public void closeShopCheckWhenClosedRemoveProduct() {
        s1.addProduct(p1);
        assertTrue(s1.close());
        try {
            s1.removeProduct(p1.getID());
        }
        catch (IllegalStateException e)
        {
            Assert.assertEquals(e.getMessage(),"The shop is closed");
            assertTrue(s1.open());
            Assert.assertEquals(1, s1.getProducts().size());
            Assert.assertEquals(p1.getName(), s1.getProducts().get(p1.getID()).getName());

        }
    }

    @Test
    public void closeShopCheckWhenClosedGetProducts() {
        s1.addProduct(p1);
        assertTrue(s1.close());
        try {
            s1.getProducts();
        }
        catch (IllegalStateException e)
        {
            Assert.assertEquals(e.getMessage(),"The shop is closed");
            assertTrue(s1.open());
            Assert.assertEquals(1, s1.getProducts().size());
            Assert.assertEquals(p1.getName(), s1.getProducts().get(p1.getID()).getName());

        }
    }


    @Test
    public void closeShopCheckWhenClosedSearchProducts() {
        s1.addProduct(p1);
        assertTrue(s1.close());
        ProductFilters productFilters = null;
        try {
            s1.searchProducts(productFilters);
        }
        catch (IllegalStateException e)
        {
            Assert.assertEquals(e.getMessage(),"The shop is closed");
            assertTrue(s1.open());
            Assert.assertEquals(1, s1.getProducts().size());
            Assert.assertEquals(p1.getName(), s1.getProducts().get(p1.getID()).getName());
        }
    }

    @Test
    public void closeShopCheckWhenClosedPurchaseBasket() {
        s1.addProduct(p1);
        User u1 = new Guest("Guy");
        assertTrue(s1.close());
        try {
            s1.purchaseBasket(u1.getName());
        }
        catch (IllegalStateException e)
        {
            Assert.assertEquals(e.getMessage(),"The shop is closed");
            assertTrue(s1.open());
            Assert.assertEquals(1, s1.getProducts().size());
            Assert.assertEquals(p1.getName(), s1.getProducts().get(p1.getID()).getName());
        }
    }

    @Test
    public void closeShopCheckWhenClosedCheckIfUserHasBasket() {
        s1.addProduct(p1);
        User u1 = new Guest("Guy");
        assertTrue(s1.close());
        try {
            s1.checkIfUserHasBasket(u1.getName());
        }
        catch (IllegalStateException e)
        {
            Assert.assertEquals(e.getMessage(),"The shop is closed");
            assertTrue(s1.open());
            Assert.assertEquals(1, s1.getProducts().size());
            Assert.assertFalse(s1.checkIfUserHasBasket(u1.getName()));
        }
    }
    @Test
    public void closeShopCheckWhenClosedGetUsersBaskets() {
        s1.addProduct(p1);
        User u1 = new Guest("Guy");
        Basket b1 = new Basket(s1.getId());
        s1.addBasket(u1.getName(),b1);
        Assert.assertEquals(1, s1.getUsersBaskets().size());
        assertTrue(s1.close());
        try {
            s1.getUsersBaskets();
        }
        catch (IllegalStateException e)
        {
            Assert.assertEquals(e.getMessage(),"The shop is closed");
            assertTrue(s1.open());
            Assert.assertEquals(1, s1.getUsersBaskets().size());
            Assert.assertEquals(b1, s1.getUsersBaskets().get(u1.getName()));
        }
    }

    @Test
    public void closeShopCheckWhenClosedAddBasket() {
        s1.addProduct(p1);
        User u1 = new Guest("Guy");
        Basket b1 = new Basket(s1.getId());
        assertTrue(s1.close());
        try {
            s1.addBasket(u1.getName(),b1);
        }
        catch (IllegalStateException e)
        {
            Assert.assertEquals(e.getMessage(),"The shop is closed");
            assertTrue(s1.open());
            Assert.assertEquals(0, s1.getUsersBaskets().size());
        }
    }

    @Test
    public void closeShopCheckWhenClosedAddAdministrator() {
        s1.addProduct(p1);
        SubscribedUser subscribedUser =new SubscribedUser("Guy","meirMaor");
        ShopAdministrator sa1 = new ShopAdministrator(s1,subscribedUser);
        assertTrue(s1.close());
        try {
            s1.addAdministrator(sa1.getUser().getUserName(),sa1);
        }
        catch (IllegalStateException e)
        {
            Assert.assertEquals(e.getMessage(),"The shop is closed");
            assertTrue(s1.open());
            Assert.assertEquals(1, s1.getShopAdministrators().size());
            Assert.assertNull(s1.getShopAdministrator(sa1.getUser().getUserName()));
        }
    }

    @Test
    public void addProductAndOtherProduct() {
        s1.addProduct(p1);
        Assert.assertEquals(1, s1.getProducts().size());
        s1.addProduct(p2);
        Assert.assertEquals(2, s1.getProducts().size());
        Assert.assertEquals(p1.getName(), s1.getProducts().get(p1.getID()).getName());
        Assert.assertNotEquals(p1.getName(), s1.getProducts().get(p2.getID()).getName());
        Assert.assertEquals(p2.getName(), s1.getProducts().get(p2.getID()).getName());
    }


    @Test
    public void addProductWhenProductIdAlreadyInTheShop() {
        s1.addProduct(p1);
        Product p3 = new Product(1, "fake", 50, 1000);
        Assert.assertEquals(1, s1.getProducts().size());
        try {
            s1.addProduct(p3);
        }
        catch (IllegalStateException e)
        {
            Assert.assertEquals(e.getMessage(),"The product is already in the shop");
            Assert.assertEquals(1, s1.getProducts().size());
            Assert.assertEquals(p1.getName(), s1.getProducts().get(p3.getID()).getName());
            Assert.assertNotEquals(p3.getName(), s1.getProducts().get(p3.getID()).getName());
        }
    }


    @Test
    public void removeProductSuccess() throws IllegalStateException {
        s1.addProduct(p1);
        s1.removeProduct(p1.getID());
        Assert.assertEquals(0, s1.getProducts().size());
    }
    @Test
    public void removeWrongProduct() throws IllegalStateException {
        s1.addProduct(p1);
        try {
            s1.removeProduct(p2.getID());
        } catch (Exception e) {
            Assert.assertEquals(1, s1.getProducts().size());
        }
    }
    @Test
    public void searchProducts() {
        s1.addProduct(p1);
        s1.addProduct(p2);
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
        s1.addProduct(p1);
        User u1 = new Guest("Yuval");
        u1.saveProducts(s1.getId(), p1.getID(), quantity);
        s1.addBasket(u1.getName(), u1.getBasket(s1.getId()));
        return s1.purchaseBasket(u1.getName());
    }
    @Test
    public void changeProductFail() {
        s1.addProduct(p1);
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
        s1.addProduct(p1);
        Product p2 =  new Product(1, "b", 10, 10);
        s1.changeProduct(p2);
        Assert.assertEquals(s1.getProducts().get(p2.getID()).getName(), p2.getName());
        Assert.assertEquals(s1.getProducts().get(p2.getID()).getPrice(), p2.getPrice(), 0.0);
        Assert.assertEquals(s1.getProducts().get(p2.getID()).getQuantity(), p2.getQuantity());
    }
}
