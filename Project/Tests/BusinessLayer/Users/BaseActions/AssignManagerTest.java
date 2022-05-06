package BusinessLayer.Users.BaseActions;

import main.java.BusinessLayer.Shops.Shop;
import main.java.BusinessLayer.Users.BaseActions.AssignShopManager;
import main.java.BusinessLayer.Users.ShopAdministrator;
import main.java.BusinessLayer.Users.ShopManager;
import main.java.BusinessLayer.Users.SubscribedUser;
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

// actions test flow:
// make mocks return values such that we will return true / false in different cases
// check ONLY if true/false were returned. any other post conditions will be checked with integration tests
@ExtendWith(MockitoExtension.class)
public class AssignManagerTest {
    @Mock
    private Shop shop;
    @Mock
    private SubscribedUser user;
    @InjectMocks
    private AssignShopManager assignment;

    @Mock
    private SubscribedUser assignee;
    @Mock
    private ShopManager newManager;

    @Mock
    private ShopAdministrator m; // admin mock of user (field) in shop



    @BeforeEach
    public void setUp(){
        assignment = new AssignShopManager(shop, user);
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

        when(user.getAdministrator(shop.getId())).thenReturn(m); // get admin object of user
        doThrow(new IllegalStateException("cyclic appointment!")).when(m).addAppoint(newManager);

        try {
            assignment.act(assignee);
            fail("allowed cyclic appointment!");
        }
        catch (Exception ignored){

        }
    }
}
