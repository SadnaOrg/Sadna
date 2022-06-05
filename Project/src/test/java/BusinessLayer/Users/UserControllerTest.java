package BusinessLayer.Users;

import BusinessLayer.Products.Product;
import BusinessLayer.Shops.PurchaseHistoryController;
import BusinessLayer.Shops.Shop;
import BusinessLayer.Shops.ShopController;
import org.junit.*;

import javax.naming.NoPermissionException;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class UserControllerTest {

    private String admin ;
    private final UserController uc = UserController.getInstance();
    private final ShopController sc = ShopController.getInstance();
    private final PurchaseHistoryController phc = PurchaseHistoryController.getInstance();

    private Shop s1;
    private Product p1;
    private SubscribedUser founder;
    private SubscribedUser loggedUser;
    private User user;

    private final Random rand = new Random();
    private int shopID;
    private int productID;
    private String userName;
    private String passWord;
    private String userLog;
    private String passLog;
    int count = 0;

    @BeforeClass
    public static void beforeClass() throws Exception {
        var uc = UserController.getInstance();
    }

    @Before
    public void setUp() {
        shopID = count;
        productID = count;
        int userID = count++;

        createFounder();
        p1 = createProduct();
        s1 = createShop();

        sc.addShop(s1);

        user = createUser();
        userName = "_SU" + userID;
        passWord = "_PW" + userID;
        userLog = "_User_" + userID;
        passLog = "_Pass_" + userID;
        uc.registerToSystem(userName, passWord);

        loggedUser = uc.login(userName, passWord, null);
        admin = "admin_userController_Test"+count;
        uc.createSystemManager(admin,admin);

    }



    @After
    public void tearDown() {
        sc.getShops().clear();
        uc.clearForTestsOnly();
        phc.clearForTestsOnly();
    }

    static AtomicInteger count_ref = new AtomicInteger(0);
    @Test
    public void removeUser_Success(){

        var u = "user______"+(count_ref.incrementAndGet()); //for the cuncarency
        var sm =(SystemManager)uc.login(admin,admin,null);
        uc.registerToSystem(u,u);
        loggedUser=uc.login(u,u,null);
        assertTrue("user does not exist",uc.getSubscribedUserInfo(admin).get(UserController.UserState.LOGGED_IN).contains(loggedUser));
        assertTrue("dont removed user",uc.removeSubscribedUserFromSystem(sm,loggedUser.getName()));
        assertFalse("dont removed user",uc.getSubscribedUserInfo(admin).get(UserController.UserState.LOGGED_IN).contains(loggedUser));
    }

    @Test
    public void removeUser_Success_concurency(){

        var u = "user___con___"+(count_ref.incrementAndGet()); //for the cuncarency
        var flag = new AtomicBoolean(false);
        AtomicBoolean suc = new AtomicBoolean(true);
        uc.registerToSystem(u,u);
        loggedUser=uc.login(u,u,null);
        int num = 20;
        Thread[] thread = new Thread[num];
        boolean[] ans = new boolean[num];
        for (int i = 0; i < num; i++) {
            thread[i] = new Thread(()->{
                var admin = this.admin+"_con_"+(count_ref.incrementAndGet());
                uc.createSystemManager(admin,admin);
                var sm =(SystemManager)uc.login(admin,admin,null);
                try {
                    if (uc.removeSubscribedUserFromSystem(sm, loggedUser.getName()))
                        if (!flag.compareAndSet(false, true))
                            fail("more then one removed the user");
                }catch (Exception ignored){}

            });
            thread[i].start();
        }
        for (Thread t :thread) {
            try {
                t.join();
            } catch (Exception ignored) {
            }
        }
        assertTrue("no one can remove the user", flag.get());
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
        Assert.assertNull(user.getShoppingCart().get(s1.getId()));
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
        Assert.assertNotNull(uc.logout(userLog));
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

//    @Test
//    public void changeManagerPermission() {
//        //Assert.assertTrue(uc.changeManagerPermission());
//    }

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

    private void createFounder() {
        String username = "FounderName" + shopID;
        String password = "FounderPass" + shopID;
        uc.registerToSystem(username, password);
        founder = uc.login(username, password, null);
    }

    public Shop createShop() {
        Shop s2 = new Shop(shopID, "ShopName","testing shop", founder);
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