package BusinessLayer.Users.BaseActions;

import BusinessLayer.Products.Users.BaseActions.BaseActionType;
import BusinessLayer.Products.Users.BaseActions.ChangeManagerPermission;
import BusinessLayer.Shops.Shop;
import BusinessLayer.Products.Users.ShopAdministrator;
import BusinessLayer.Products.Users.ShopManager;
import BusinessLayer.Products.Users.ShopOwner;
import BusinessLayer.Products.Users.SubscribedUser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.naming.NoPermissionException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

// we don't test that the new permissions are actually associated with the owner.
// that is the responsibility of the owner object.
// that behavior will be tested in integration tests.
public class ChangeManagerPermissionTest {
    private Shop shop = mock(Shop.class);
    private SubscribedUser user = mock(SubscribedUser.class);
    private ChangeManagerPermission permissionChange = new ChangeManagerPermission(shop, user);

    private SubscribedUser manager = mock(SubscribedUser.class);
    private ShopManager managerAdminMock = mock(ShopManager.class);
    private ShopOwner userOwnerMock = mock(ShopOwner.class);

    private ConcurrentLinkedDeque<ShopAdministrator> goodQ;

    @Before
    public void setUp(){
        permissionChange = new ChangeManagerPermission(shop, user);
        goodQ = new ConcurrentLinkedDeque<>();
        goodQ.add(managerAdminMock);
    }

    @After
    public void tearDown(){
        permissionChange = null;
        goodQ = null;
    }

    @Test
    public void changePermissionSuccess() throws NoPermissionException {
        ConcurrentLinkedDeque<ShopAdministrator> appoints = new ConcurrentLinkedDeque<>();
        appoints.add(managerAdminMock);

        when(shop.getId()).thenReturn(0); // shop ID

        when(manager.getAdministrator(shop.getId())).thenReturn(managerAdminMock); // manager
        when(user.getAdministrator(shop.getId())).thenReturn(userOwnerMock); // owner
        when(userOwnerMock.getAppoints()).thenReturn(appoints);

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
        //when(user.getAdministrator(shop.getId())).thenReturn(null);

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
        when(manager.getAdministrator(shop.getId())).thenReturn(managerAdminMock); // manager
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
