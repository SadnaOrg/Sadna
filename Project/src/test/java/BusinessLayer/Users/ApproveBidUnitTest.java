package BusinessLayer.Users;

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
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class ApproveBidUnitTest {
    private ApproveBid approveBid;

    @Mock
    private SubscribedUser founder;
    @Mock
    private SubscribedUser subscribedUser1;
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Before
    public void setUp() throws Exception {
        when(founder.getUserName()).thenReturn("Founder Guy");
        when(subscribedUser1.getUserName()).thenReturn("meir maor");
        List<String> admins = new ArrayList<>();
        admins.add(founder.getUserName());
        admins.add(subscribedUser1.getUserName());
        approveBid= new ApproveBid(1,1,10,5.0,admins);
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
