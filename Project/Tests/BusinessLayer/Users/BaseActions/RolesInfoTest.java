package BusinessLayer.Users.BaseActions;

import BusinessLayer.Shops.Shop;
import BusinessLayer.Users.AdministratorInfo;
import BusinessLayer.Users.BaseActions.BaseActionType;
import BusinessLayer.Users.BaseActions.RolesInfo;
import BusinessLayer.Users.ShopAdministrator;
import BusinessLayer.Users.ShopOwner;
import BusinessLayer.Users.SubscribedUser;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.naming.NoPermissionException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RolesInfoTest {
    @Mock
    private Shop shop;
    @Mock
    private SubscribedUser user;
    @InjectMocks
    private RolesInfo rolesInfo;

    @Mock
    private ShopOwner userOwnerMock;
    @Mock
    private ShopAdministrator failUser;

    @BeforeEach
    public void setUp(){
        rolesInfo = new RolesInfo(shop, user);
    }

    @AfterEach
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
