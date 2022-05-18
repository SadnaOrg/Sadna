package BusinessLayer.Shops;

import BusinessLayer.Products.Product;
import BusinessLayer.Users.Basket;
import BusinessLayer.Users.SubscribedUser;
import BusinessLayer.Users.UserController;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;

public class PurchaseHistoryControllerTest {
    private static PurchaseHistoryController phc = PurchaseHistoryController.getInstance();
    private static ShopController sc = ShopController.getInstance();
    private static Shop s1;
    private static SubscribedUser founder = new SubscribedUser("Guy Kish on", "I am not Guy Kishon");
    private static SubscribedUser buyer = new SubscribedUser("Guy Ki Shon", "I am also not Guy Kishon");
    private static final int shopId = 1200;
    private static final int otherShopId = 12930;
    private static Product p1 = new Product(12090, "pord", 156.2, 45);
    private static Basket basket = new Basket(shopId);
    private static UserController uc = UserController.getInstance();
    private static boolean flag = false;
    @Before
    public void setUp(){
        if(!flag) {
            uc.registerToSystem(founder.getUserName(), "I am not Guy Kishon");
            uc.registerToSystem(buyer.getUserName(), "I am also not Guy Kishon");
            uc.login(buyer.getUserName(), "I am also not Guy Kishon", buyer);
            basket.saveProducts(p1.getID(), 23);
            s1 = new Shop(shopId, "name of shop", founder);
            s1.addBasket(buyer.getUserName(), basket);
            sc.addShop(s1);
            sc.addToPurchaseHistory(buyer.getUserName(), createPayments());
        }
        phc.emptyDataOnPurchases();
        flag = true;
    }

    @Test
    public void testGetPurchaseInfoSuccess() {
        phc.createPurchaseHistory(s1, buyer.getUserName());
        assertEquals(1, phc.getPurchaseInfo().size());
    }

    @Test
    public void testGetPurchaseInfoByIdSuccess() {
        phc.createPurchaseHistory(s1, buyer.getUserName());
        assertEquals(phc.getPurchaseInfo(shopId).size(), 1);
    }

    @Test
    public void testGetPurchaseInfoByShopFail_IncorrectShopId() {
        phc.createPurchaseHistory(s1, buyer.getUserName());
        assertEquals(phc.getPurchaseInfo(otherShopId).size(), 0);
    }

    @Test
    public void testGetPurchaseInfoByUser_Success() {
        phc.createPurchaseHistory(s1, buyer.getUserName());
        assertEquals(phc.getPurchaseInfo(buyer.getUserName()).size(), 1);
    }

    @Test
    public void testGetPurchaseInfoByUser_FailWrongUser() {
        phc.createPurchaseHistory(s1, buyer.getUserName());
        assertEquals(phc.getPurchaseInfo(founder.getUserName()).size(), 0);
    }

    @Test
    public void testGetPurchaseInfoByUserAndShopSuccess() {
        phc.createPurchaseHistory(s1, buyer.getUserName());
        assertEquals(phc.getPurchaseInfo(s1.getId(), buyer.getUserName()).size(), 1);
    }

    @Test
    public void testGetPurchaseInfoByUserAndShop_FailWrongUser() {
        phc.createPurchaseHistory(s1, buyer.getUserName());
        assertEquals(phc.getPurchaseInfo(s1.getId(), founder.getUserName()).size(), 0);
    }

    @Test
    public void testGetPurchaseInfoByUserAndShopFail_IncorrectShopId() {
        phc.createPurchaseHistory(s1, buyer.getUserName());
        assertEquals(phc.getPurchaseInfo(otherShopId, buyer.getUserName()).size(), 0);
    }

    @Test
    public void createPurchaseHistorySuccessOnlyOnce() {
        assertEquals(phc.getPurchaseInfo(s1.getId(), founder.getUserName()).stream().toList().size(), 0);
        PurchaseHistory purchaseHistory = phc.createPurchaseHistory(s1, founder.getUserName());
        assertEquals(purchaseHistory, phc.createPurchaseHistory(s1, founder.getUserName()));
    }

    public ConcurrentHashMap<Integer, Boolean> createPayments() {
        ConcurrentHashMap<Integer, Boolean> payments = new ConcurrentHashMap<>();
        payments.put(shopId, true);
        return payments;
    }
}
