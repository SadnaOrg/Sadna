package BusinessLayer.Users;

import BusinessLayer.Shops.Shop;
import BusinessLayer.Users.BaseActions.BaseActionType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.naming.NoPermissionException;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class SubscribedUserTest {
    private SubscribedUser user1;
    private SubscribedUser user2;
    private SubscribedUser toAssign;
    private SubscribedUser founder;
    private Shop shop1;
    private Shop shop2;
    ShopManager sm1;
    ShopOwner so1;
    @BeforeAll
    public void setUp() {
        user1 = new SubscribedUser("user1","pass12");
        user2 = new SubscribedUser("user2","pass12");
        toAssign = new SubscribedUser("toAssign","pass12");
        founder = new SubscribedUser("Founder Guy","pass12");
        shop1 = new Shop(1,"shop1", founder);
        shop2 = new Shop(2,"shop2", founder);

        sm1 = new ShopManager(shop1, user1);
        so1 = new ShopOwner(shop1, user1, true);
        sm1.AddAction(BaseActionType.ASSIGN_SHOP_MANAGER);
    }

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