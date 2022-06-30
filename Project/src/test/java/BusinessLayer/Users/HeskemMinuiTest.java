package BusinessLayer.Users;

import AcceptanceTests.DataObjects.Shop;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import static org.junit.Assert.*;

public class HeskemMinuiTest {

    private HeskemMinui heskemMinui;

    private SubscribedUser founder = new SubscribedUser("Founder Guy","Guy123456",new Date(2001, Calendar.DECEMBER,1));
    private SubscribedUser second = new SubscribedUser("Founder meir","Guy123456",new Date(2001, Calendar.DECEMBER,1));
    private SubscribedUser third = new SubscribedUser("Founder maor","Guy123456",new Date(2001, Calendar.DECEMBER,1));
    private SubscribedUser userToAssgin = new SubscribedUser("meir maor","meiow",new Date(2001, Calendar.DECEMBER,1));


    @Before
    public void setUp() {
        Collection<String> admins =new ArrayList<>();
        admins.add(founder.getUserName());
        admins.add(second.getUserName());
        heskemMinui = new HeskemMinui(1,userToAssgin.getUserName(),founder.getUserName(),admins);
    }

    @Test
    public void allApprove() {
        assertFalse(heskemMinui.getApprovals().get(founder.getUserName()));
        assertFalse(heskemMinui.approve(founder.getUserName()));
        assertTrue(heskemMinui.getApprovals().get(founder.getUserName()));
        assertFalse(heskemMinui.getApprovals().get(second.getUserName()));
        assertTrue(heskemMinui.approve(second.getUserName()));
        assertTrue(heskemMinui.getApprovals().get(second.getUserName()));
        assertTrue(heskemMinui.isApproved());

    }
    @Test
    public void approveTwice() {
        assertFalse(heskemMinui.getApprovals().get(founder.getUserName()));
        assertFalse(heskemMinui.approve(founder.getUserName()));
        assertTrue(heskemMinui.getApprovals().get(founder.getUserName()));
        try {
            assertFalse(heskemMinui.approve(founder.getUserName()));
            fail("shouldn't get here");
        }
        catch (IllegalStateException e)
        {
            assertEquals(e.getMessage(),"the admin is already approve");
            assertTrue(heskemMinui.getApprovals().get(founder.getUserName()));
        }
    }
    @Test
    public void outsiderApprove() {
        assertFalse(heskemMinui.getApprovals().get(founder.getUserName()));
        assertFalse(heskemMinui.approve(founder.getUserName()));
        assertTrue(heskemMinui.getApprovals().get(founder.getUserName()));
        try {
            assertFalse(heskemMinui.approve(third.getUserName()));
            fail("shouldn't get here");
        }
        catch (IllegalStateException e)
        {
            assertEquals(e.getMessage(),"the admin is not in the approve list");
            assertFalse(heskemMinui.getApprovals().containsKey(third.getUserName()));
        }
    }
    @Test
    public void isApproved() {
        assertFalse(heskemMinui.isApproved());
        assertFalse(heskemMinui.getApprovals().get(founder.getUserName()));
        assertFalse(heskemMinui.approve(founder.getUserName()));
        assertTrue(heskemMinui.getApprovals().get(founder.getUserName()));
        assertFalse(heskemMinui.isApproved());
        assertFalse(heskemMinui.getApprovals().get(second.getUserName()));
        assertTrue(heskemMinui.approve(second.getUserName()));
        assertTrue(heskemMinui.getApprovals().get(second.getUserName()));
        assertTrue(heskemMinui.isApproved());
    }
}