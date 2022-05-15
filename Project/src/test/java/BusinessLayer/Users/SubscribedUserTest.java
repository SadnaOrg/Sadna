package BusinessLayer.Users;

import BusinessLayer.Shops.Shop;
import BusinessLayer.Users.BaseActions.BaseActionType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.naming.NoPermissionException;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.Assert.*;

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
    @Before
    public void setUp() {
        user1pass = "pass12";
        user1 = new SubscribedUser("user1",user1pass);
        user2 = new SubscribedUser("user2","pass12");
        toAssign = new SubscribedUser("toAssign","pass12");
        founder = new SubscribedUser("Founder Guy","pass12");
        shop1 = new Shop(1,"shop1", founder);
        shop2 = new Shop(2,"shop2", founder);

        sm1 = new ShopManager(shop1, user1);
        so1 = new ShopOwner(shop1, user1, true);
        sm1.AddAction(BaseActionType.ASSIGN_SHOP_MANAGER);
    }

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



//
//    @Test
//    public void testAssignShopOwner() {
//    }

//    @Test
//    public void testChangeManagerPermission() {
//    }

    @Test
    public void addAdministrator() {
        assertNull(user1.getAdministrator(shop1.getId()));
        user1.addAdministrator(shop1.getId(),sm1);
        assertEquals(user1.getAdministrator(shop1.getId()),sm1);
        user1.addAdministrator(shop1.getId(),new ShopAdministrator(shop2,user1));
        assertNull(user1.getAdministrator(shop2.getId()));
        assertEquals(user1.getAdministrator(shop1.getId()),sm1);
    }

    @Test
    public void assignShopManager() {
        user1.addAdministrator(shop1.getId(),sm1);
        try {
            user2.assignShopManager(shop2.getId(),toAssign);
            fail("do the transaction with out a permission");
        } catch (NoPermissionException ignore) {
            assertNull(toAssign.getAdministrator(shop2.getId()));
        }

        try{
            user1.assignShopManager(shop1.getId(),toAssign);
            assertNotNull(toAssign.getAdministrator(shop1.getId()));
        } catch (NoPermissionException e) {
            fail("supposed to succeed but got exception :"+e.getMessage());
        }

        try {
            if(toAssign.assignShopManager(shop1.getId(),toAssign))
                fail("do the transaction with out a permission");
        } catch (NoPermissionException ignore) {
            assertNull(user2.getAdministrator(shop1.getId()));
        }
    }

    @Test
    public void assignShopOwner() {
        user1.addAdministrator(shop1.getId(), so1);
        try {
            user2.assignShopOwner(shop2.getId(),toAssign);
            fail("do the transaction with out a permission");
        } catch (NoPermissionException ignore) {
            assertNull(toAssign.getAdministrator(shop2.getId()));
        }

        try{
            user1.assignShopOwner(shop1.getId(),toAssign);
            assertNotNull(toAssign.getAdministrator(shop1.getId()));
        } catch (NoPermissionException e) {
            fail("supposed to succeed but got exception :"+e.getMessage());
        }

        try {
            if(toAssign.assignShopOwner(shop1.getId(), toAssign))
                fail("do the transaction with out a permission");
        } catch (NoPermissionException ignore) {
            assertNull(user2.getAdministrator(shop1.getId()));
        }
    }

    @Test
    public void changeManagerPermission() {
        user1.addAdministrator(shop1.getId(), so1);
        user2.addAdministrator(shop1.getId(), sm1);
        CopyOnWriteArrayList<BaseActionType> types = new CopyOnWriteArrayList<>();
        types.add(BaseActionType.ASSIGN_SHOP_MANAGER);
        try {
            user1.changeManagerPermission(shop1.getId(), user2, new CopyOnWriteArrayList<>());
            user2.assignShopManager(shop1.getId(), toAssign);
            fail("do the transaction with out a permission");
        } catch (NoPermissionException ignore) {
            assertNull(toAssign.getAdministrator(shop1.getId()));
        }

        try{
            user1.changeManagerPermission(shop1.getId(), user2, types);
            user2.assignShopManager(shop1.getId(), toAssign);
            assertNotNull(toAssign.getAdministrator(shop1.getId()));
        } catch (NoPermissionException e) {
            fail("supposed to succeed but got exception :"+e.getMessage());
        }

        try {
            user2.changeManagerPermission(shop1.getId(), user1, new CopyOnWriteArrayList<>());
            fail("do the transaction with out a permission");
        } catch (NoPermissionException ignore) {
            assertNotEquals(0, user2.getAdministrator(shop1.getId()).action.size());
        }
    }

    @Test
    public void getAdministratorInfo() {
//        user1.addAdministrator(shop1.getId(), so1);
        try {
            user2.getAdministratorInfo(shop2.getId());
            fail("do the transaction with out a permission");
        } catch (NoPermissionException ignore) {
        }

//        try{
//            Collection<AdministratorInfo> c = user1.getAdministratorInfo(shop1.getId());
//            Assert.assertTrue("should be exactly only a 1 Administrator",c.size() ==1);
//            Assert.assertTrue("should contain the founder",c.stream().filter(a->a.getType() == AdministratorInfo.ShopAdministratorType.FOUNDER
//                                                                                                            && Objects.equals(a.getUserName(), user1.getUserName())).count() ==1);
//            user1.assignShopOwner(shop1.getId(), user2);
//
//            c = user2.getAdministratorInfo(shop1.getId());
//            Assert.assertTrue("should be exactly only a 1 Administrator",c.size() ==2);
//            Assert.assertTrue("should contain the founder",c.stream().filter(a->a.getType() == AdministratorInfo.ShopAdministratorType.FOUNDER
//                    && Objects.equals(a.getUserName(), user1.getUserName())).count() ==1);
//            Assert.assertTrue("should contain all the owners",c.stream().filter(a->a.getType() == AdministratorInfo.ShopAdministratorType.OWNER
//                    && Objects.equals(a.getUserName(), user2.getUserName())).count() ==1);
//        } catch (NoPermissionException e) {
//            fail("supposed to succeed but got exception :"+e.getMessage());
//        }
//
//        try {
//            user1.assignShopManager(shop1.getId(), toAssign);
//            toAssign.getAdministratorInfo(shop1.getId());
//            fail("do the transaction without a permission");
//        } catch (NoPermissionException ignore) {
//        }
    }

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