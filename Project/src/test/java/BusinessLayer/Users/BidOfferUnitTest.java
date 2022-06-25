package BusinessLayer.Users;

import BusinessLayer.Products.Product;
import BusinessLayer.Shops.Shop;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BidOfferUnitTest {


    private BidOffer bidOffer;
    @Mock
    private SubscribedUser founder;
    @Mock
    private SubscribedUser subscribedUser1;
    @Mock
    private Shop s1;
    @Mock
    private Product p1;
    @Mock
    private Product p2;
  @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Before
    public void setUp() throws Exception {
        when(founder.getUserName()).thenReturn("Founder Guy");
        when(subscribedUser1.getUserName()).thenReturn("meir maor");
        when(s1.getId()).thenReturn(100);
        when(p1.getID()).thenReturn(1);
        when(p2.getID()).thenReturn(2);
        when(p1.getPrice()).thenReturn(5.0);
        when(p2.getPrice()).thenReturn(15.0);
        User u = mock(User.class);
        when(u.getUserName()).thenReturn("mock user");
        bidOffer= new BidOffer(s1.getId(),u);
    }
    @Test
    public void AddTOBidOnceGoodThenAddSame() {
        bidOffer.AddToBid(p1.getID(),10,4);
        assertEquals(1, bidOffer.getProducts().size());
        try {
            bidOffer.AddToBid(p1.getID(), 10, 4);
            fail("shouldn't get her");
        }
        catch (IllegalStateException e)
        {
            assertEquals(e.getMessage(),"The product is already in the basket");
            assertEquals(1, bidOffer.getProducts().size());
        }
        assertEquals(1, bidOffer.getApprovals().size());
        assertEquals(10, bidOffer.getApprovals().get(p1.getID()).getQuantity());

    }

    @Test
    public void setApprovalOnceGoodThenAddSame() {
        bidOffer.AddToBid(p1.getID(),10,4);
        assertEquals(1, bidOffer.getApprovals().size());
        List<String> admins = new ArrayList<>();
        admins.add(founder.getUserName());
        admins.add(subscribedUser1.getUserName());
        bidOffer.setApproval(p1.getID(),admins);
        assertNotNull(bidOffer.getApprovals().get(p1.getID()));
        try {
            bidOffer.setApproval(p1.getID(),admins);
            fail("shouldn't get her");
        }
        catch (IllegalStateException e)
        {
            assertEquals(e.getMessage(),"The product is already have an approval");
            assertEquals(1, bidOffer.getProducts().size());

            var x =bidOffer.getApprovals().get(p1.getID()).getAdminsNames();
            assertTrue(admins.containsAll(x)&& x.containsAll(admins));
        }
    }
    @Test
    public void removeProductOnceGoodThenAddSame() {
        bidOffer.AddToBid(p1.getID(),10,4);
        assertEquals(1, bidOffer.getProducts().size());
        assertEquals(1, bidOffer.getApprovals().size());
        List<String> admins = new ArrayList<>();
        admins.add(founder.getUserName());
        admins.add(subscribedUser1.getUserName());
        bidOffer.setApproval(p1.getID(),admins);
        assertEquals(1, bidOffer.getApprovals().size());
        assertNotNull(bidOffer.getApprovals().get(p1.getID()));
        try {
            bidOffer.removeProduct(p2.getID());
            fail("shouldn't get her");
        }
        catch (IllegalStateException e)
        {
            assertEquals(e.getMessage(),"The product is not exist in the bid");
            assertEquals(1, bidOffer.getProducts().size());
            assertEquals(1, bidOffer.getApprovals().size());
        }
        assertTrue(bidOffer.removeProduct(p1.getID()));
        assertEquals(0, bidOffer.getProducts().size());
        assertEquals(0, bidOffer.getApprovals().size());

        try {
            bidOffer.removeProduct(p1.getID());
            fail("shouldn't get her");
        }
        catch (IllegalStateException e)
        {
            assertEquals(e.getMessage(),"The product is not exist in the bid");
            assertEquals(0, bidOffer.getProducts().size());
            assertEquals(0, bidOffer.getApprovals().size());
        }
    }

    @Test
    public void editProductPriceWrongPrice() {
        bidOffer.AddToBid(p1.getID(), 10, 4);
        assertEquals(1, bidOffer.getProducts().size());
        assertEquals(1, bidOffer.getApprovals().size());
        List<String> admins = new ArrayList<>();
        admins.add(founder.getUserName());
        admins.add(subscribedUser1.getUserName());
        bidOffer.setApproval(p1.getID(), admins);
        assertEquals(1, bidOffer.getApprovals().size());
        assertNotNull(bidOffer.getApprovals().get(p1.getID()));
        try {
            bidOffer.editProductPrice(p1.getID(), -1);
            fail("shouldn't get her");
        } catch (IllegalStateException e) {
            assertEquals(e.getMessage(), "a product can't appear in a bid with a negative price!");
            assertEquals(1, bidOffer.getApprovals().size());
        }
    }
    @Test
    public void editProductPriceNotContainsOne() {
        try {
            bidOffer.editProductPrice(p1.getID(), 2);
            fail("shouldn't get her");
        } catch (IllegalStateException e) {
            assertEquals(e.getMessage(), "The product is not exist in the bid");
            assertEquals(0, bidOffer.getApprovals().size());
        }
    }
    @Test
    public void editProductPriceGood() {
        bidOffer.AddToBid(p1.getID(), 10, 4);
        assertEquals(1, bidOffer.getProducts().size());
        List<String> admins = new ArrayList<>();
        admins.add(founder.getUserName());
        admins.add(subscribedUser1.getUserName());
        bidOffer.setApproval(p1.getID(), admins);
        assertEquals(1, bidOffer.getApprovals().size());
        bidOffer.editProductPrice(p1.getID(), 3);
        assertEquals(1, bidOffer.getApprovals().size());
        assertEquals(3,  bidOffer.getPrices().get(p1.getID()), 0.0);
    }

    @Test
    public void declineBidOffer() {
        bidOffer.AddToBid(p1.getID(), 10, 4);
        assertEquals(1, bidOffer.getProducts().size());
        List<String> admins = new ArrayList<>();
        admins.add(founder.getUserName());
        admins.add(subscribedUser1.getUserName());
        bidOffer.setApproval(p1.getID(), admins);
        assertEquals(1, bidOffer.getApprovals().size());
        assertNotNull(bidOffer.getApprovals().get(p1.getID()));
        bidOffer.AddToBid(p2.getID(), 10, 4);
        assertEquals(2, bidOffer.getProducts().size());
        bidOffer.setApproval(p2.getID(), admins);
        assertEquals(2, bidOffer.getApprovals().size());
        assertNotNull(bidOffer.getApprovals().get(p1.getID()));

        bidOffer.declineBidOffer(p1.getID());
        assertEquals(1, bidOffer.getProducts().size());
        assertEquals(1, bidOffer.getApprovals().size());
        try {
            bidOffer.declineBidOffer(p1.getID());
            fail("shouldn't get her");
        }
        catch (IllegalStateException e)
        {
            assertEquals(e.getMessage(),"The product is not exist in the bid");
            assertEquals(1, bidOffer.getProducts().size());
            assertEquals(1, bidOffer.getApprovals().size());
        }
    }
    @Test
    public void approveBidOfferGood() {
        bidOffer.AddToBid(p1.getID(), 10, 4);
        assertEquals(1, bidOffer.getProducts().size());
        assertEquals(1, bidOffer.getApprovals().size());
        List<String> admins = new ArrayList<>();
        admins.add(founder.getUserName());
        admins.add(subscribedUser1.getUserName());
        bidOffer.setApproval(p1.getID(), admins);
        assertEquals(1, bidOffer.getApprovals().size());
        bidOffer.editProductPrice(p1.getID(), 3);
        assertEquals(1, bidOffer.getApprovals().size());
        assertNull(bidOffer.approveBidOffer(founder.getUserName(), p1.getID()));
        assertNotNull(bidOffer.approveBidOffer(subscribedUser1.getUserName(), p1.getID()));
    }
    @Test
    public void approveBidOfferTwice() {
        bidOffer.AddToBid(p1.getID(), 10, 4);
        assertEquals(1, bidOffer.getProducts().size());
        List<String> admins = new ArrayList<>();
        admins.add(founder.getUserName());
        admins.add(subscribedUser1.getUserName());
        bidOffer.setApproval(p1.getID(), admins);
        assertEquals(1, bidOffer.getApprovals().size());
        bidOffer.editProductPrice(p1.getID(), 3);
        assertEquals(1, bidOffer.getApprovals().size());
        assertNull(bidOffer.approveBidOffer(founder.getUserName(), p1.getID()));
        try {
            bidOffer.approveBidOffer(founder.getUserName(), p1.getID());
            fail("shouldn't get her");
        }
        catch (IllegalStateException e)
        {
            assertEquals(e.getMessage(),"the admin is already answered");
            assertEquals(1, bidOffer.getApprovals().size());
        }
    }
}
