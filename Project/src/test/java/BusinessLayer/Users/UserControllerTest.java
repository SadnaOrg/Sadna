package BusinessLayer.Users;

import BusinessLayer.Products.Product;
import BusinessLayer.Shops.PurchaseHistoryController;
import BusinessLayer.Shops.Shop;
import BusinessLayer.Shops.ShopController;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.naming.NoPermissionException;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.*;

public class UserControllerTest {

    private final UserController uc = UserController.getInstance();
    private final ShopController sc = ShopController.getInstance();
    private final PurchaseHistoryController phc = PurchaseHistoryController.getInstance();

    private Shop s1;
    private Product p1;
    private final SubscribedUser founder = new SubscribedUser("User1", "Pass1");
    private SubscribedUser loggedUser;
    private User user;

    private final Random rand = new Random();
    private int shopID;
    private int productID;
    private String userName;
    private String passWord;
    private String userLog;
    private String passLog;

    @Before
    public void setUp() {
        shopID = rand.nextInt();
        productID = rand.nextInt();
        int userID = rand.nextInt();

        p1 = createProduct();
        s1 = createShop();

        sc.addShop(s1);

        user = createUser();
        userName = "SU" + userID;
        passWord = "PW" + userID;
        userLog = "User_" + userID;
        passLog = "Pass_" + userID;
        uc.registerToSystem(userName, passWord);
        loggedUser = uc.login(userName, passWord, null);
    }


    @After
    public void tearDown() {
        sc.getShops().clear();
        uc.clearForTestsOnly();
        phc.clearForTestsOnly();
    }

    @Test
    public void saveProducts() {
        Assert.assertTrue(uc.saveProducts(user, s1.getId(), p1.getID(), 100));
        Assert.assertTrue(sc.checkIfUserHasBasket(s1.getId(), user.getName()));
    }

    @Test
    public void getShoppingCartClone() {
        uc.saveProducts(user, s1.getId(), p1.getID(), 100);
        Assert.assertEquals(uc.getShoppingCart(user.getUserName()).keySet(), uc.getShoppingCartClone(user).keySet());
    }

    @Test
    public void removeproduct() {
        uc.saveProducts(user, s1.getId(), p1.getID(), 100);
        uc.removeproduct(user, s1.getId(), p1.getID());
        Assert.assertEquals(user.getShoppingCart().get(s1.getId()).getProducts().size(), 0);
    }

    @Test
    public void editProductQuantity() {
        uc.saveProducts(user, s1.getId(), p1.getID(), 100);
        Assert.assertTrue(uc.editProductQuantity(user, s1.getId(), productID, 20));
        Assert.assertEquals(user.getShoppingCart().get(s1.getId()).getProducts().get(productID), 20, 0.0);
    }

    @Test
    public void reciveInformation() {
        Assert.assertEquals(uc.reciveInformation().size(), 1);
    }

    @Test
    public void showCart() {
        uc.saveProducts(user, s1.getId(), p1.getID(), 100);
        Assert.assertEquals(uc.showCart(user).size(), 1);
    }

    @Test
    public void assignShopManager() throws NoPermissionException {
        Assert.assertTrue(uc.assignShopManager(founder, s1.getId(), loggedUser.getName()));
        Assert.assertTrue(s1.getShopAdministrators().size() > 1);
    }

    @Test
    public void closeShop() throws NoPermissionException {
        Assert.assertTrue(uc.closeShop(founder, s1.getId()));
        Assert.assertEquals(uc.reciveInformation().size(), 0);
    }

    @Test
    public void createSystemManager() {
        uc.createSystemManager(userName, passWord);
        Assert.assertNotNull(uc.getSysUser(userName));
    }

    @Test
    public void registerToSystem() {
        Assert.assertTrue(uc.registerToSystem(userLog, passLog));
        Assert.assertNotNull(uc.getUser(userLog));
        Assert.assertNotNull(uc.getSubUser(userLog));
    }

    @Test
    public void login() {
        registerToSystem();
        SubscribedUser login_res = uc.login(userLog, passLog, null);
        Assert.assertNotNull(login_res);
        Assert.assertEquals(login_res.getName(), userLog);
        Assert.assertTrue(login_res.isLoggedIn());
    }

    @Test
    public void logout() {
        registerToSystem();
        SubscribedUser login_res = uc.login(userLog, passLog, null);
        Assert.assertTrue(uc.logout(userLog));
        Assert.assertFalse(login_res.isLoggedIn());
    }

    @Test
    public void loginSystem() {
        Assert.assertNotNull(uc.loginSystem());
    }

    @Test
    public void logoutSystem() {
        User login_system_res = uc.loginSystem();
        Assert.assertTrue(uc.logoutSystem(login_system_res.getName()));
    }

    @Test
    public void assignShopOwner() throws NoPermissionException {
        Assert.assertTrue(uc.assignShopOwner(founder, s1.getId(), loggedUser.getName()));
        Assert.assertTrue(s1.getShopAdministrators().size() > 1);
    }

    @Test
    public void changeManagerPermission() {
        //Assert.assertTrue(uc.changeManagerPermission());
    }

    @Test
    public void getAdministratorInfo() throws NoPermissionException {
        assignShopOwner();
        Assert.assertNotNull(uc.getAdministratorInfo(loggedUser, s1.getId()));
    }

    @Test
    public void getHistoryInfo() throws NoPermissionException {
        assignShopOwner();
        Assert.assertEquals(uc.getHistoryInfo(loggedUser, s1.getId()).size(), 0);
    }

    @Test
    public void getShopsAndUsersInfo() {
        createPrerequisitePurchase();
        assertEquals(1, uc.getShopsAndUsersInfo(uc.getSysUser(userName)).size());
    }

    @Test
    public void testGetShopsAndUsersInfo() {
        createPrerequisitePurchase();
        assertEquals(1, uc.getShopsAndUsersInfo(uc.getSysUser(userName), s1.getId()).size());
    }

    @Test
    public void testGetShopsAndUsersInfo1() {
        createPrerequisitePurchase();
        assertEquals(1, uc.getShopsAndUsersInfo(uc.getSysUser(userName), user.getName()).size());
    }

    @Test
    public void testGetShopsAndUsersInfo2() {
        createPrerequisitePurchase();
        assertEquals(1, uc.getShopsAndUsersInfo(uc.getSysUser(userName), s1.getId(), user.getName()).size());
    }

    private User createUser() {
        return uc.loginSystem();
    }

    public Shop createShop() {
        Shop s2 = new Shop(shopID, "ShopName", founder);
        s2.addProduct(p1);
        return s2;
    }

    private Product createProduct() {
        return new Product(productID, "Product" + rand.nextInt(), 5.0, 1000);
    }

    private ConcurrentHashMap<Integer, Boolean> createPayments() {
        ConcurrentHashMap<Integer, Boolean> payments = new ConcurrentHashMap<>();
        payments.put(s1.getId(), true);
        return payments;
    }

    private void createPrerequisitePurchase() {
        saveProducts();
        sc.purchaseBasket(user.getName());
        ConcurrentHashMap<Integer, Boolean> paymentSituation = createPayments();
        sc.addToPurchaseHistory(user.getName(), paymentSituation);
        createSystemManager();
    }
}