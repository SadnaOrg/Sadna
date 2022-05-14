package BusinessLayer.Users;

import BusinessLayer.Products.Users.ShopOwner;
import BusinessLayer.Products.Users.SubscribedUser;
import BusinessLayer.Shops.Shop;
import BusinessLayer.Products.Users.BaseActions.BaseActionType;
import BusinessLayer.Products.Users.BaseActions.CloseShop;
import BusinessLayer.Products.Users.BaseActions.ReOpenShop;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

public class ShopOwnerTest {
    SubscribedUser founderUser = mock(SubscribedUser.class);
    Shop shop = mock(Shop.class);

    private ShopOwner founder;
    SubscribedUser ownerUser = mock(SubscribedUser.class);

    private ShopOwner owner;

    CloseShop closeShop = mock(CloseShop.class);
    ReOpenShop reOpenShop = mock(ReOpenShop.class);

    @Before
    public void setUp(){
        founder = new ShopOwner(shop,founderUser,true);
        owner = new ShopOwner(shop,ownerUser,false);
        founder.AddAction(BaseActionType.REOPEN_SHOP, reOpenShop);
        founder.AddAction(BaseActionType.CLOSE_SHOP, closeShop);
    }

    @Test
    public void closeShopSuccess(){
        try {
            founder.closeShop();
        }
        catch (Exception ignored){

        }
    }

    @Test
    public void closeShopFailure(){
        try {
            owner.closeShop();
            fail("not founder has closed the shop");
        }
        catch (Exception ignored){

        }
    }

    @Test
    public void reopenShopSuccess(){
        closeShopSuccess();
        try {
            founder.reOpenShop();
        }
        catch (Exception ignored){

        }
    }

    @Test
    public void reopenShopFailure(){
        closeShopSuccess();
        try {
            owner.reOpenShop();
            fail("owner reopened shop!");
        }
        catch (Exception ignored){

        }
    }
}
