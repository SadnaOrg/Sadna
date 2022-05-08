package BusinessLayer.Shops;

import BusinessLayer.Products.Product;
import BusinessLayer.Products.ProductFilters;
import BusinessLayer.Users.Basket;
import BusinessLayer.Users.SubscribedUser;
import BusinessLayer.Users.UserController;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class ShopControllerTest {

    private ShopController sc = ShopController.getInstance();
    private UserController uc = UserController.getInstance();

    private Shop s1;
    private Basket basket;
    private Product p1;
    private SubscribedUser su;

    private Random rand = new Random();
    private int shopID;
    private int productID;
    private int userID;
    private String userName;
    private String passWord;

    @Before
    public void setUp() {
        shopID = rand.nextInt();
        productID = rand.nextInt();

        s1 = createShop();
        basket = createBasket();
        p1 = createProduct();

        sc.addShop(s1);

        userID = rand.nextInt();
        userName = "User" + userID;
        passWord = "password" + userID;
        uc.registerToSystem(userName, passWord);
        su = uc.login(userName, passWord, null);
    }

    @After
    public void tearDown() {
        removeShop();
        uc.logout(userName);
    }

    @Test
    public void addShop() {
        Assert.assertFalse(sc.addShop(s1));
        Assert.assertNotEquals(0, sc.getShops().size());
    }

    @Test
    public void addBasket() {
        Assert.assertTrue(sc.AddBasket(s1.getId(), userName, basket));
        Assert.assertNotEquals(0, sc.getShops().get(s1.getId()).getUsersBaskets().size());
    }

    @Test
    public void searchProducts() {
        ShopFilters shopPredicate = shop -> true;
        ProductFilters prodPredicate = product -> true;
        Assert.assertNotEquals(0, sc.searchProducts(shopPredicate, prodPredicate).size());
    }

    @Test
    public void purchaseBasket() {
        basket.saveProducts(productID, 10);
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
        Assert.assertTrue(sc.checkIfUserHasBasket(shopID, userName));
    }

    @Test
    public void checkIfUserHasBasketFailNoBasket() {
        Assert.assertFalse(sc.checkIfUserHasBasket(shopID, userName));
    }

    @Test
    public void reciveInformation() {
        ConcurrentHashMap<Integer, ShopInfo> res = sc.reciveInformation();
        Assert.assertEquals(s1.getId(), res.get(s1.getId()).getShopid());
        Assert.assertEquals(s1.getName(), res.get(s1.getId()).getShopname());
    }

    @Test
    public void openShop() {
        int old_size = sc.getShops().size();
        sc.openShop(su, "myShop" + su);
        Assert.assertEquals(sc.getShops().get(old_size).getName(), "myShop" + su);
    }

    public Shop createShop() {
        Shop s2 = new Shop(shopID, "ShopName", new SubscribedUser("User1", "Pass1"));
        s2.addProduct(createProduct());
        return s2;
    }

    public Basket createBasket() {
        return new Basket(s1.getId());
    }

    public Product createProduct() {
        return new Product(productID, "Product" + rand.nextInt(), 5.0, 1000);
    }

    public void removeShop() {
        sc.getShops().clear();
    }

    public ConcurrentHashMap<Integer, Boolean> createPayments() {
        ConcurrentHashMap<Integer, Boolean> payments = new ConcurrentHashMap<>();
        payments.put(shopID, true);
        return payments;
    }
}