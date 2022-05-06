package BusinessLayer.Users.BaseActions;

import main.java.BusinessLayer.Shops.Shop;
import main.java.BusinessLayer.Users.BaseActions.BaseActionType;
import main.java.BusinessLayer.Users.BaseActions.ChangeManagerPermission;
import main.java.BusinessLayer.Users.ShopAdministrator;
import main.java.BusinessLayer.Users.ShopManager;
import main.java.BusinessLayer.Users.ShopOwner;
import main.java.BusinessLayer.Users.SubscribedUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
// we don't test that the new permissions are actually associated with the owner.
// that is the responsibility of the owner object.
// that behavior will be tested in integration tests.
@ExtendWith(MockitoExtension.class)
public class ChangeManagerPermissionTest {
    @Mock
    private Shop shop;
    @Mock
    private SubscribedUser user;
    @InjectMocks
    private ChangeManagerPermission permissionChange;

    @Mock
    private SubscribedUser manager;
    @Mock
    private ShopManager managerAdminMock;
    @Mock
    private ShopOwner userOwnerMock;

    private ConcurrentLinkedDeque<ShopAdministrator> goodQ;

    @BeforeEach
    public void setUp(){
        permissionChange = new ChangeManagerPermission(shop, user);
        goodQ = new ConcurrentLinkedDeque<>();
        goodQ.add(managerAdminMock);
    }

    @AfterEach
    public void tearDown(){
        permissionChange = null;
        goodQ = null;
    }

    @Test
    public void changePermissionSuccess(){
        when(shop.getId()).thenReturn(0); // shop ID

        when(manager.getAdministrator(shop.getId())).thenReturn(managerAdminMock); // manager
        when(user.getAdministrator(shop.getId())).thenReturn(userOwnerMock); // owner
        when(userOwnerMock.getAppoints()).thenReturn(goodQ);

        List<BaseActionType> permissions = new LinkedList<>();
        permissions.add(BaseActionType.CLOSE_SHOP); // some new permissions

        doNothing().when(managerAdminMock).emptyActions(); // ignore those actions
        doNothing().when(managerAdminMock).AddAction(BaseActionType.CLOSE_SHOP); // ignore as well

        boolean res = permissionChange.act(manager, permissions);
        assertTrue(res);
    }

    @Test
    public void changePermissionFailureNotOwner(){
        when(shop.getId()).thenReturn(0); // shop ID

        when(manager.getAdministrator(shop.getId())).thenReturn(managerAdminMock); // manager
        when(user.getAdministrator(shop.getId())).thenReturn(null); // owner

        List<BaseActionType> permissions = new LinkedList<>();
        permissions.add(BaseActionType.CLOSE_SHOP); // some new permissions

        try {
            permissionChange.act(manager, permissions);
            fail("not owner user has changed permissions!");
        }
        catch (Exception ignored){

        }
    }

    @Test
    public void changePermissionFailureNotManager(){
        when(shop.getId()).thenReturn(0); // shop ID

        when(manager.getAdministrator(shop.getId())).thenReturn(null); // manager

        List<BaseActionType> permissions = new LinkedList<>();
        permissions.add(BaseActionType.CLOSE_SHOP); // some new permissions

        try {
            permissionChange.act(manager, permissions);
            fail("not manager user has changed permissions!");
        }
        catch (Exception ignored){

        }
    }

    @Test
    public void changeManagerPermissionFailureNotAppointer(){
        when(user.getAdministrator(shop.getId())).thenReturn(userOwnerMock); // owner
        when(userOwnerMock.getAppoints()).thenReturn(new ConcurrentLinkedDeque<>());

        List<BaseActionType> permissions = new LinkedList<>();
        permissions.add(BaseActionType.CLOSE_SHOP); // some new permissions
        try {
            permissionChange.act(manager, permissions);
            fail("change permissions of some you didn't appoint!");
        }
        catch (Exception ignored){

        }
    }
}
