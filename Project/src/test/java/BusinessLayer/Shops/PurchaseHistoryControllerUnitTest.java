package BusinessLayer.Shops;

import BusinessLayer.Users.SubscribedUser;
import BusinessLayer.Users.User;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PurchaseHistoryControllerUnitTest {
    private PurchaseHistoryController phc = PurchaseHistoryController.getInstance();
    private Shop s1;
    private SubscribedUser buyer;
    private SubscribedUser founder;
    private int shopId = 12030;
    private int otherShopId = 26565;

    @Before
    public void setUp(){
        s1 = mock(Shop.class);
        buyer = mock(SubscribedUser.class);
        founder = mock(SubscribedUser.class);
        phc.emptyDataOnPurchases();
        when(buyer.getUserName()).thenReturn("guy");
        when(founder.getUserName()).thenReturn("not guy");
        when(s1.getId()).thenReturn(shopId);
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
        int otherShopId = 29103;
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
}
