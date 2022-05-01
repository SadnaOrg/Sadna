package BusinessLayer.Shops;

import BusinessLayer.Products.Product;
import BusinessLayer.Users.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;

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
    }

    @Test
    public void changeProduct() {
        changeProductSuccess();
        changeProductFail();
    }

    @Test
    public void removeProduct() {
        Shop s1 = createShop();
        s1.addProduct(p1);
        s1.removeProduct(p1.getID());
        Assert.assertEquals(0, s1.getProducts().size());
    }

    @Test
    public void searchProducts() {
        Shop s = createShopWithTwoProducts();

    }

    @Test
    public void purchaseBasket() {
        Assert.assertNotEquals(purchaseBasketHelper(10), 0.0, 0.0);
        Assert.assertEquals(purchaseBasketHelper(1000), 0.0, 0.0);
    }

    private double purchaseBasketHelper(int quantity) {
        Shop s1 = createShop();
        s1.addProduct(p1);
        User u1 = new Guest("Yuval");
        u1.saveProducts(s1.getId(), p1.getID(), quantity);
        s1.addBasket(u1.getName(), u1.getBasket(s1.getId()));
        return s1.purchaseBasket(u1.getName());
    }

    private void changeProductFail() {
        Shop s1 = createShopWithProduct();
        Product p2 = createDifferentProduct();
        s1.changeProduct(p2);
        Assert.assertNotEquals(s1.getProducts().get(p2.getID()), p2);
    }

    private void changeProductSuccess() {
        Shop s1 = createShopWithProduct();
        Product p2 = createChangedProduct();
        s1.changeProduct(p2);
        Assert.assertEquals(getProductFromShop(s1, p2.getID()).getName(), p2.getName());
        Assert.assertEquals(getProductFromShop(s1, p2.getID()).getPrice(), p2.getPrice(), 0.0);
        Assert.assertEquals(getProductFromShop(s1, p2.getID()).getQuantity(), p2.getQuantity());
    }

    private Shop createShopWithProduct() {
        s1.addProduct(p1);
        return s1;
    }

    private Shop createShopWithTwoProducts() {
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
