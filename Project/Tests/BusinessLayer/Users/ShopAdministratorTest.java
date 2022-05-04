package BusinessLayer.Users;

import BusinessLayer.Shops.Shop;
import BusinessLayer.Users.BaseActions.AssignShopManager;
import BusinessLayer.Users.BaseActions.BaseAction;
import BusinessLayer.Users.BaseActions.BaseActionType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.naming.NoPermissionException;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ShopAdministratorTest {
    @Mock
    Shop s;
    @Mock
    SubscribedUser adminUser;
    @InjectMocks
    ShopAdministrator sa;

    @Mock
    SubscribedUser assignee;
    @InjectMocks
    ShopAdministrator newAdmin;
    @Mock
    AssignShopManager assignManager;

    @BeforeEach
    public void setUp(){
        // give default permissions
    }

    @AfterEach
    public void tearDown(){
        // cancel all permissions
    }

    @Test
    public void assignShopManagerSuccess(){
        // give permission
        sa.AddAction(BaseActionType.ASSIGN_SHOP_MANAGER, assignManager);
        try {
            // mock for success
            when(assignManager.act(assignee)).thenReturn(true);
            boolean assigned = sa.AssignShopManager(assignee);
            assertTrue(assigned);

            // we have 2 admins now
            Collection<ShopAdministrator> admins = new ArrayList<>();
            admins.add(sa);
            admins.add(newAdmin);

            when(assignee.getUserName()).thenReturn("Michael");
            when(s.getShopAdministrators()).thenReturn(admins);

            Collection<ShopAdministrator> shopAdministrators =s.getShopAdministrators();
            boolean found = false;
            // search for the new admin
            for (ShopAdministrator shopAdmin:
                 shopAdministrators) {
                User u = shopAdmin.getUser();
                String name = u.getUserName();
                if (name.equals(assignee.getUserName())){
                    found = true;
                }
            }
            assertTrue(found);

            // check that the appointment was correct
            when(adminUser.getUserName()).thenReturn("Itai");
            when(s.getShopAdministrator("Itai")).thenReturn(sa);
            String adminName = adminUser.getUserName();
            ShopAdministrator actual = s.getShopAdministrator(adminName);
            // it is a manger of the shop
            assertNotNull(actual);

        }
        catch (NoPermissionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void assignShopManagerNoPermission() throws NoPermissionException {
        try {
            sa.AssignShopManager(assignee);
            fail("assigned manager without permission");
        } catch (NoPermissionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void assignShopManagerFailureAlreadyManager() throws NoPermissionException {
        assignShopManagerSuccess();
        boolean doubleAssign = sa.AssignShopManager(assignee);
        assertFalse(doubleAssign);
    }
}
