package BusinessLayer.Users;

import BusinessLayer.Products.Product;
import BusinessLayer.Products.ProductFilters;
import BusinessLayer.Products.Users.Basket;
import BusinessLayer.Products.Users.SubscribedUser;
import BusinessLayer.Products.Users.SystemManager;
import BusinessLayer.Products.Users.UserController;
import BusinessLayer.Shops.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.*;

public class SystemManagerTest {
    private static SystemManager manager = new SystemManager("Maor", "I am not Meir");
    private static PurchaseHistoryController phc = PurchaseHistoryController.getInstance();
    private static ShopController sc = ShopController.getInstance();
    private static Shop s1;
    private static SubscribedUser founder = new SubscribedUser("Guy Kish on", "I am not Guy Kishon");
    private static SubscribedUser buyer = new SubscribedUser("Guy Ki Shon", "I am also not Guy Kishon");
    private static final int shopId = 1561;
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
            basket.saveProducts(p1.getID(), 23,p1.getPrice());
            s1 = new Shop(shopId, "name of shop","testing shop", founder);
            s1.addBasket(buyer.getUserName(), basket);
            sc.addShop(s1);
            sc.addToPurchaseHistory(buyer.getUserName(), createPayments());
        }
        phc.emptyDataOnPurchases();
        flag = true;
    }

    public ConcurrentHashMap<Integer, Boolean> createPayments() {
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