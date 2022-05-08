package BusinessLayer.Shops;

import BusinessLayer.Products.Product;
import BusinessLayer.Products.ProductFilters;
import BusinessLayer.Users.Basket;
import BusinessLayer.Users.SubscribedUser;
import BusinessLayer.Users.UserController;
import org.junit.*;
import org.mockito.Mock;
import org.mockito.internal.junit.JUnitRule;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ShopControllerUnitTest {

    private ShopController sc = ShopController.getInstance();
    private UserController uc = UserController.getInstance();

    @Mock
    private Shop s1;

    @Mock
    private Basket basket;

    @Mock
    private Product p1;

    @Mock
    private SubscribedUser su;

    @Rule public MockitoRule rule = MockitoJUnit.rule();

    private Random rand = new Random();
    private int shopID;
    private int productID;
    private int userID;
    private String userName;

    @Before
    public void setUp() {
        shopID = rand.nextInt();
        productID = rand.nextInt();
        userID = rand.nextInt();
        userName = "User" + userID;
        sc.getShops().put(shopID, s1);

        when(s1.getId()).thenReturn(shopID);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void addBasket() {
        when(s1.addBasket(userName, basket)).thenReturn(true);
        Assert.assertTrue(sc.AddBasket(s1.getId(), userName, basket));
    }

    @Test
    public void purchaseBasket() {
        when(s1.addBasket(userName, basket)).thenReturn(true);
        when(s1.purchaseBasket(userName)).thenReturn(50.0);
        sc.AddBasket(shopID, userName, basket);
        Assert.assertEquals(50.0, sc.purchaseBasket(userName).get(shopID), 0.0);
    }

    @Test
    public void addToPurchaseHistory() {
        purchaseBasket();
        ConcurrentHashMap<Integer, Boolean> paymentSituation = createPayments();
        Assert.assertTrue(sc.addToPurchaseHistory(userName, paymentSituation));
    }

    @Test
    public void checkIfUserHasBasket() {
        addBasket();
        when(s1.checkIfUserHasBasket(userName)).thenReturn(true);
        Assert.assertTrue(sc.checkIfUserHasBasket(shopID, userName));
    }

    @Test
    public void checkIfUserHasBasketFailNoBasket() {
        when(s1.checkIfUserHasBasket(userName)).thenReturn(false);
        Assert.assertFalse(sc.checkIfUserHasBasket(shopID, userName));
        verify(s1).checkIfUserHasBasket(userName);
    }

    public ConcurrentHashMap<Integer, Boolean> createPayments() {
        ConcurrentHashMap<Integer, Boolean> payments = new ConcurrentHashMap<>();
        payments.put(shopID, true);
        return payments;
    }
}