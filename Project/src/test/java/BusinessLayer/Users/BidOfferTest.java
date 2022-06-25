package BusinessLayer.Users;

import BusinessLayer.Products.Product;
import BusinessLayer.Shops.Shop;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class BidOfferTest {

    private BidOffer bidOffer;

    private final SubscribedUser founder = new SubscribedUser("Founder Guy","Guy123456",new Date(2001, Calendar.DECEMBER,1));
    private SubscribedUser subscribedUser1 = new SubscribedUser("meir maor","meiow",new Date(2001, Calendar.DECEMBER,1));
    private User user1 = new Guest("meow maor");
    private Shop s1;
    private Product p1;
    private Product p2;

    @Before
    public void setUp() throws Exception {
        s1 = new Shop(100, "shop","testing shop", founder);
        p1 = new Product(1, "a", 5, 100);
        p2 = new Product(2, "c", 15, 500);
        founder.login(founder.getUserName(),"Guy123456");
        founder.assignShopOwner(s1.getId(),subscribedUser1);
        bidOffer= new BidOffer(s1.getId(), user1);
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
        assertEquals(0, bidOffer.getApprovals().get(p1.getID()).getAdministraitorsApproval().size());
        assertEquals(10, bidOffer.getApprovals().get(p1.getID()).getQuantity());

    }

    @Test
    public void setApprovalOnceGoodThenAddSame() {
        bidOffer.AddToBid(p1.getID(),10,4);
        assertEquals(1, bidOffer.getApprovals().size());
        assertEquals(0, bidOffer.getApprovals().get(p1.getID()).getAdministraitorsApproval().size());
        List<String> admins = new ArrayList<>();
        admins.add(founder.getUserName());
        admins.add(subscribedUser1.getUserName());
        bidOffer.setApproval(p1.getID(),admins);
        assertEquals(2, bidOffer.getApprovals().get(p1.getID()).getAdministraitorsApproval().size());
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
        assertEquals(0, bidOffer.getApprovals().size());

        try {
            bidOffer.removeProduct(p1.getID());
            fail("shouldn't get her");
        }
        catch (IllegalStateException e)
        {
            assertEquals(e.getMessage(),"The product is not exist in the bid");
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
        assertNull(bidOffer.approveBidOffer(founder.getName(), p1.getID()));
        assertEquals(2, bidOffer.getApprovals().get(p1.getID()).getAdministraitorsApproval().size());
        assertNotNull(bidOffer.approveBidOffer(subscribedUser1.getName(), p1.getID()));
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
        assertNull(bidOffer.approveBidOffer(founder.getName(), p1.getID()));
        try {
            bidOffer.approveBidOffer(founder.getName(), p1.getID());
            fail("shouldn't get her");
        }
        catch (IllegalStateException e)
        {
            assertEquals(e.getMessage(),"the admin is already answered");
            assertEquals(1, bidOffer.getApprovals().size());
        }
    }
}