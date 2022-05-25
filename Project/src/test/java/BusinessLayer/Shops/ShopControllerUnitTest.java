package BusinessLayer.Shops;

import BusinessLayer.Products.Product;
import BusinessLayer.Users.Basket;
import BusinessLayer.Users.SubscribedUser;
import BusinessLayer.Users.UserController;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import static org.mockito.Mockito.*;

public class ShopControllerUnitTest {

    private ShopController sc = ShopController.getInstance();

    @Mock
    private UserController uc;

    @Mock
    private Shop s1;

    @Mock
    private Basket basket;

    @Mock
    private Product p1;

    @Mock
    private SubscribedUser su;

    @Mock
    private PurchaseHistoryController phc;

    private PurchaseHistory ph;

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
        ph = mock(PurchaseHistory.class);
    }

    @Test
    public void addBasket() {
        when(s1.addBasket(userName, basket)).thenReturn(true);
        Assert.assertTrue(sc.AddBasket(s1.getId(), userName, basket));
    }

    @Test
    public void purchaseBasket() {
        when(s1.addBasket(userName, basket)).thenReturn(true);
        when(s1.checkIfcanBuy(userName)).thenReturn(50.0);
        when(s1.checkIfUserHasBasket(userName)).thenReturn(true);
        sc.AddBasket(shopID, userName, basket);
        Assert.assertEquals(50.0, sc.purchaseBasket(userName).get(shopID), 0.0);
    }

    @Test
    public void addToPurchaseHistory() {
        purchaseBasket();
        ConcurrentHashMap<Integer, Boolean> paymentSituation = createPayments();

        try(MockedStatic<PurchaseHistoryController> mockedStatic1 = mockStatic(PurchaseHistoryController.class)) {
            mockedStatic1.when(PurchaseHistoryController::getInstance).thenReturn(phc);
            when(phc.createPurchaseHistory(s1, userName)).thenReturn(ph);
            doNothing().when(ph).makePurchase();

            ConcurrentHashMap<String, Basket> userBasket = createUserBasket();
            when(s1.getUsersBaskets()).thenReturn(userBasket);

            try (MockedStatic<UserController> mockedStatic2 = mockStatic(UserController.class)) {
                mockedStatic2.when(UserController::getInstance).thenReturn(uc);
                when(UserController.getInstance()).thenReturn(uc);
                ConcurrentHashMap<Integer, Basket> shoppingCart = createShoppingCart();
                when(uc.getShoppingCart(userName)).thenReturn(shoppingCart);
                Assert.assertTrue(sc.addToPurchaseHistory(userName, paymentSituation));
            }
        }
    }

    private ConcurrentHashMap<Integer, Basket> createShoppingCart() {
        ConcurrentHashMap<Integer, Basket> res = new ConcurrentHashMap<>();
        res.put(s1.getId(), new Basket(s1.getId()));
        return res;
    }

    private ConcurrentHashMap<String, Basket> createUserBasket() {
        ConcurrentHashMap<String, Basket> res = new ConcurrentHashMap<>();
        res.put(userName, new Basket(s1.getId()));
        return res;
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