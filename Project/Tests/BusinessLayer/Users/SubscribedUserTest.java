package BusinessLayer.Users;

import BusinessLayer.Shops.Shop;
import BusinessLayer.Users.BaseActions.BaseActionType;
import org.junit.Before;
import org.junit.Test;

import javax.naming.NoPermissionException;

import static org.junit.Assert.*;

public class SubscribedUserTest {
    private SubscribedUser user1;
    private SubscribedUser user2;
    private SubscribedUser toAssign;
    private Shop shop1;
    private Shop shop2;
    ShopManager sm1;
    ShopOwner so1;
    @Before
    public void setUp() {
        user1 = new SubscribedUser("user1");
        user2 = new SubscribedUser("user2");
        toAssign = new SubscribedUser("toAssign");
        shop1 = new Shop(1,"shop1");
        shop2 = new Shop(2,"shop2");

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
                fail("do the transaction with out a premmission");
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
        BaseActionType[] types = new BaseActionType[]{BaseActionType.ASSIGN_SHOP_MANAGER};
        try {
            user1.changeManagerPermission(shop1.getId(), user2, new BaseActionType[]{});
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
            user2.changeManagerPermission(shop1.getId(), user1, new BaseActionType[]{});
            fail("do the transaction with out a permission");
        } catch (NoPermissionException ignore) {
            assertNotEquals(0, user2.getAdministrator(shop1.getId()).action.size());
        }
    }
}