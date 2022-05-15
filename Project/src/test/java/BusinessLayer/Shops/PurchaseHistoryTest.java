package BusinessLayer.Shops;

import BusinessLayer.Products.Product;
import BusinessLayer.Users.Basket;
import BusinessLayer.Users.Guest;
import BusinessLayer.Users.SubscribedUser;
import BusinessLayer.Users.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.*;

import static org.mockito.Mockito.when;

public class PurchaseHistoryTest {
    private PurchaseHistory purchaseHistory;
    private final SubscribedUser founder = new SubscribedUser("Founder Guy","Guy123456");

    private Shop s1;
    private Product p1;
    private Product p2;
    private User u1;
    private Basket basket;

    @Before
    public void setUp() {
        s1 = new Shop(100, "shop", founder);
        p1 = new Product(1, "a", 5, 100);
        p2 = new Product(2, "c", 15, 500);
    }
    @Test
    public void makePurchaseTest() {
        s1.addProduct(p1);
        s1.addProduct(p2);
        User u1 = new Guest("Yuval");
        Basket b = new Basket(s1.getId());
        b.saveProducts(p1.getID(),10);
        b.saveProducts(p2.getID(),50);
        s1.addBasket(u1.getName(), b);
        purchaseHistory = new PurchaseHistory(s1,u1.getName());
        Assert.assertEquals(0, purchaseHistory.getPast_purchases().size());
        purchaseHistory.makePurchase();
        Assert.assertEquals(1, purchaseHistory.getPast_purchases().size());

    }
}
