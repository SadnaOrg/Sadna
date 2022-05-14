package BusinessLayer.Users.BaseActions;

import BusinessLayer.Products.Users.BaseActions.AssignShopOwner;
import BusinessLayer.Shops.Shop;
import BusinessLayer.Products.Users.ShopAdministrator;
import BusinessLayer.Products.Users.ShopOwner;
import BusinessLayer.Products.Users.SubscribedUser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class AssignOwnerTest {
    private Shop shop = mock(Shop.class);
    private SubscribedUser user = mock(SubscribedUser.class);

    private AssignShopOwner assignment = new AssignShopOwner(shop, user);

    private SubscribedUser assignee = mock(SubscribedUser.class);
    private ShopOwner newOwner = mock(ShopOwner.class);
    private ShopAdministrator m = mock(ShopAdministrator.class); // admin (mock) of user (field) in shop

    @Before
    public void setUp(){
        assignment = new AssignShopOwner(shop, user);
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
        when(assignee.addAdministrator(eq(shop.getId()), any(ShopAdministrator.class))).thenReturn(newOwner); // not manager

        when(shop.addAdministrator(eq(assignee.getUserName()),any(ShopAdministrator.class))).thenReturn(true); // added admin

        when(user.getAdministrator(shop.getId())).thenReturn(m); // get admin object of user
        doNothing().when(m).addAppoint(newOwner); // add an appointment

        boolean res = assignment.act(assignee);
        assertTrue(res);
    }

    @Test
    public void assignFailureAlreadyOwner(){
        when(assignee.getAdministrator(shop.getId())).thenReturn(newOwner);
        boolean res = assignment.act(assignee);
        assertFalse(res);
    }

    @Test
    public void assignFailureShopIsClosed(){
        when(shop.addAdministrator(eq(assignee.getUserName()),any(ShopAdministrator.class))).
                thenThrow(new IllegalStateException("The shop is closed"));
        try{
            assignment.act(assignee);
            fail("tried to assign an owner to a closed shop!");
        }
        catch (Exception ignored){

        }
    }

    @Test
    public void assignFailureCyclicAppointment(){
        when(shop.addAdministrator(eq(assignee.getUserName()),any(ShopAdministrator.class))).thenReturn(true); // added owner

        //when(user.getAdministrator(shop.getId())).thenReturn(m); // get admin object of user
        //doThrow(new IllegalStateException("cyclic appointment!")).when(m).addAppoint(newOwner);

        try {
            assignment.act(assignee);
            fail("allowed cyclic appointment!");
        }
        catch (Exception ignored){

        }
    }
}
