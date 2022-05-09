package BusinessLayer.Users.BaseActions;

import BusinessLayer.Shops.Shop;
import BusinessLayer.Users.BaseActions.AssignShopOwner;
import BusinessLayer.Users.ShopAdministrator;
import BusinessLayer.Users.ShopOwner;
import BusinessLayer.Users.SubscribedUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AssignOwnerTest {
    @Mock
    private Shop shop;
    @Mock
    private SubscribedUser user;
    @InjectMocks
    private AssignShopOwner assignment;

    @Mock
    private SubscribedUser assignee;
    @Mock
    private ShopOwner newOwner;

    @Mock
    private ShopAdministrator m; // admin (mock) of user (field) in shop

    @BeforeEach
    public void setUp(){
        assignment = new AssignShopOwner(shop, user);
    }

    @AfterEach
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

        when(user.getAdministrator(shop.getId())).thenReturn(m); // get admin object of user
        doThrow(new IllegalStateException("cyclic appointment!")).when(m).addAppoint(newOwner);

        try {
            assignment.act(assignee);
            fail("allowed cyclic appointment!");
        }
        catch (Exception ignored){

        }
    }
}
