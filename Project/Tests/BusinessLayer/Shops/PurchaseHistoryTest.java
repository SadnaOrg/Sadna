package BusinessLayer.Shops;

import BusinessLayer.Products.Product;
import BusinessLayer.Users.Basket;
import BusinessLayer.Users.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Mockito.when;

public class PurchaseHistoryTest {
    private PurchaseHistory purchaseHistory;
    @Mock
    private Shop s1;
    @Mock
    private Product p1;
    @Mock
    private Product p2;
    @Mock
    private User u1;
    @Mock
    private Basket basket;

    @Rule public MockitoRule rule= MockitoJUnit.rule();

    @Before
    public void setUp() {


    }

    @Test
    public void makePurchaseTest() {
        when(u1.getName()).thenReturn("Guy");
        when(s1.getUsersBaskets().get(u1.getName())).thenReturn(basket);
        purchaseHistory = new PurchaseHistory(s1,u1.getName());
        purchaseHistory.makePurchase();
        Assert.assertEquals(1, purchaseHistory.getPast_purchases().size());

    }

}