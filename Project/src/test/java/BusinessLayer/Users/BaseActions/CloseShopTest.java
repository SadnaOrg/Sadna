package BusinessLayer.Users.BaseActions;

import BusinessLayer.Shops.Shop;
import BusinessLayer.Users.BaseActions.CloseShop;
import BusinessLayer.Users.ShopAdministrator;
import BusinessLayer.Users.ShopOwner;
import BusinessLayer.Users.SubscribedUser;
import BusinessLayer.Users.ShopAdministrator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.naming.NoPermissionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class CloseShopTest {
    @Mock
    private Shop shop;
    @Mock
    private SubscribedUser user;
    @InjectMocks
    private CloseShop close;

    @Mock
    private ShopOwner ownerMock;
    @Mock
    private ShopAdministrator failAdmin;

    @BeforeEach
    public void setUp(){
        close = new CloseShop(shop, user);
    }

    @AfterEach
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
