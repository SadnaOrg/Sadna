package BusinessLayer.Users;

import BusinessLayer.Products.Product;
import BusinessLayer.Shops.*;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.*;

public class SystemManagerTest {
    private static SystemManager manager = new SystemManager("Maor", "I am not Meir",new Date(2001, Calendar.DECEMBER,1));
    private static PurchaseHistoryController phc = PurchaseHistoryController.getInstance();
    private static ShopController sc = ShopController.getInstance();
    private static Shop s1;
    private static SubscribedUser founder = new SubscribedUser("Guy Kish on", "I am not Guy Kishon",new Date(2001, Calendar.DECEMBER,1));
    private static SubscribedUser buyer = new SubscribedUser("Guy Ki Shon", "I am also not Guy Kishon",new Date(2001, Calendar.DECEMBER,1));
    private static final int shopId = 1561;
    private static final int otherShopId = 12930;
    private static Product p1 = new Product(12090, "pord", 156.2, 45);
    private static Basket basket = new Basket(shopId);
    private static UserController uc = UserController.getInstance();
    private static boolean flag = false;
    @BeforeClass
    public static void setUp(){
        if(!flag) {
            uc.registerToSystem(founder.getUserName(), "I am not Guy Kishon",new Date(2001, Calendar.DECEMBER,1));
            uc.registerToSystem(buyer.getUserName(), "I am also not Guy Kishon",new Date(2001, Calendar.DECEMBER,1));
            uc.login(buyer.getUserName(), "I am also not Guy Kishon", buyer);
            s1 = new Shop(shopId, "name of shop","testing shop", founder);
            s1.addProduct(p1);
            basket.saveProducts(p1.getID(), 23,p1.getPrice(),"meow");
            s1.addBasket(buyer.getUserName(), basket);
            sc.addShop(s1);
            sc.addToPurchaseHistory(buyer.getUserName(), createPayments());
        }
        phc.emptyDataOnPurchases();
        flag = true;
    }

    @After
    public void tearDown() throws Exception {
        phc.emptyDataOnPurchases();

    }

    public static ConcurrentHashMap<Integer, Boolean> createPayments() {
        ConcurrentHashMap<Integer, Boolean> payments = new ConcurrentHashMap<>();
        payments.put(shopId, true);
        return payments;
    }

    @Test
    public void testGetShopsAndUsersInfoSuccess() {
        phc.createPurchaseHistory(s1, buyer.getUserName());
        assertEquals(1, manager.getShopsAndUsersInfo().size());
    }

    @Test
    public void testGetShopsAndUsersInfoByIdSuccess() {
        phc.createPurchaseHistory(s1, buyer.getUserName());
        assertEquals(manager.getShopsAndUsersInfo(shopId).size(), 1);
    }

    @Test
    public void testGetShopsAndUsersInfoByShopFail_IncorrectShopId() {
        phc.createPurchaseHistory(s1, buyer.getUserName());
        assertEquals(manager.getShopsAndUsersInfo(otherShopId).size(), 0);
    }

    @Test
    public void testGetShopsAndUsersInfoByUser_Success() {
        phc.createPurchaseHistory(s1, buyer.getUserName());
        assertEquals(manager.getShopsAndUsersInfo(buyer.getUserName()).size(), 1);
    }

    @Test
    public void testGetShopsAndUsersInfoByUser_FailWrongUser() {
        phc.createPurchaseHistory(s1, buyer.getUserName());
        assertEquals(manager.getShopsAndUsersInfo(founder.getUserName()).size(), 0);
    }

    @Test
    public void testGetShopsAndUsersInfoByUserAndShopSuccess() {
        phc.createPurchaseHistory(s1, buyer.getUserName());
        assertEquals(manager.getShopsAndUsersInfo(s1.getId(), buyer.getUserName()).size(), 1);
    }

    @Test
    public void testGetShopsAndUsersInfoByUserAndShop_FailWrongUser() {
        phc.createPurchaseHistory(s1, buyer.getUserName());
        assertEquals(manager.getShopsAndUsersInfo(s1.getId(), founder.getUserName()).size(), 0);
    }

    @Test
    public void testGetShopsAndUsersInfoByUserAndShopFail_IncorrectShopId() {
        phc.createPurchaseHistory(s1, buyer.getUserName());
        assertEquals(manager.getShopsAndUsersInfo(otherShopId, buyer.getUserName()).size(), 0);
    }
}