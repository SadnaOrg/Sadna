package BusinessLayer.Users.BaseActions;

import BusinessLayer.Shops.Shop;
import BusinessLayer.Users.ShopAdministrator;
import BusinessLayer.Users.ShopOwner;
import BusinessLayer.Users.SubscribedUser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.naming.NoPermissionException;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RolesInfoTest {
    private Shop shop = mock(Shop.class);
    private SubscribedUser user = mock(SubscribedUser.class);
    private RolesInfo rolesInfo = new RolesInfo(shop, user);

    private ShopOwner userOwnerMock = mock(ShopOwner.class);
    private ShopAdministrator failUser = mock(ShopAdministrator.class);

    @Before
    public void setUp(){
        rolesInfo = new RolesInfo(shop, user);
    }

    @After
    public void tearDown(){
        rolesInfo = null;
    }

    @Test
    public void getRolesInfoSuccess() throws NoPermissionException {
        when(shop.isOpen()).thenReturn(true);
        when(user.getAdministrator(shop.getId())).thenReturn(userOwnerMock);
        try {
            rolesInfo.act();
        }
        catch (Exception e){
            fail("failed to get shop info when it is possible!");
        }
    }

    @Test
    public void getRolesInfoFailureShopClosed(){
        when(shop.isOpen()).thenReturn(false);

        try {
            rolesInfo.act();
            fail("got roles of a closed shop!");
        } catch (NoPermissionException ignored) {

        }
    }

    @Test
    public void getRolesInfoFailureNotOwner(){
        when(shop.isOpen()).thenReturn(true);
        when(shop.getId()).thenReturn(0);

        when(user.getAdministrator(shop.getId())).thenReturn(failUser);
        try {
            rolesInfo.act();
            fail("a non owner administrator has viewed the shop roles info!");
        } catch (NoPermissionException ignored) {

        }
    }
}
