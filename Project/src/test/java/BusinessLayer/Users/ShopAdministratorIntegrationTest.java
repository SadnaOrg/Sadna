package BusinessLayer.Users;

import BusinessLayer.Products.Product;
import BusinessLayer.Shops.Shop;
import BusinessLayer.Users.BaseActions.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.naming.NoPermissionException;

import java.util.*;

import static org.junit.Assert.*;

public class ShopAdministratorIntegrationTest {
    private ShopOwner sa;
    private Shop shop;
    private SubscribedUser assignee;
    private SubscribedUser assignee1;
    private SubscribedUser sua;
    private Product product;
    @Before
    public void setUp(){
        sua = new SubscribedUser("ShopFounder","pass",new Date(2001, Calendar.DECEMBER,1));
        assignee = new SubscribedUser("assignee","new admin",new Date(2001, Calendar.DECEMBER,1));
        assignee1 = new SubscribedUser("assignee1","new admin!",new Date(2001, Calendar.DECEMBER,1));
        product = new Product(0,"test P",12.5,100);

        shop = new Shop(0,"test shop","test shop", sua);
        shop.addProduct(product);

        sa = new ShopOwner(shop, sua,"ShopFounder",true);
    }

    @After
    public void tearDown(){
        int id = shop.getId();
        ShopAdministrator admin = assignee.getAdministrator(id);
        if(admin != null){
            shop.removeAdmin(assignee.getUserName());
            sa.getAppoints().remove(admin);
            assignee.removeMyRole(id);
        }

        admin = assignee1.getAdministrator(id);
        if(admin != null){
            shop.removeAdmin(assignee1.getUserName());
            sa.getAppoints().remove(admin);
            assignee1.removeMyRole(id);
        }

        if(!shop.isOpen())
            shop.open();

        product.setQuantity(100);
    }

    @Test
    public void assignShopManagerSuccess() throws NoPermissionException {
        boolean assigned = sa.AssignShopManager(assignee);
        assertTrue(assigned);
    }

    @Test
    public void assignShopManagerFailure() throws NoPermissionException {
        assignShopManagerSuccess();
        try{
            assignee.getAdministrator(shop.getId()).AssignShopManager(assignee1);
            fail("assigned without permission!");
        }
        catch (Exception ignored){

        }
    }

    @Test
    public void assignShopOwnerSuccess() throws NoPermissionException {
        boolean assigned = sa.AssignShopOwner(assignee);
        assertTrue(assigned);
    }

    @Test
    public void assignShopOwnerFailure() throws NoPermissionException {
        assignShopManagerSuccess();
        try{
            assignee.getAdministrator(shop.getId()).AssignShopManager(assignee1);
            fail("assigned without permission!");
        }
        catch (Exception ignored){

        }
    }

    @Test
    public void changePermissionsSuccess() throws NoPermissionException {
        assignShopManagerSuccess();
        List<BaseActionType> permissions = new LinkedList<>();
        permissions.add(BaseActionType.ASSIGN_SHOP_MANAGER);
        permissions.add(BaseActionType.HISTORY_INFO);
        boolean updated = sa.ChangeManagerPermission(assignee,permissions);
        assertTrue(updated);
        ShopAdministrator admin = assignee.getAdministrator(shop.getId());
        assertNotNull(admin);
        Collection<BaseActionType> actions = admin.getActionsTypes();
        assertEquals(2,actions.size());
        assertTrue(actions.containsAll(permissions));
    }

    @Test
    public void changePermissionsFailure() throws NoPermissionException {
        assignShopManagerSuccess();
        ShopAdministrator admin = assignee.getAdministrator(shop.getId());
        assertNotNull(admin);
        try {
            admin.ChangeManagerPermission(sua, new LinkedList<>());
            fail("assigned wit");
        }
        catch (Exception ignored){

        }
    }

    @Test
    public void assignManagerByManager() throws NoPermissionException {
        changePermissionsSuccess();
        boolean assigned = assignee.getAdministrator(shop.getId()).AssignShopManager(assignee1);
        assertTrue(assigned);
        ShopAdministrator admin = assignee1.getAdministrator(shop.getId());
        assertNotNull(admin);
        Collection<BaseActionType> types = admin.getActionsTypes();
        assertNotNull(types);
        assertEquals(1,types.size());
        assertTrue(types.contains(BaseActionType.HISTORY_INFO));
    }

    @Test
    public void closeShopByFounder() throws NoPermissionException {
        boolean closed = sa.closeShop();
        assertTrue(closed);
    }

    @Test
    public void reopenShopByFounder() throws NoPermissionException {
        closeShopByFounder();
        sa.reOpenShop();
        assertTrue(shop.isOpen());
    }

    @Test
    public void removeAdminFromShop() throws NoPermissionException {
        assignShopManagerSuccess();
        boolean removed = sa.removeAdmin(assignee);
        assertTrue(removed);
        ShopAdministrator admin = assignee.getAdministrator(shop.getId());
        assertNull(admin);
    }

    @Test
    public void assignRemoveShopOwner() throws NoPermissionException {
        assignShopOwnerSuccess();
        ShopOwner admin = (ShopOwner) assignee.getAdministrator(shop.getId());
        int id = shop.getId();
        admin.AssignShopManager(assignee1);
        sa.removeAdmin(assignee);
        ShopAdministrator adminAssignee = assignee.getAdministrator(id);
        ShopAdministrator adminAssignee1 = assignee1.getAdministrator(id);
        assertNull(adminAssignee);
        assertNull(adminAssignee1);
        adminAssignee = shop.getShopAdministrator(assignee.getUserName());
        adminAssignee1 = shop.getShopAdministrator(assignee1.getUserName());
        assertNull(adminAssignee);
        assertNull(adminAssignee1);
    }

    @Test
    public void stockManagementChangeProductQuantitySuccess() throws NoPermissionException {
        boolean updated = sa.changeProductQuantity(0,25);
        assertTrue(updated);
        Product p = shop.getProducts().get(0);
        assertNotNull(p);
        assertEquals(25,p.getQuantity());
    }

    @Test
    public void stockManagementRemoveProductFromShop() throws NoPermissionException {
        sa.removeProduct(0);
        Product p = shop.getProducts().get(0);
        assertNull(p);
    }

    @Test
    public void stockManagementRemoveProductNotInShop() {
        try {
            sa.removeProduct(2);
            fail("removed product not in shop");
        }
        catch (Exception ignored){

        }
    }

    @Test
    public void changeProductName() throws NoPermissionException {
        boolean renamed = sa.changeProductName(0,"new name");
        assertTrue(renamed);
        Product p = shop.getProducts().get(0);
        assertNotNull(p);
        assertEquals("new name", p.getName());
    }

    @Test
    public void stockManagementChangeProductQuantitySuccessByOwner() throws NoPermissionException {
        assignShopOwnerSuccess();
        ShopOwner owner = (ShopOwner) assignee.getAdministrator(shop.getId());
        boolean updated = owner.changeProductQuantity(0,25);
        assertTrue(updated);
        Product p = shop.getProducts().get(0);
        assertNotNull(p);
        assertEquals(25,p.getQuantity());
    }

    @Test
    public void stockManagementRemoveProductFromShopByOwner() throws NoPermissionException {
        assignShopOwnerSuccess();
        ShopOwner owner = (ShopOwner) assignee.getAdministrator(shop.getId());
        owner.removeProduct(0);
        Product p = shop.getProducts().get(0);
        assertNull(p);
    }

    @Test
    public void stockManagementRemoveProductNotInShopByOwner() {
        try {
            assignShopOwnerSuccess();
            ShopOwner owner = (ShopOwner) assignee.getAdministrator(shop.getId());
            owner.removeProduct(2);
            fail("removed product not in shop");
        }
        catch (Exception ignored){

        }
    }

    @Test
    public void changeProductNameByOwner() throws NoPermissionException {
        assignShopOwnerSuccess();
        ShopOwner owner = (ShopOwner) assignee.getAdministrator(shop.getId());
        boolean renamed = owner.changeProductName(0,"new name");
        assertTrue(renamed);
        Product p = shop.getProducts().get(0);
        assertNotNull(p);
        assertEquals("new name", p.getName());
    }

}
