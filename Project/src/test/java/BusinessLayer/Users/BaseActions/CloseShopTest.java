package BusinessLayer.Users.BaseActions;

import BusinessLayer.Shops.Shop;
import BusinessLayer.Users.ShopAdministrator;
import BusinessLayer.Users.ShopOwner;
import BusinessLayer.Users.SubscribedUser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import javax.naming.NoPermissionException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class CloseShopTest {
    private Shop shop = mock(Shop.class);
    private SubscribedUser user = mock(SubscribedUser.class);
    private CloseShop close = new CloseShop(shop, user);

    private ShopOwner ownerMock = mock(ShopOwner.class);
    private ShopAdministrator failAdmin = mock(ShopAdministrator.class);

    @Before
    public void setUp(){
        close = new CloseShop(shop, user);
    }

    @After
    public void tearDown(){
        close = null;
    }

    @Test
    public void closeShopSuccess() throws NoPermissionException {
        when(shop.getId()).thenReturn(0);
        when(shop.close()).thenReturn(true);

        when(user.getAdministrator(shop.getId())).thenReturn(ownerMock);
        when(ownerMock.isFounder()).thenReturn(true);

        boolean res = close.act();
        assertTrue(res);
    }

    @Test
    public void closeShopFailureNotOwner() {
        when(shop.getId()).thenReturn(0);

        when(user.getAdministrator(shop.getId())).thenReturn(failAdmin);
        try{
            close.act();
            fail("not an owner of the shop attempted to close it!");
        }
        catch (Exception ignored){

        }
    }

    @Test
    public void closeShopFailureAlreadyClosed() throws NoPermissionException {
        when(shop.getId()).thenReturn(0);
        when(shop.close()).thenReturn(false);

        when(user.getAdministrator(shop.getId())).thenReturn(ownerMock);
        when(ownerMock.isFounder()).thenReturn(true);

        boolean res = close.act();
        assertFalse(res);
    }

    @Test
    public void closeShopFailureNotFounder() {
        when(shop.getId()).thenReturn(0);

        when(user.getAdministrator(shop.getId())).thenReturn(ownerMock);
        when(ownerMock.isFounder()).thenReturn(false);

        try{
            close.act();
            fail("an owner which is not the founder has attempted to close the shop!");
        }
        catch (Exception ignored){

        }
    }
}
