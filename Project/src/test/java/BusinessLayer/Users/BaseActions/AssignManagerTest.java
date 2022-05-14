package BusinessLayer.Users.BaseActions;

import BusinessLayer.Products.Users.BaseActions.AssignShopManager;
import BusinessLayer.Shops.Shop;
import BusinessLayer.Products.Users.ShopAdministrator;
import BusinessLayer.Products.Users.ShopManager;
import BusinessLayer.Products.Users.SubscribedUser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

// actions test flow:
// make mocks return values such that we will return true / false in different cases
// check ONLY if true/false were returned. any other post conditions will be checked with integration tests

public class AssignManagerTest {
    private Shop shop = mock(Shop.class);
    private SubscribedUser user = mock(SubscribedUser.class);
    private AssignShopManager assignment = new AssignShopManager(shop, user);
    private SubscribedUser assignee = mock(SubscribedUser.class);
    private ShopManager newManager = mock(ShopManager.class);
    private ShopAdministrator m = mock(ShopAdministrator.class); // admin mock of user (field) in shop

    @Before
    public void setUp(){
        assignment = new AssignShopManager(shop, user);
    }

    @After
    public void tearDown(){
        assignment = null;
    }

    @Test
    public void assignSuccess(){
        when(shop.getId()).thenReturn(0); // set shop id

        when(assignee.getUserName()).thenReturn("Michael"); // assignee name
        when(assignee.getAdministrator(shop.getId())).thenReturn(null); // not manager
        when(assignee.addAdministrator(eq(shop.getId()), any(ShopAdministrator.class))).thenReturn(newManager); // not manager

        when(shop.addAdministrator(eq(assignee.getUserName()),any(ShopAdministrator.class))).thenReturn(true); // added admin

        when(user.getAdministrator(shop.getId())).thenReturn(m); // get admin object of user
        doNothing().when(m).addAppoint(newManager); // add an appointment

        boolean res = assignment.act(assignee);
        assertTrue(res);
    }

    @Test
    public void assignFailureAlreadyManager(){
        when(assignee.getAdministrator(shop.getId())).thenReturn(newManager);
        boolean res = assignment.act(assignee);
        assertFalse(res);
    }

    @Test
    public void assignFailureShopIsClosed(){
        when(shop.addAdministrator(eq(assignee.getUserName()),any(ShopAdministrator.class))).
                thenThrow(new IllegalStateException("The shop is closed"));
        try{
            assignment.act(assignee);
            fail("tried to assign a manager to a closed shop!");
        }
        catch (Exception ignored){

        }
    }

    @Test
    public void assignFailureCyclicAppointment(){
        when(shop.addAdministrator(eq(assignee.getUserName()),any(ShopAdministrator.class))).thenReturn(true); // added admin

        //when(user.getAdministrator(shop.getId())).thenReturn(m); // get admin object of user
        //doThrow(new IllegalStateException("cyclic appointment!")).when(m).addAppoint(newManager);

        try {
            assignment.act(assignee);
            fail("allowed cyclic appointment!");
        }
        catch (Exception ignored){

        }
    }
}
