package BusinessLayer.Users.BaseActions;

import BusinessLayer.Shops.Shop;
import BusinessLayer.Users.ShopOwner;
import BusinessLayer.Users.SubscribedUser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class ReopenShopTest {
    private Shop shop = mock(Shop.class);
    private SubscribedUser founderUser = mock(SubscribedUser.class);
    private ReOpenShop reOpenShop = new ReOpenShop(shop, founderUser);

    ShopOwner founder = mock(ShopOwner.class);

    @Before
    public void setUp(){
        reOpenShop = new ReOpenShop(shop, founderUser);
    }

    @After
    public void tearDown(){
        reOpenShop = null;
    }

    @Test
    public void reOpenShopSuccess() throws Exception {
        when(founderUser.getAdministrator(shop.getId())).thenReturn(founder);

        boolean res= reOpenShop.act();
        assertTrue(res);
    }

    @Test
    public void reOpenShopFailureNotFounder() throws Exception {
        try{
            reOpenShop.act();
            fail("not a founder has reopened the shop!");
        }
        catch (Exception ignore){

        }
    }

    @Test
    public void reOpenShopFailureAlreadyOpen() throws Exception {
        try{
            reOpenShop.act();
            fail("reopened an already open shop!");
        }
        catch (Exception ignore){

        }
    }
}
