package BusinessLayer.Users;

import BusinessLayer.Products.Product;
import BusinessLayer.Shops.*;
import org.junit.*;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import static org.mockito.Mockito.*;

import javax.naming.NoPermissionException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class UserControllerUnitTest {

    private UserController uc = UserController.getInstance();

    @Mock
    private ShopController sc;

    @Mock
    private PurchaseHistoryController phc;

    @Mock
    private Shop s1;

    @Mock
    private Product p1;

    @Mock
    private SubscribedUser founder;

    @Mock
    private SubscribedUser loggedUser;

    @Mock
    private User user;

    @Mock
    private Basket basket;

    @Mock
    private ShopInfo s1_info;

    @Mock
    private BasketInfo basket_info;

    private Random rand = new Random();
    private int shopID;
    private int productID;
    private int userID;
    private String userName;
    private String passWord;
    private String userLog;
    private String passLog;

    @Rule public MockitoRule rule = MockitoJUnit.rule();

    @Before
    public void setUp() {
        shopID = rand.nextInt();
        productID = rand.nextInt();
        userID = rand.nextInt();

        userName = "SU" + userID;
        passWord = "PW" + userID;
        userLog = "User_" + userID;
        passLog = "Pass_" + userID;

        when(s1.getId()).thenReturn(shopID);
        when(p1.getID()).thenReturn(productID);
        ConcurrentHashMap<Integer, Basket> cart = createShoppingCart();
        when(user.getShoppingCart()).thenReturn(cart);
        when(user.getName()).thenReturn(userLog);

        uc.registerToSystem(userName, passWord);
        loggedUser = uc.login(userName, passWord, null);
    }


    @After
    public void tearDown() {
        uc.clearForTestsOnly();
    }

    @Test
    public void saveProducts() {
        when(user.saveProducts(s1.getId(), p1.getID(), 100)).thenReturn(true);
        try(MockedStatic<ShopController> mockedStatic1 = mockStatic(ShopController.class)) {
            mockedStatic1.when(ShopController::getInstance).thenReturn(sc);
            when(sc.checkIfUserHasBasket(s1.getId(), user.getName())).thenReturn(true);
            when(user.getBasket(s1.getId())).thenReturn(basket);
            when(sc.AddBasket(s1.getId(), user.getName(), basket)).thenReturn(true);
            Assert.assertTrue(uc.saveProducts(user, s1.getId(), p1.getID(), 100));
            Assert.assertTrue(sc.checkIfUserHasBasket(s1.getId(), user.getName()));
        }
    }

    @Test
    public void removeproduct() {
        uc.saveProducts(user, s1.getId(), p1.getID(), 100);
        when(user.removeProduct(s1.getId(), p1.getID())).thenReturn(true);
        uc.removeproduct(user, s1.getId(), p1.getID());
        when(basket.getProducts()).thenReturn(new ConcurrentHashMap<>());
        Assert.assertEquals(user.getShoppingCart().get(s1.getId()).getProducts().size(), 0);
    }

    private ConcurrentHashMap<Integer, Basket> createShoppingCart() {
        ConcurrentHashMap<Integer, Basket> res = new ConcurrentHashMap<>();
        res.put(s1.getId(), basket);
        return res;
    }

    @Test
    public void editProductQuantity() {
        uc.saveProducts(user, s1.getId(), p1.getID(), 100);
        when(user.editProductQuantity(s1.getId(), p1.getID(), 20)).thenReturn(true);
        ConcurrentHashMap<Integer, Integer> basket_products = createBasket();
        when(basket.getProducts()).thenReturn(basket_products);
        Assert.assertTrue(uc.editProductQuantity(user, s1.getId(), productID, 20));
        Assert.assertEquals(user.getShoppingCart().get(s1.getId()).getProducts().get(productID), 20, 0.0);
    }

    private ConcurrentHashMap<Integer, Integer> createBasket() {
        ConcurrentHashMap<Integer, Integer> res = new ConcurrentHashMap<>();
        res.put(p1.getID(), 20);
        return res;
    }

    @Test
    public void reciveInformation() {
        try(MockedStatic<ShopController> mockedStatic1 = mockStatic(ShopController.class)) {
            mockedStatic1.when(ShopController::getInstance).thenReturn(sc);
            ConcurrentHashMap<Integer, ShopInfo> info = createInformation();
            when(sc.reciveInformation()).thenReturn(info);
            when(s1.isOpen()).thenReturn(true);
            Assert.assertEquals(uc.reciveInformation().size(), 1);
        }
    }

    private ConcurrentHashMap<Integer, ShopInfo> createInformation() {
        ConcurrentHashMap<Integer, ShopInfo> res = new ConcurrentHashMap<>();
        res.put(s1.getId(), s1_info);
        return res;
    }

    @Test
    public void showCart() {
        saveProducts();
        uc.registerToSystem(user.getName(), "123456");
        ConcurrentHashMap<Integer, BasketInfo> cart = createBasketInfo();
        when(user.showCart()).thenReturn(cart);
        Assert.assertEquals(0, uc.showCart(user).size());
    }

    private ConcurrentHashMap<Integer, BasketInfo> createBasketInfo() {
        ConcurrentHashMap<Integer, BasketInfo> res = new ConcurrentHashMap<>();
        res.put(s1.getId(), basket_info);
        return res;
    }
}