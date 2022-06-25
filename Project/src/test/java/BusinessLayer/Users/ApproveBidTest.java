package BusinessLayer.Users;

import BusinessLayer.Products.Product;
import BusinessLayer.Shops.Shop;
import BusinessLayer.Shops.ShopController;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class ApproveBidTest {
    private ApproveBid approveBid;


    private final SubscribedUser founder = new SubscribedUser("Founder Guy","Guy123456",new Date(2001, Calendar.DECEMBER,1));
    private SubscribedUser subscribedUser1 = new SubscribedUser("meir maor","meiow",new Date(2001, Calendar.DECEMBER,1));

    @Before
    public void setUp() throws Exception {
        List<String> admins = new ArrayList<>();
        admins.add(founder.getUserName());
        admins.add(subscribedUser1.getUserName());
        approveBid= new ApproveBid(1,1,5,5.0,admins);
    }

    @Test
    public void approveBoth() {
        assertFalse(approveBid.getAdministraitorsApproval().get(founder.getUserName()));
        assertFalse(approveBid.approve(founder.getUserName()));
        assertTrue(approveBid.getAdministraitorsApproval().get(founder.getUserName()));
        assertFalse(approveBid.getAdministraitorsApproval().get(subscribedUser1.getUserName()));
        assertTrue(approveBid.approve(subscribedUser1.getUserName()));
        assertTrue(approveBid.getAdministraitorsApproval().get(subscribedUser1.getUserName()));
    }

    @Test
    public void approveBothSame() {
        assertFalse(approveBid.getAdministraitorsApproval().get(founder.getUserName()));
        assertFalse(approveBid.approve(founder.getUserName()));
        assertTrue(approveBid.getAdministraitorsApproval().get(founder.getUserName()));
        try {
            assertFalse(approveBid.approve(founder.getUserName()));
            fail("shouldn't get here");
        }
        catch (IllegalStateException e)
        {
            assertEquals(e.getMessage(),"the admin is already answered");
            assertTrue(approveBid.getAdministraitorsApproval().get(founder.getUserName()));
        }
    }

    @Test
    public void isApproved() {
        assertFalse(approveBid.isApproved());
        assertFalse(approveBid.getAdministraitorsApproval().get(founder.getUserName()));
        assertFalse(approveBid.approve(founder.getUserName()));
        assertTrue(approveBid.getAdministraitorsApproval().get(founder.getUserName()));
        assertFalse(approveBid.isApproved());
        assertFalse(approveBid.getAdministraitorsApproval().get(subscribedUser1.getUserName()));
        assertTrue(approveBid.approve(subscribedUser1.getUserName()));
        assertTrue(approveBid.getAdministraitorsApproval().get(subscribedUser1.getUserName()));
        assertTrue(approveBid.isApproved());
    }
    @Test
    public void resetApproves() {
        assertFalse(approveBid.isApproved());
        assertFalse(approveBid.getAdministraitorsApproval().get(founder.getUserName()));
        assertFalse(approveBid.approve(founder.getUserName()));
        assertTrue(approveBid.getAdministraitorsApproval().get(founder.getUserName()));
        assertFalse(approveBid.isApproved());
        assertTrue(approveBid.resetApproves());
        assertFalse(approveBid.getAdministraitorsApproval().get(founder.getUserName()));
        assertFalse(approveBid.getAdministraitorsApproval().get(subscribedUser1.getUserName()));
        assertFalse(approveBid.approve(subscribedUser1.getUserName()));
        assertTrue(approveBid.getAdministraitorsApproval().get(subscribedUser1.getUserName()));
        assertFalse(approveBid.isApproved());
        assertFalse(approveBid.getAdministraitorsApproval().get(founder.getUserName()));
        assertTrue(approveBid.approve(founder.getUserName()));
        assertTrue(approveBid.getAdministraitorsApproval().get(founder.getUserName()));
        assertTrue(approveBid.isApproved());

    }

}