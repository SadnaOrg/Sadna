package BusinessLayer.Users;

import BusinessLayer.Shops.PurchaseHistory;
import BusinessLayer.Shops.PurchaseHistoryController;
import BusinessLayer.Shops.Shop;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;

import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class SystemManagerUnitTest {
    private PurchaseHistoryController phc;
    private UserController uc;
    private Shop s1;
    private SystemManager manager = new SystemManager("Unit Maor", "Unit not Meir");
    private SubscribedUser buyer;
    private SubscribedUser founder;
    private final int shopId = 1561;
    private final int otherShopId = 12930;
    private Collection<PurchaseHistory> histories;

    @Before
    public void setUp(){
        uc = mock(UserController.class);
        phc = mock(PurchaseHistoryController.class);
        buyer = mock(SubscribedUser.class);
        founder = mock(SubscribedUser.class);
        s1 = mock(Shop.class);
        when(phc.createPurchaseHistory(any(Shop.class), anyString())).thenReturn(null);
        when(buyer.getUserName()).thenReturn("Best Test Writer in the world");
        when(founder.getUserName()).thenReturn("2nd best Test Writer in the world");
        when(s1.getId()).thenReturn(5456);
        phc.createPurchaseHistory(s1, buyer.getUserName());
        histories = mock(Collection.class);
    }

    @Test
    public void testGetShopsAndUsersInfoSuccess() {
        try(MockedStatic<PurchaseHistoryController> mockedStatic = mockStatic(PurchaseHistoryController.class)) {
            mockedStatic.when(PurchaseHistoryController::getInstance).thenReturn(phc);
            when(phc.getPurchaseInfo()).thenReturn(histories);
            when(histories.size()).thenReturn(1);
            assertEquals(1, manager.getShopsAndUsersInfo().size());
        }
    }

    @Test
    public void testGetShopsAndUsersInfoByIdSuccess() {
        try(MockedStatic<PurchaseHistoryController> mockedStatic = mockStatic(PurchaseHistoryController.class)) {
            mockedStatic.when(PurchaseHistoryController::getInstance).thenReturn(phc);
            when(phc.getPurchaseInfo(shopId)).thenReturn(histories);
            when(histories.size()).thenReturn(1);
            assertEquals(manager.getShopsAndUsersInfo(shopId).size(), 1);
        }
    }

    @Test
    public void testGetShopsAndUsersInfoByShopFail_IncorrectShopId() {
        try(MockedStatic<PurchaseHistoryController> mockedStatic = mockStatic(PurchaseHistoryController.class)) {
            mockedStatic.when(PurchaseHistoryController::getInstance).thenReturn(phc);
            when(phc.getPurchaseInfo(otherShopId)).thenReturn(histories);
            when(histories.size()).thenReturn(0);
            assertNotEquals(manager.getShopsAndUsersInfo(otherShopId).size(), 1);
        }
    }

    @Test
    public void testGetShopsAndUsersInfoByUser_Success() {
        try(MockedStatic<PurchaseHistoryController> mockedStatic = mockStatic(PurchaseHistoryController.class)) {
            mockedStatic.when(PurchaseHistoryController::getInstance).thenReturn(phc);
            when(phc.getPurchaseInfo(buyer.getUserName())).thenReturn(histories);
            when(histories.size()).thenReturn(1);
            assertEquals(manager.getShopsAndUsersInfo(buyer.getUserName()).size(), 1);
        }
    }

    @Test
    public void testGetShopsAndUsersInfoByUser_FailWrongUser() {
        try(MockedStatic<PurchaseHistoryController> mockedStatic = mockStatic(PurchaseHistoryController.class)) {
            mockedStatic.when(PurchaseHistoryController::getInstance).thenReturn(phc);
            when(phc.getPurchaseInfo(founder.getUserName())).thenReturn(histories);
            when(histories.size()).thenReturn(0);
            assertEquals(manager.getShopsAndUsersInfo(founder.getUserName()).size(), 0);
        }
    }

    @Test
    public void testGetShopsAndUsersInfoByUserAndShopSuccess() {
        try(MockedStatic<PurchaseHistoryController> mockedStatic = mockStatic(PurchaseHistoryController.class)) {
            mockedStatic.when(PurchaseHistoryController::getInstance).thenReturn(phc);
            when(phc.getPurchaseInfo(s1.getId(), buyer.getUserName())).thenReturn(histories);
            when(histories.size()).thenReturn(1);
            assertEquals(manager.getShopsAndUsersInfo(s1.getId(), buyer.getUserName()).size(), 1);
        }
    }

    @Test
    public void testGetShopsAndUsersInfoByUserAndShop_FailWrongUser() {
        try(MockedStatic<PurchaseHistoryController> mockedStatic = mockStatic(PurchaseHistoryController.class)) {
            mockedStatic.when(PurchaseHistoryController::getInstance).thenReturn(phc);
            when(phc.getPurchaseInfo(s1.getId(), founder.getUserName())).thenReturn(histories);
            when(histories.size()).thenReturn(0);
            assertEquals(manager.getShopsAndUsersInfo(s1.getId(), founder.getUserName()).size(), 0);
        }
    }

    @Test
    public void testGetShopsAndUsersInfoByUserAndShopFail_IncorrectShopId() {
        try(MockedStatic<PurchaseHistoryController> mockedStatic = mockStatic(PurchaseHistoryController.class)) {
            mockedStatic.when(PurchaseHistoryController::getInstance).thenReturn(phc);
            when(phc.getPurchaseInfo(otherShopId, buyer.getUserName())).thenReturn(histories);
            when(histories.size()).thenReturn(0);
            assertEquals(manager.getShopsAndUsersInfo(otherShopId, buyer.getUserName()).size(), 0);
        }
    }

    @Test
    public void removeSubscribedUser_sucsses() {
        try(MockedStatic<UserController> mockedStatic = mockStatic(UserController.class)) {
            mockedStatic.when(UserController::getInstance).thenReturn(uc);
            when(uc.removeSubscribedUserFromSystem( buyer.getUserName())).thenReturn(true);
            Assert.assertTrue(manager.removeSubscribedUser(buyer.getUserName()));
        }
    }

    @Test
    public void removeSubscribedUser_fail() {
        try(MockedStatic<UserController> mockedStatic = mockStatic(UserController.class)) {
            mockedStatic.when(UserController::getInstance).thenReturn(uc);
            when(uc.removeSubscribedUserFromSystem( buyer.getUserName())).thenReturn(false);
            Assert.assertFalse(manager.removeSubscribedUser(buyer.getUserName()));
        }
    }
}
