package BusinessLayer.Users;

import BusinessLayer.Products.Users.BaseActions.*;
import BusinessLayer.Products.Users.ShopAdministrator;
import BusinessLayer.Products.Users.SubscribedUser;
import BusinessLayer.Shops.Shop;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.naming.NoPermissionException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

// the functions that test the actions of the administrator mock the action classes and assume
// we won't check for all the post conditions of a successful appointment. this is taken care off by the action class.
// we test for these conditions when integrating.
// test flow:
// cases that fail when having permission are tested in the action class
public class ShopAdministratorTest {

    private ShopAdministrator sa;
    private Shop shop = mock(Shop.class);
    private AssignShopManager assignManager = mock(AssignShopManager.class);
    private AssignShopOwner assignOwner =  mock(AssignShopOwner.class);;
    private ChangeManagerPermission changePermissions = mock(ChangeManagerPermission.class);
    private RolesInfo info = mock(RolesInfo.class);
    private HistoryInfo historyInfo = mock(HistoryInfo.class);
    private SubscribedUser assignee = mock(SubscribedUser.class);
    private ShopAdministrator sa1;
    private ShopAdministrator sa2;
    private ShopAdministrator sa3;
    private SubscribedUser su1 = mock(SubscribedUser.class);
    private SubscribedUser su2 = mock(SubscribedUser.class);
    private SubscribedUser su3 = mock(SubscribedUser.class);
    private SubscribedUser sua = mock(SubscribedUser.class);

    @Before
    public void setUp(){
        sa = new ShopAdministrator(shop, sua,"ShopFounder");
        sa1 = new ShopAdministrator(shop, su1,"random");
        sa2 = new ShopAdministrator(shop, su2,"test");
        sa3 = new ShopAdministrator(shop, su3,"name");
    }

    @After
    public void tearDown(){
        sa.emptyActions();
    }

    @Test
    public void assignShopManagerSuccess() throws NoPermissionException {
        sa.AddAction(BaseActionType.ASSIGN_SHOP_MANAGER, assignManager);
        when(sua.getUserName()).thenReturn("Name");
        when(assignManager.act(any(SubscribedUser.class),any(String.class))).thenReturn(true);
        boolean res = sa.AssignShopManager(assignee);
        assertTrue(res);
    }

    @Test
    public void assignShopManagerFailure() {
        try {
            sa.AssignShopManager(assignee);
            fail("assigned without permission!");
        }
        catch (Exception ignored){

        }
    }

    @Test
    public void assignShopOwnerSuccess() throws NoPermissionException {
        when(assignOwner.act(any(SubscribedUser.class),anyString())).thenReturn(true);
        when(sua.getUserName()).thenReturn("Name");
        sa.AddAction(BaseActionType.ASSIGN_SHOP_OWNER, assignOwner);
        boolean res = sa.AssignShopOwner(assignee);
        assertTrue(res);
    }

    @Test
    public void assignShopOwnerFailure() {
        try {
            sa.AssignShopOwner(assignee);
            fail("assigned without permission!");
        }
        catch (Exception ignored){

        }
    }

    @Test
    public void changePermissionsSuccess() throws NoPermissionException {
        sa.AddAction(BaseActionType.ASSIGN_SHOP_OWNER, changePermissions);
        List<BaseActionType> permissions = new LinkedList<>();
        permissions.add(BaseActionType.CLOSE_SHOP); // some new permissions
        when(changePermissions.act(any(SubscribedUser.class),eq(permissions))).thenReturn(true);
        boolean res = changePermissions.act(assignee, permissions);
        assertTrue(res);
    }

    @Test
    public void changePermissionsFailure() throws NoPermissionException {
        List<BaseActionType> permissions = new LinkedList<>();
        permissions.add(BaseActionType.CLOSE_SHOP); // some new permissions
        boolean res = changePermissions.act(assignee, permissions);
        assertFalse(res);
    }

    @Test
    public void getAdminInfoSuccess(){
        sa.AddAction(BaseActionType.ROLE_INFO, info);
        try{
            sa.getAdministratorInfo();
        }
        catch (Exception ignored){
            fail("failed to get info when having permission!");
        }
    }

    @Test
    public void getAdminInfoFailure(){
        try{
            sa.getAdministratorInfo();
            fail("got info without permission!");
        }
        catch (Exception ignored){

        }
    }

    @Test
    public void getHistoryInfoSuccess(){
        sa.AddAction(BaseActionType.HISTORY_INFO, historyInfo);
        try{
            sa.getHistoryInfo();
        }
        catch (Exception ignored){
            fail("failed to get info when having permission!");
        }
    }

    @Test
    public void getHistoryInfoFailure(){
        try{
            sa.getAdministratorInfo();
            fail("got info without permission!");
        }
        catch (Exception ignored){

        }
    }

    @Test
    public void checkCyclesFound(){
        sa1.addAppoint(sa2);
        sa2.addAppoint(sa3);
        boolean res = sa3.checkForCycles(sa1);

        assertTrue(res);
        res = sa2.checkForCycles(sa1);
        assertTrue(res);
    }

    @Test
    public void checkCyclesNotFound(){
        sa1.addAppoint(sa2);
        boolean res = sa3.checkForCycles(sa1);
        assertFalse(res);
    }

    @Test
    public void addAppointSuccess(){
        sa1.addAppoint(sa2);
        boolean res = sa1.getAppoints().contains(sa2);
        assertTrue(res);
    }

    @Test
    public void addAppointFailure(){
        sa1.addAppoint(sa2);
        sa2.addAppoint(sa3);
        try {
            sa3.addAppoint(sa1);
            fail("made a cyclic appointment!");
        }
        catch (Exception ignored){

        }
    }
}

