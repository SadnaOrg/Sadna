package BusinessLayer.Users;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class HeskemMinuiUnitTest {


    private HeskemMinui heskemMinui;
    @Mock
    private SubscribedUser founder ;
    @Mock
    private SubscribedUser second ;
    @Mock
    private SubscribedUser third ;
    @Mock
    private SubscribedUser userToAssgin;
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Before
    public void setUp() {
        when(founder.getUserName()).thenReturn("Founder Guy");
        when(second.getUserName()).thenReturn("Founder meir");
        when(third.getUserName()).thenReturn("Founder maor");
        when(userToAssgin.getUserName()).thenReturn("meir maor");
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
