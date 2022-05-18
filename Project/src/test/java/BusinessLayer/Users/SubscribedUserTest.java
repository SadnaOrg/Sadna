package BusinessLayer.Users;

import BusinessLayer.Shops.PurchaseHistory;
import BusinessLayer.Shops.Shop;
import BusinessLayer.Users.BaseActions.AssignShopManager;
import BusinessLayer.Users.BaseActions.BaseAction;
import BusinessLayer.Users.BaseActions.BaseActionType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.naming.NoPermissionException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class SubscribedUserTest {
    private SubscribedUser user1;
    private String user1pass;
    private SubscribedUser user2;
    private SubscribedUser toAssign;
    private SubscribedUser founder;
    private Shop shop1;
    private Shop shop2;
    ShopManager sm1;
    ShopOwner so1;
    @Mock
    Collection<PurchaseHistory> c_PH ;
    @Mock
    Collection<BaseActionType> types;

    public void testLogin(SubscribedUser u,String pass) {
        assertFalse("logged in with wrong password",u.login(u.getUserName(),pass+"wrong_pass"));
        Assert.assertFalse(u.isLoggedIn());
        assertFalse("logged in with wrong user name ",u.login(u.getUserName()+"wrong_name",pass));
        Assert.assertFalse(u.isLoggedIn());

        assertTrue(u.login(u.getUserName(),pass));
        Assert.assertTrue(u.isLoggedIn());

        try{
            if(u.login(u.getUserName(),pass))
                fail("cannot log in second time");
        }catch (Exception ignored){}
        Assert.assertTrue(u.isLoggedIn());
    }

    @Test
    public void testLogin() {
        testLogin(user1,user1pass);
    }


    @Test
    public void testLogout(){
        if(!user1.isLoggedIn())
            testLogin(user1,user1pass);
        assertTrue(user1.logout());
        assertFalse(user1.isLoggedIn());
        try {
            assertFalse(user1.logout());
            fail("user cannot logout twice");
        }catch (Exception ignored){}
        assertFalse(user1.isLoggedIn());
    }
    @Mock
    Collection<AdministratorInfo> a_info;
    private ShopAdministrator sa;

    @Before
    public void setUp() {
        user1pass = "pass12";
        user1 = new SubscribedUser("user1",user1pass);
        user2 = new SubscribedUser("user2","pass12");
        toAssign = new SubscribedUser("toAssign","pass12");
        founder = new SubscribedUser("Founder Guy","pass12");

        shop1 = mock(Shop.class);// new Shop(1,"shop1", founder);
        when(shop1.getId()).thenReturn(1);
        shop2 = mock(Shop.class);// new Shop(2,"shop2", founder);
        when(shop2.getId()).thenReturn(2);

        sm1 = mock(ShopManager.class);
        so1 = mock(ShopOwner.class);
        sa = mock(ShopAdministrator.class);

        //sm1.AddAction(BaseActionType.ASSIGN_SHOP_MANAGER);
    }

    @Test
    public void addAdministrator_new_shop() {
        assertNull(user1.getAdministrator(shop1.getId()));
        user1.addAdministrator(shop1.getId(),sm1);
        assertEquals(user1.getAdministrator(shop1.getId()),sm1);

        user1.addAdministrator(shop1.getId(),sa);
        assertNull(user1.getAdministrator(shop2.getId()));
        assertEquals(user1.getAdministrator(shop1.getId()),sm1);
    }

    @Test (expected =NoPermissionException.class)
    public void assignShopManager_fail_not_login() throws NoPermissionException {
        user1.addAdministrator(shop1.getId(),sm1);
        when(sm1.AssignShopManager(toAssign)).thenReturn(true);
        ConcurrentLinkedDeque<BaseActionType> c = new ConcurrentLinkedDeque<>();
        c.add(BaseActionType.ASSIGN_SHOP_MANAGER);
        when(sm1.getActionsTypes()).thenReturn(c);
        user1.assignShopManager(shop1.getId(),toAssign);
    }

    @Test (expected =NoPermissionException.class)
    public void assignShopManager_fail_no_permission() throws NoPermissionException {
        testLogin(user1,user1pass);
        when(sm1.AssignShopManager(toAssign)).thenReturn(false);
        ConcurrentLinkedDeque<BaseActionType> c = new ConcurrentLinkedDeque<>();
        when(sm1.getActionsTypes()).thenReturn(c);
        user1.assignShopManager(shop1.getId(),toAssign);
    }

    @Test
    public void assignShopManager_successe() throws NoPermissionException {
        testLogin(user1,user1pass);
        user1.addAdministrator(shop1.getId(),sm1);
        when(sm1.AssignShopManager(toAssign)).thenReturn(true);
        assertTrue(user1.assignShopManager(shop1.getId(),toAssign));
    }

    @Test (expected =NoPermissionException.class)
    public void assignShopOwner_fail_not_login() throws NoPermissionException {
        user1.addAdministrator(shop1.getId(),sm1);
        when(sm1.AssignShopOwner(toAssign)).thenReturn(false);
        user1.assignShopOwner(shop1.getId(),toAssign);
    }

    @Test (expected =NoPermissionException.class)
    public void assignShopOwner_fail_no_permission() throws NoPermissionException {
        testLogin(user1,user1pass);
        when(sm1.AssignShopOwner(toAssign)).thenReturn(false);
        user1.assignShopOwner(shop1.getId(),toAssign);
    }

    @Test
    public void assignShopOwner_successe() throws NoPermissionException {
        testLogin(user1,user1pass);
        user1.addAdministrator(shop1.getId(),sm1);
        when(sm1.AssignShopOwner(toAssign)).thenReturn(true);
        assertTrue(user1.assignShopOwner(shop1.getId(),toAssign));
    }

    @Test (expected =NoPermissionException.class)
    public void closeShop_fail_not_login() throws NoPermissionException {
        user1.addAdministrator(shop1.getId(),so1);
        when(so1.closeShop()).thenReturn(false);
        Assert.assertFalse(user1.closeShop(shop1.getId()));
    }

    @Test (expected =NoPermissionException.class)
    public void closeShop_fail_no_permission() throws NoPermissionException {
        testLogin(user1,user1pass);
        when(so1.closeShop()).thenReturn(false);
        Assert.assertFalse(user1.closeShop(shop1.getId()));
    }

    @Test
    public void closeShop_successe() throws NoPermissionException {
        testLogin(user1,user1pass);
        user1.addAdministrator(shop1.getId(),so1);
        when(so1.closeShop()).thenReturn(true);
        Assert.assertTrue(user1.closeShop(shop1.getId()));
    }

    @Test (expected =NoPermissionException.class)
    public void getHistoryInfo_fail_not_login() throws NoPermissionException {
        user1.addAdministrator(shop1.getId(),so1);
        when(so1.getHistoryInfo()).thenReturn(null);
        assertNull(user1.getHistoryInfo(shop1.getId()));
    }

    @Test (expected =NoPermissionException.class)
    public void getHistoryInfo_fail_no_permission() throws NoPermissionException {
        testLogin(user1,user1pass);
        when(so1.getHistoryInfo()).thenReturn(null);
        assertNull(user1.getHistoryInfo(shop1.getId()));
    }

    @Test
    public void getHistoryInfo_successe() throws NoPermissionException {
        testLogin(user1,user1pass);
        user1.addAdministrator(shop1.getId(),so1);
        when(so1.getHistoryInfo()).thenReturn(c_PH);
        assertEquals(user1.getHistoryInfo(shop1.getId()),c_PH);
    }

    @Test (expected =NoPermissionException.class)
    public void changeManagerPermission_fail_not_login() throws NoPermissionException {
        user1.addAdministrator(shop1.getId(),sm1);
        when(sm1.ChangeManagerPermission(toAssign,types)).thenReturn(false);
        user1.changeManagerPermission(shop1.getId(),toAssign,types);
    }

    @Test (expected =NoPermissionException.class)
    public void changeManagerPermission_fail_no_permission() throws NoPermissionException {
        testLogin(user1,user1pass);
        when(sm1.ChangeManagerPermission(toAssign,types)).thenReturn(false);
        user1.changeManagerPermission(shop1.getId(),toAssign,types);
    }



//    @Test
//    public void changeManagerPermission() {
//        user1.addAdministrator(shop1.getId(), so1);
//        user2.addAdministrator(shop1.getId(), sm1);
//        CopyOnWriteArrayList<BaseActionType> types = new CopyOnWriteArrayList<>();
//        types.add(BaseActionType.ASSIGN_SHOP_MANAGER);
//        try {
//            user1.changeManagerPermission(shop1.getId(), user2, new CopyOnWriteArrayList<>());
//            user2.assignShopManager(shop1.getId(), toAssign);
//            fail("do the transaction with out a permission");
//        } catch (NoPermissionException ignore) {
//            assertNull(toAssign.getAdministrator(shop1.getId()));
//        }
//
//        try{
//            user1.changeManagerPermission(shop1.getId(), user2, types);
//            user2.assignShopManager(shop1.getId(), toAssign);
//            assertNotNull(toAssign.getAdministrator(shop1.getId()));
//        } catch (NoPermissionException e) {
//            fail("supposed to succeed but got exception :"+e.getMessage());
//        }
//
//        try {
//            user2.changeManagerPermission(shop1.getId(), user1, new CopyOnWriteArrayList<>());
//            fail("do the transaction with out a permission");
//        } catch (NoPermissionException ignore) {
//            assertNotEquals(0, user2.getAdministrator(shop1.getId()).action.size());
//        }
//    }

    @Test
    public void changeManagerPermission_successe() throws NoPermissionException {
        testLogin(user1,user1pass);
        user1.addAdministrator(shop1.getId(),sm1);
        when(sm1.ChangeManagerPermission(toAssign,types)).thenReturn(true);
        Assert.assertTrue(user1.changeManagerPermission(shop1.getId(),toAssign,types));
    }

    @Test (expected =NoPermissionException.class)
    public void getAdministratorInfo_fail_not_login() throws NoPermissionException {
        user1.addAdministrator(shop1.getId(),sm1);
        when(sm1.getAdministratorInfo()).thenReturn(null);
        assertNull(user1.getAdministratorInfo(shop1.getId()));
    }

    @Test (expected =NoPermissionException.class)
    public void getAdministratorInfo_fail_no_permission() throws NoPermissionException {
        testLogin(user1,user1pass);
        when(sm1.getAdministratorInfo()).thenReturn(null);
        assertNull(user1.getAdministratorInfo(shop1.getId()));
    }

    @Test
    public void getAdministratorInfo_successe() throws NoPermissionException {
        testLogin(user1,user1pass);
        user1.addAdministrator(shop1.getId(),sm1);
        when(sm1.getAdministratorInfo()).thenReturn(a_info);
        assertEquals(user1.getAdministratorInfo(shop1.getId()),a_info);
    }


//    @Test
//    public void getAdministratorInfo() {
////        user1.addAdministrator(shop1.getId(), so1);
//        try {
//            user2.getAdministratorInfo(shop2.getId());
//            fail("do the transaction with out a permission");
//        } catch (NoPermissionException ignore) {
//        }
//
////        try{
////            Collection<AdministratorInfo> c = user1.getAdministratorInfo(shop1.getId());
////            Assert.assertTrue("should be exactly only a 1 Administrator",c.size() ==1);
////            Assert.assertTrue("should contain the founder",c.stream().filter(a->a.getType() == AdministratorInfo.ShopAdministratorType.FOUNDER
////                                                                                                            && Objects.equals(a.getUserName(), user1.getUserName())).count() ==1);
////            user1.assignShopOwner(shop1.getId(), user2);
////
////            c = user2.getAdministratorInfo(shop1.getId());
////            Assert.assertTrue("should be exactly only a 1 Administrator",c.size() ==2);
////            Assert.assertTrue("should contain the founder",c.stream().filter(a->a.getType() == AdministratorInfo.ShopAdministratorType.FOUNDER
////                    && Objects.equals(a.getUserName(), user1.getUserName())).count() ==1);
////            Assert.assertTrue("should contain all the owners",c.stream().filter(a->a.getType() == AdministratorInfo.ShopAdministratorType.OWNER
////                    && Objects.equals(a.getUserName(), user2.getUserName())).count() ==1);
////        } catch (NoPermissionException e) {
////            fail("supposed to succeed but got exception :"+e.getMessage());
////        }
////
////        try {
////            user1.assignShopManager(shop1.getId(), toAssign);
////            toAssign.getAdministratorInfo(shop1.getId());
////            fail("do the transaction without a permission");
////        } catch (NoPermissionException ignore) {
////        }
//    }

    @Test
    public void login() {
        assertTrue(user1.login("user1", "pass12"));
        try{
            assertTrue(user1.login("user1", "pass12"));
            fail("should not be able to login twice");
        }
        catch (IllegalStateException ignored){}
        assertFalse(user2.login("user2", "1234"));
    }

    @Test
    public void logout() {
        assertTrue(user1.login("user1", "pass12"));
        assertTrue(user1.logout());
        try{
            assertTrue(user1.logout());
            fail("should not be able to logout twice");
        }
        catch (IllegalStateException ignored){}
        try{
            assertTrue(user2.logout());
            fail("should not be able to logout of user who didn't log in");
        }
        catch (IllegalStateException ignored){}
    }


}