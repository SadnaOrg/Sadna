package BusinessLayer.Users.BaseActions;

import main.java.BusinessLayer.Shops.Shop;
import main.java.BusinessLayer.Users.BaseActions.ReOpenShop;
import main.java.BusinessLayer.Users.ShopOwner;
import main.java.BusinessLayer.Users.SubscribedUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class ReopenShopTest {
    @Mock
    private Shop shop;
    @Mock
    private SubscribedUser founderUser;
    @InjectMocks
    private ReOpenShop reOpenShop;

    @Mock
    ShopOwner founder;
    @Mock
    ShopOwner failOwner;

    @BeforeEach
    public void setUp(){
        reOpenShop = new ReOpenShop(shop, founderUser);
    }

    @AfterEach
    public void tearDown(){
        reOpenShop = null;
    }

    @Test
    public void reOpenShopSuccess() throws Exception {
        when(founderUser.getUserName()).thenReturn("Bill Gates");
        when(founder.isFounder()).thenReturn(true);

        when(shop.getShopAdministrator("Bill Gates")).thenReturn(founder);
        when(shop.isOpen()).thenReturn(false);
        doNothing().when(shop).open();

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
