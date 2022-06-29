package AcceptanceTests.Tests;

import AcceptanceTests.Bridge.*;
import AcceptanceTests.DataObjects.*;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class SystemManagerTests extends SubscribedUserTests{
    private SystemManager manager;
    private SubscribedUser user;
    private Guest g;
    private static Guest g1;
    private static final SystemManagerBridge managerBridge = new SystemManagerProxy((SubscribedUserProxy) subscribedUserBridge);

    @BeforeClass
    public static void setUpPurchase(){
        g1 = managerBridge.visit();
        managerBridge.addProductToCart(g1.name,shops[castro_ID].ID,2,10);
        managerBridge.addProductToCart(g1.name,shops[ACE_ID].ID,0,10);
        managerBridge.purchaseCart(g1.name, "4800470023456848", 674, 7, 2025);
        managerBridge.updateProductQuantity(castroFounder.name, shops[ACE_ID].ID,0,30);
        managerBridge.updateProductQuantity(castroFounder.name, shops[castro_ID].ID,2,30);
    }
    @Before
    public void setUpManager(){
        g = managerBridge.visit();
        user = managerBridge.login(g.name,new RegistrationInfo("Admin","ILoveIttaiNeria"));
        manager = managerBridge.manageSystemAsSystemManager(user.name);
    }

    @After
    public void tearDownManager(){
        Guest g =managerBridge.logout(manager.name);
        managerBridge.exit(g.name);
    }

    @Test
    public void testEnterAsManagerSuccess(){
        Guest guest = managerBridge.logout(manager.name);
        assertNotNull(guest);
        boolean exit = managerBridge.exit(guest.name);
        assertTrue(exit);
        g = managerBridge.visit();
        user = managerBridge.login(g.name,new RegistrationInfo("Admin","ILoveIttaiNeria"));
        manager = managerBridge.manageSystemAsSystemManager(user.name);
        assertNotNull(manager);
    }

    @Test
    public void testEnterAsManagerFailure() {
        Guest g = managerBridge.visit();
        boolean registered = managerBridge.register(g.name, new RegistrationInfo("bla bla", "123"));
        assertTrue(registered);
        AcceptanceTests.DataObjects.SubscribedUser user = managerBridge.login(g.name, new RegistrationInfo("bla bla", "123"));
        assertNotNull(user);
        SystemManager manager = managerBridge.manageSystemAsSystemManager(user.name);
        assertNull(manager);
        boolean exit = managerBridge.exit(u.name);
        assertTrue(exit);
    }

    @Test
    public void testGetShopsAndUsersInfoShopAndUserSuccess(){
        PurchaseHistoryInfo info = managerBridge.getShopsAndUsersInfo(manager.name,shops[castro_ID].ID, g1.name);
        assertNotNull(info);
        List<PurchaseHistory> historyList = info.historyInfo;
        assertEquals(historyList.size(), 1);
        PurchaseHistory history = historyList.get(0);
        Shop s = history.shop;
        String user = history.user;
        List<Purchase> purchases = history.purchases;
        assertEquals(s.ID, shops[castro_ID].ID);
        assertEquals(g1.name,user);
        assertEquals(1, purchases.size());
        Purchase purchase = purchases.get(0);
        assertNotNull(purchase);
        List<ProductInfo> infos = purchase.products;
        assertEquals(1, infos.size());
        ProductInfo productInfo = infos.get(0);
        assertNotNull(productInfo);
        assertEquals(productInfo.Id,2);
        assertEquals(productInfo.price, 30.0,0);
    }

    @Test
    public void testGetShopsAndUsersInfoShopAndUserFailure(){
        PurchaseHistoryInfo info = managerBridge.getShopsAndUsersInfo(manager.name,shops[castro_ID].ID, "bla bla");
        assertEquals(info.historyInfo.size(), 0);
    }

    @Test
    public void testGetShopsAndUsersUserInfoSuccess(){
        PurchaseHistoryInfo info = managerBridge.getShopsAndUsersInfo(manager.name);
        assertNotNull(info);
        List<PurchaseHistory> historyList = info.historyInfo;
        assertEquals(historyList.size(), 2);
        PurchaseHistory history = historyList.get(1);
        Shop s = history.shop;
        String user = history.user;
        List<Purchase> purchases = history.purchases;
        assertEquals(s.ID, shops[castro_ID].ID);
        assertEquals(g1.name,user);
        assertEquals(1, purchases.size());
        Purchase purchase = purchases.get(0);
        assertNotNull(purchase);
        List<ProductInfo> infos = purchase.products;
        assertEquals(1, infos.size());
        ProductInfo productInfo = infos.get(0);
        assertNotNull(productInfo);
        assertEquals(productInfo.Id,2);
        assertEquals(productInfo.price, 30.0,0);
        history = historyList.get(0);
        s = history.shop;
        user = history.user;
        purchases = history.purchases;
        assertEquals(s.ID, shops[ACE_ID].ID);
        assertEquals(g1.name,user);
        assertEquals(1, purchases.size());
        purchase = purchases.get(0);
        assertNotNull(purchase);
        infos = purchase.products;
        assertEquals(1, infos.size());
        productInfo = infos.get(0);
        assertNotNull(productInfo);
        assertEquals(productInfo.Id,0);
        assertEquals(productInfo.price, 20.0,0);
    }

    @Test
    public void testGetShopsAndUsersUserInfoFailure(){
        PurchaseHistoryInfo info = managerBridge.getShopsAndUsersInfo("not admin");
        assertNull(info);
    }

    @Test
    public void testGetShopsAndUsersInfoShopSuccess(){
        PurchaseHistoryInfo info = managerBridge.getShopsAndUsersInfo(manager.name,shops[castro_ID].ID);
        assertNotNull(info);
        List<PurchaseHistory> purchases = info.historyInfo;
        assertEquals(purchases.size(),1);
        PurchaseHistory history = purchases.get(0);
        assertNotNull(history);
        Shop s = history.shop;
        String username = history.user;
        List<Purchase> purchases1 = history.purchases;
        assertNotNull(s);
        assertNotNull(purchases1);
        assertEquals(1, purchases1.size());
        assertEquals(username,g1.name);
        Purchase purchase = purchases1.get(0);
        List<ProductInfo> products = purchase.products;
        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals(2,products.get(0).Id);
    }

    @Test
    public void testGetShopsAndUsersInfoShopFailure(){
        PurchaseHistoryInfo info = managerBridge.getShopsAndUsersInfo(manager.name,-1);
        assertEquals(info.historyInfo.size(),0);
    }

    @Test
    public void testRemoveSubscribedUserFailure(){
        boolean removed = managerBridge.removeSubscribedUserFromSystem(manager.name, "dwada");
        assertFalse(removed);
    }

    @Test
    public void testRemoveSubscribedUserSuccess(){
        Guest g = managerBridge.visit();
        managerBridge.register(g.name,new RegistrationInfo("bla","bla"));
        SubscribedUser u = managerBridge.login(g.name,new RegistrationInfo("bla","bla"));
        g = managerBridge.logout("bla");
        managerBridge.exit(g.name);
        boolean removed = managerBridge.removeSubscribedUserFromSystem(manager.name, "bla");
        assertTrue(removed);
        g = managerBridge.visit();
        u = managerBridge.login(g.name, new RegistrationInfo("bla","bla"));
        assertNull(u);
    }
}
