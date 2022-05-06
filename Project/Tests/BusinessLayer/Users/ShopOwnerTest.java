package BusinessLayer.Users;

import main.java.BusinessLayer.Shops.Shop;
import main.java.BusinessLayer.Users.BaseActions.BaseActionType;
import main.java.BusinessLayer.Users.BaseActions.CloseShop;
import main.java.BusinessLayer.Users.BaseActions.ReOpenShop;
import main.java.BusinessLayer.Users.ShopOwner;
import main.java.BusinessLayer.Users.SubscribedUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.fail;
@ExtendWith(MockitoExtension.class)
public class ShopOwnerTest {
    @Mock
    SubscribedUser founderUser;
    @Mock
    Shop shop;

    private ShopOwner founder;
    @Mock
    SubscribedUser ownerUser;

    private ShopOwner owner;

    @Mock
    CloseShop closeShop;
    @Mock
    ReOpenShop reOpenShop;

    @BeforeEach
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
