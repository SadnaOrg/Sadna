package AcceptanceTests.Tests;

import AcceptanceTests.Bridge.SubscribedUserBridge;
import AcceptanceTests.Bridge.SubscribedUserProxy;
import AcceptanceTests.Bridge.UserProxy;
import AcceptanceTests.DataObjects.*;
import AcceptanceTests.Threads.*;
import ServiceLayer.Objects.Policies.Discount.*;
import org.junit.*;

import javax.net.ssl.SSLContext;
import java.time.LocalTime;
import java.util.*;

import static AcceptanceTests.DataObjects.SubscribedUser.Permission.REMOVE_SHOP_OWNER;
import static org.junit.Assert.*;

public class SubscribedUserTests extends UserTests {
    private static SubscribedUserBridge subscribedUserBridge = null;

    private static Shop supersal;
    private boolean closeSupersal = false;
    private boolean deleteCastro222 = false;
    private boolean removeU1OwnerCastro = false;
    private boolean removeU1ManagerCastro = false;
    private int removeU3ManagerCastro = -1;
    private int policyID = -1;
    private boolean removeCastro = false;
    private  int purchasePolicyID = -1;

    private final static String[] userNames = new String[]{"testUser3","buyer100","michael","superfounder"};
    private final static String[] passwords = new String[]{"42","secret","leahcim","superpassword"};

    private final static SubscribedUser.Permission[] defaultFounderPermissions = new SubscribedUser.Permission[]{
            SubscribedUser.Permission.STOCK_MANAGEMENT, SubscribedUser.Permission.SET_PURCHASE_POLICY, SubscribedUser.Permission.ASSIGN_SHOP_OWNER,
            SubscribedUser.Permission.ASSIGN_SHOP_MANAGER, SubscribedUser.Permission.CHANGE_MANAGER_PERMISSION,
            SubscribedUser.Permission.CLOSE_SHOP, SubscribedUser.Permission.REOPEN_SHOP, SubscribedUser.Permission.ROLE_INFO,
            SubscribedUser.Permission.HISTORY_INFO, SubscribedUser.Permission.REMOVE_ADMIN,REMOVE_SHOP_OWNER
    };

    private final static SubscribedUser.Permission[] defaultOwnerPermissions = new SubscribedUser.Permission[]{
            SubscribedUser.Permission.STOCK_MANAGEMENT, SubscribedUser.Permission.SET_PURCHASE_POLICY, SubscribedUser.Permission.ASSIGN_SHOP_OWNER,
            SubscribedUser.Permission.ASSIGN_SHOP_MANAGER, SubscribedUser.Permission.CHANGE_MANAGER_PERMISSION,
            SubscribedUser.Permission.ROLE_INFO,
            SubscribedUser.Permission.HISTORY_INFO, SubscribedUser.Permission.REMOVE_ADMIN,REMOVE_SHOP_OWNER
    };

    private static SubscribedUser u1;
    private static SubscribedUser u2;
    private static SubscribedUser u3;
    private static SubscribedUser supersalFounder;

    public static String getU3Name(){
        return u3.name;
    }

    public static SubscribedUserBridge getUserBridge(){return subscribedUserBridge;}

    @BeforeClass
    public static void setUp(){
        UserTests.setUp();
        subscribedUserBridge = new SubscribedUserProxy((UserProxy) UserTests.getUserBridge());
        Guest setUpU1 = subscribedUserBridge.visit();
        Guest setUpU2 = subscribedUserBridge.visit();
        Guest setUpU3 = subscribedUserBridge.visit();
        Guest setUpSupersal = subscribedUserBridge.visit();
        Guest setUpEnter = subscribedUserBridge.visit();

        RegistrationInfo u1Reg = new RegistrationInfo(userNames[0], passwords[0]);
        RegistrationInfo u2Reg = new RegistrationInfo(userNames[1],passwords[1]);
        RegistrationInfo u3Reg = new RegistrationInfo(userNames[2],passwords[2]);
        RegistrationInfo supersalReg= new RegistrationInfo(userNames[3], passwords[3]);
        RegistrationInfo enterReg = new RegistrationInfo( "enterUser","enterPass");

        subscribedUserBridge.register(setUpU1.name,u1Reg);
        subscribedUserBridge.register(setUpU2.name,u2Reg);
        subscribedUserBridge.register(setUpU3.name,u3Reg);
        subscribedUserBridge.register(setUpSupersal.name,supersalReg);
        subscribedUserBridge.register(setUpEnter.name,enterReg);

        u1 = subscribedUserBridge.login(setUpU1.getName(),u1Reg);
        u2 = subscribedUserBridge.login(setUpU2.getName(), u2Reg);
        u3 = subscribedUserBridge.login(setUpU3.getName(), u3Reg);
        supersalFounder = subscribedUserBridge.login(setUpSupersal.name, supersalReg);

        subscribedUserBridge.registerToNotifier(u1.name);
        subscribedUserBridge.registerToNotifier(u2.name);
        subscribedUserBridge.registerToNotifier(u3.name);
        subscribedUserBridge.registerToNotifier(supersalFounder.name);

        subscribedUserBridge.exit(u1.name);
        subscribedUserBridge.exit(u2.name);
        subscribedUserBridge.exit(u3.name);
        subscribedUserBridge.exit(supersalFounder.name);
        subscribedUserBridge.exit(setUpEnter.name);
    }

    @Override
    @Before
    public void setUpTest(){
        super.setUpTest();
        Guest ace_guest =subscribedUserBridge.visit();
        Guest castro_guest = subscribedUserBridge.visit();
        Guest megasport_guest = subscribedUserBridge.visit();
        Guest setUpU1 = subscribedUserBridge.visit();
        Guest setUpU2 = subscribedUserBridge.visit();
        Guest setUpU3 = subscribedUserBridge.visit();
        Guest setUpSupersal = subscribedUserBridge.visit();

        RegistrationInfo u1Reg = new RegistrationInfo(userNames[0], passwords[0]);
        RegistrationInfo u2Reg = new RegistrationInfo(userNames[1],passwords[1]);
        RegistrationInfo u3Reg = new RegistrationInfo(userNames[2],passwords[2]);
        RegistrationInfo supersalReg= new RegistrationInfo(userNames[3], passwords[3]);
        RegistrationInfo ace = new RegistrationInfo("ACEFounder","ACE_rocks");
        RegistrationInfo castro = new RegistrationInfo("castroFounder","castro_rocks");
        RegistrationInfo megaSport = new RegistrationInfo("MegaSportFounder","MegaSport_rocks");

        u1 = subscribedUserBridge.login(setUpU1.getName(),u1Reg);
        u2 = subscribedUserBridge.login(setUpU2.getName(), u2Reg);
        u3 = subscribedUserBridge.login(setUpU3.getName(), u3Reg);
        supersalFounder = subscribedUserBridge.login(setUpSupersal.name, supersalReg);
        ACEFounder = subscribedUserBridge.login(ace_guest.getName(),ace);
        castroFounder = subscribedUserBridge.login(castro_guest.getName(), castro);
        MegaSportFounder = subscribedUserBridge.login(megasport_guest.getName(), megaSport);
    }

    @Override
    @After
    public void tearDown(){
        super.tearDown();
        if(closeSupersal) {
            subscribedUserBridge.closeShop(supersal.ID, supersalFounder.name);
            closeSupersal = false;
        }
        if(deleteCastro222){
            subscribedUserBridge.deleteProductFromShop(castroFounder.name, shops[castro_ID].ID,222);
            deleteCastro222 = false;
        }
        if(removeU1OwnerCastro){
            subscribedUserBridge.removeAdmin(shops[castro_ID].ID, castroFounder.name, u1.name);
            removeU1OwnerCastro = false;
        }
        if(removeU1ManagerCastro){
            subscribedUserBridge.removeAdmin(shops[castro_ID].ID, castroFounder.name, u1.name);
            removeU1ManagerCastro = false;
        }
        if(removeU3ManagerCastro == 0){
            subscribedUserBridge.removeAdmin(shops[castro_ID].ID, u1.name, u3.name);
            removeU3ManagerCastro = -1;
        }
        else if(removeU3ManagerCastro == 1){
            subscribedUserBridge.removeAdmin(shops[castro_ID].ID, castroFounder.name, u3.name);
            removeU3ManagerCastro = -1;
        }

        if(purchasePolicyID != -1)
            subscribedUserBridge.removePurchasePolicy(ACEFounder.name, purchasePolicyID,shops[ACE_ID].ID);

        if(policyID != -1)
            subscribedUserBridge.removeDiscount(ACEFounder.name, policyID,shops[ACE_ID].ID);

        if(removeCastro){
            subscribedUserBridge.removeDiscount(castroFounder.name, policyID,shops[castro_ID].ID);
            removeCastro = false;
        }

        subscribedUserBridge.getNotifications(u1.name);
        subscribedUserBridge.getNotifications(u2.name);
        subscribedUserBridge.getNotifications(u3.name);
        subscribedUserBridge.getNotifications(supersalFounder.name);
        subscribedUserBridge.getNotifications(ACEFounder.name);
        subscribedUserBridge.getNotifications(MegaSportFounder.name);
        subscribedUserBridge.getNotifications(castroFounder.name);

        subscribedUserBridge.exit(u1.name);
        subscribedUserBridge.exit(u2.name);
        subscribedUserBridge.exit(u3.name);
        subscribedUserBridge.exit(supersalFounder.name);
        subscribedUserBridge.exit(MegaSportFounder.name);
        subscribedUserBridge.exit(castroFounder.name);
        subscribedUserBridge.exit(ACEFounder.name);
    }

    @Test
    public void testExitOldUser() {
        assertEquals("testUser3",u1.name);
    }

    @Test
    public void testExitNewUserWithItems() {
        String testUserName = "newRegistered";
        String testPassword = "testExit";

        Guest newGuest = subscribedUserBridge.visit();
        userBridge.register(newGuest.name,new RegistrationInfo(testUserName, testPassword));
        SubscribedUser u = userBridge.login(newGuest.name, new RegistrationInfo(testUserName, testPassword));
        boolean added = subscribedUserBridge.addProductToCart(u.name,shops[castro_ID].ID,2,10);
        assertTrue(added);

        Guest newG = subscribedUserBridge.logout(u.name);

        u = subscribedUserBridge.login(newG.name,new RegistrationInfo(testUserName, testPassword));
        ShoppingCart userCart = subscribedUserBridge.checkCart(u.name);
        assertNotNull(userCart);
        assertEquals(10,userCart.numOfProductsInCart());
        assertEquals(1,userCart.getNumberOfBaskets());

        ShopBasket basket = userCart.getShopBasket(shops[castro_ID].ID);
        assertNotNull(basket);

        int quantity = basket.getProductQuantity(2);
        assertEquals(10,quantity);

        //cancel side-effects
        subscribedUserBridge.updateCart(u.name,2,shops[castro_ID].ID,0);

        boolean exitResult = subscribedUserBridge.exit(u.name);
        assertTrue(exitResult);

    }

    @Test
    public void testEnterNewUserSuccess() {
        Guest newGuest = subscribedUserBridge.visit();
        boolean res = subscribedUserBridge.register(newGuest.name,new RegistrationInfo("newName","newPassword"));
        assertTrue(res);
        SubscribedUser u = subscribedUserBridge.login(newGuest.name, new RegistrationInfo("newName","newPassword"));
        assertNotNull(u);
        assertEquals("newName",u.name);

        boolean exitResult = subscribedUserBridge.exit(u.name);
        assertTrue(exitResult);
    }

    @Test
    public void testEnterNewUserFailure() {
        Guest newGuest = subscribedUserBridge.visit();
        boolean res = subscribedUserBridge.register(newGuest.name,new RegistrationInfo(u1.name, passwords[0]));
        assertFalse(res);
        res = subscribedUserBridge.exit(newGuest.name);
        assertTrue(res);
    }

    @Test
    public void testOpenShopSuccess(){
        supersal = subscribedUserBridge.openShop(supersalFounder.name,"supersal","food");

        assertNotNull(supersal);
        assertEquals("supersal",supersal.name);
        assertEquals("food",supersal.desc);

        Map<String,Appointment> appointments = subscribedUserBridge.getShopAppointments(supersalFounder.name,supersal.ID);
        assertTrue(appointments.containsKey(supersalFounder.name));
        Appointment appointment = appointments.get(supersalFounder.name);
        assertEquals(appointment.appointer,supersalFounder.name);
        assertEquals(appointment.getRole(), Appointment.Role.FOUNDER);
        closeSupersal = true; // for correct teardown
    }

    @Test
    public void testAddProductToShopByFounderSuccess(){
        Product newproduct = new Product("belt","brown","china");

        boolean added = subscribedUserBridge.addProductToShop(castroFounder.name,shops[castro_ID].ID,newproduct,222,12,67.5);
        assertTrue(added);
        ProductInShop pis  = subscribedUserBridge.searchProductInShop(castroFounder.name ,222,shops[castro_ID].ID);

        assertNotNull(pis);
        assertEquals(222,pis.ID);
        assertEquals(12,pis.quantity);
        assertEquals(67.5, pis.price, 0.0);
        deleteCastro222 = true; // for correct teardown
    }

    @Test
    public void testAddProductToShopByFounderFailureBadQuantity(){
        Product newproduct = new Product("belt","brown","china");

        boolean added = subscribedUserBridge.addProductToShop(castroFounder.name,shops[castro_ID].ID,newproduct,221,-12,67.5);
        assertFalse(added);

        ProductInShop pis  = subscribedUserBridge.searchProductInShop(castroFounder.name, 221,shops[castro_ID].ID);
        assertNull(pis);
    }

    @Test
    public void testAddProductToShopByFounderFailureBadPrice(){
        Product newproduct = new Product("belt","brown","china");

        boolean added = subscribedUserBridge.addProductToShop(castroFounder.name,shops[castro_ID].ID,newproduct,221,12,-12);
        assertFalse(added);

        ProductInShop pis  = subscribedUserBridge.searchProductInShop(castroFounder.name, 221,shops[castro_ID].ID);
        assertNull(pis);
    }

    @Test
    public void testAddProductToShopByFounderFailureBadPriceBadQuantity(){
        Product newproduct = new Product("belt","brown","china");

        boolean added = subscribedUserBridge.addProductToShop(castroFounder.name,shops[castro_ID].ID,newproduct,221,-12,-12);
        assertFalse(added);

        ProductInShop pis  = subscribedUserBridge.searchProductInShop(castroFounder.name, 221,shops[castro_ID].ID);
        assertNull(pis);
    }

    @Test
    public void testDeleteProductFromShopByOwnerSuccess(){
        testAddProductToShopByFounderSuccess();
;
        boolean deleted = subscribedUserBridge.deleteProductFromShop(ACEFounder.name,shops[castro_ID].ID,222);
        assertTrue(deleted);

        ProductInShop pis  = subscribedUserBridge.searchProductInShop(ACEFounder.name, 222,shops[castro_ID].ID);
        assertNull(pis);
        deleteCastro222 = false;

        List<Notification> notifications = subscribedUserBridge.getNotifications(ACEFounder.name);
        assertNotNull(notifications);
        assertEquals(1,notifications.size());
        assertEquals("product 222 from shop 1 got deleted",notifications.get(0).getContent());

        notifications = subscribedUserBridge.getNotifications(MegaSportFounder.name);
        assertNotNull(notifications);
        assertEquals(1,notifications.size());
        assertEquals("product 222 from shop 1 got deleted",notifications.get(0).getContent());
    }

    @Test
    public void testDeleteProductFromShopByOwnerFailureBadID(){
        boolean deleted = subscribedUserBridge.deleteProductFromShop(ACEFounder.name,shops[castro_ID].ID,5);
        assertFalse(deleted);

        ProductInShop pis  = subscribedUserBridge.searchProductInShop(ACEFounder.name, 5,shops[castro_ID].ID);
        assertNull(pis);

        List<Notification> notifications = subscribedUserBridge.getNotifications(ACEFounder.name);
        assertNotNull(notifications);
        assertEquals(0,notifications.size());

        notifications = subscribedUserBridge.getNotifications(MegaSportFounder.name);
        assertNotNull(notifications);
        assertEquals(0,notifications.size());
    }

    @Test
    public void testUpdateProductIncreasePriceByFounderSuccess(){
        boolean updated =subscribedUserBridge.updateProductPrice(castroFounder.name,shops[castro_ID].ID,45,120);
        assertTrue(updated);

        ProductInShop pis  = subscribedUserBridge.searchProductInShop(castroFounder.name, 45,shops[castro_ID].ID);
        assertNotNull(pis);
        assertEquals(45,pis.ID);
        assertEquals(40,pis.quantity);
        assertEquals(120,pis.price,0);

        List<Notification> notifications = subscribedUserBridge.getNotifications(castroFounder.name);
        assertNotNull(notifications);
        assertEquals(1,notifications.size());
        assertEquals("product 45 from shop 1 price has change to: 120.0",notifications.get(0).getContent());

        notifications = subscribedUserBridge.getNotifications(MegaSportFounder.name);
        assertNotNull(notifications);
        assertEquals(1,notifications.size());
        assertEquals("product 45 from shop 1 price has change to: 120.0",notifications.get(0).getContent());

        notifications = subscribedUserBridge.getNotifications(ACEFounder.name);
        assertNotNull(notifications);
        assertEquals(1,notifications.size());
        assertEquals("product 45 from shop 1 price has change to: 120.0",notifications.get(0).getContent());

        testUpdateKeepProductPriceByFounderSuccess();
    }

    @Test
    public void testUpdateProductDecreasePriceByFounderSuccess(){
        boolean updated =subscribedUserBridge.updateProductPrice(castroFounder.name,shops[castro_ID].ID,45,12.5);
        assertTrue(updated);

        ProductInShop pis  = subscribedUserBridge.searchProductInShop(castroFounder.name, 45,shops[castro_ID].ID);
        assertNotNull(pis);
        assertEquals(45,pis.ID);
        assertEquals(40,pis.quantity);
        assertEquals(12.5,pis.price,0);

        List<Notification> notifications = subscribedUserBridge.getNotifications(castroFounder.name);
        assertNotNull(notifications);
        assertEquals(1,notifications.size());
        assertEquals("product 45 from shop 1 price has change to: 12.5",notifications.get(0).getContent());

        notifications = subscribedUserBridge.getNotifications(MegaSportFounder.name);
        assertNotNull(notifications);
        assertEquals(1,notifications.size());
        assertEquals("product 45 from shop 1 price has change to: 12.5",notifications.get(0).getContent());

        notifications = subscribedUserBridge.getNotifications(ACEFounder.name);
        assertNotNull(notifications);
        assertEquals(1,notifications.size());
        assertEquals("product 45 from shop 1 price has change to: 12.5",notifications.get(0).getContent());

        testUpdateKeepProductPriceByFounderSuccess();
    }

    @Test
    public void testUpdateKeepProductPriceByFounderSuccess(){
        boolean updated =subscribedUserBridge.updateProductPrice(castroFounder.name,shops[castro_ID].ID,45,50);
        assertTrue(updated);

        ProductInShop pis  = subscribedUserBridge.searchProductInShop(castroFounder.name, 45,shops[castro_ID].ID);
        assertNotNull(pis);
        assertEquals(45,pis.ID);
        assertEquals(40,pis.quantity);
        assertEquals(50,pis.price,0);
        List<Notification> notifications = subscribedUserBridge.getNotifications(castroFounder.name);
        assertNotNull(notifications);
        assertEquals(1,notifications.size());
        assertEquals("product 45 from shop 1 price has change to: 50.0",notifications.get(0).getContent());

        notifications = subscribedUserBridge.getNotifications(MegaSportFounder.name);
        assertNotNull(notifications);
        assertEquals(1,notifications.size());
        assertEquals("product 45 from shop 1 price has change to: 50.0",notifications.get(0).getContent());

        notifications = subscribedUserBridge.getNotifications(ACEFounder.name);
        assertNotNull(notifications);
        assertEquals(1,notifications.size());
        assertEquals("product 45 from shop 1 price has change to: 50.0",notifications.get(0).getContent());
    }

    @Test
    public void testUpdateProductByOwnerFailureBadID(){
        boolean updated = subscribedUserBridge.updateProductName(MegaSportFounder.name,shops[ACE_ID].ID,3,"fail");
        assertFalse(updated);

        ProductInShop pis  = subscribedUserBridge.searchProductInShop(MegaSportFounder.name, 3,shops[ACE_ID].ID);
        assertNull(pis);

        List<Notification> notifications = subscribedUserBridge.getNotifications(MegaSportFounder.name);
        assertNotNull(notifications);
        assertEquals(0,notifications.size());

        notifications = subscribedUserBridge.getNotifications(ACEFounder.name);
        assertNotNull(notifications);
        assertEquals(0,notifications.size());
    }

    @Test
    public void testUpdateProductByOwnerFailureBadPrice(){
        boolean updated =subscribedUserBridge.updateProductPrice(MegaSportFounder.name,shops[ACE_ID].ID,1,-50);
        assertFalse(updated);

        ProductInShop pis  = subscribedUserBridge.searchProductInShop(MegaSportFounder.name, 1,shops[ACE_ID].ID);
        assertNotNull(pis);
        assertEquals(pis.ID,1);
        assertEquals(pis.price,25,0);

        List<Notification> notifications = subscribedUserBridge.getNotifications(MegaSportFounder.name);
        assertNotNull(notifications);
        assertEquals(0,notifications.size());

        notifications = subscribedUserBridge.getNotifications(ACEFounder.name);
        assertNotNull(notifications);
        assertEquals(0,notifications.size());
    }

    @Test
    public void testUpdateProductByOwnerFailureBadPriceBadID(){
        boolean updated =subscribedUserBridge.updateProductPrice(MegaSportFounder.name,shops[ACE_ID].ID,11,-50);
        assertFalse(updated);

        ProductInShop pis  = subscribedUserBridge.searchProductInShop(MegaSportFounder.name, 11,shops[ACE_ID].ID);
        assertNull(pis);

        List<Notification> notifications = subscribedUserBridge.getNotifications(MegaSportFounder.name);
        assertNotNull(notifications);
        assertEquals(0,notifications.size());

        notifications = subscribedUserBridge.getNotifications(ACEFounder.name);
        assertNotNull(notifications);
        assertEquals(0,notifications.size());
    }

    @Test
    public void testUpdateProductDescriptionSuccess(){
        boolean updated = subscribedUserBridge.updateProductDescription(ACEFounder.name, shops[ACE_ID].ID,0,"new desc!");
        assertTrue(updated);

        ProductInShop p = subscribedUserBridge.searchProductInShop(ACEFounder.name, 0,shops[ACE_ID].ID);
        assertEquals(p.shopID, shops[ACE_ID].ID);
        assertEquals(p.desc,"new desc!");
        assertEquals(p.ID,0);

        List<Notification> notifications = subscribedUserBridge.getNotifications(MegaSportFounder.name);
        assertNotNull(notifications);
        assertEquals(1,notifications.size());
        assertEquals("product 0 from shop 0 description has change to: new desc!",notifications.get(0).getContent());

        notifications = subscribedUserBridge.getNotifications(ACEFounder.name);
        assertNotNull(notifications);
        assertEquals(1,notifications.size());
        assertEquals("product 0 from shop 0 description has change to: new desc!",notifications.get(0).getContent());

        testUpdateProductDescriptionSuccessKeepSame();
    }

    @Test
    public void testUpdateProductDescriptionSuccessKeepSame(){
        boolean updated = subscribedUserBridge.updateProductDescription(ACEFounder.name, shops[ACE_ID].ID,0,"recommended");
        assertTrue(updated);

        ProductInShop p = subscribedUserBridge.searchProductInShop(ACEFounder.name, 0,shops[ACE_ID].ID);
        assertEquals(p.shopID, shops[ACE_ID].ID);
        assertEquals(p.desc,"recommended");
        assertEquals(p.ID,0);
    }

    @Test
    public void testUpdateProductDescriptionFailureNoPermissionsToManager(){
        boolean updated = subscribedUserBridge.updateProductDescription(MegaSportFounder.name, shops[castro_ID].ID,2,"new desc!");
        assertFalse(updated);

        ProductInShop p = subscribedUserBridge.searchProductInShop(castroFounder.name, 2,shops[castro_ID].ID);
        assertEquals("recommended",p.desc);
        assertEquals(shops[castro_ID].ID,p.shopID);
        assertEquals(2,p.ID);
    }

    @Test
    public void testUpdateProductNameSuccess(){
        boolean update = subscribedUserBridge.updateProductName(castroFounder.name, shops[castro_ID].ID,45,"new name!");
        assertTrue(update);

        ProductInShop p = subscribedUserBridge.searchProductInShop(castroFounder.name, 45,shops[castro_ID].ID);
        assertEquals("new name!",p.product.name);
        testUpdateProductNameKeepSame();

    }

    @Test
    public void testUpdateProductNameKeepSame(){
        boolean update = subscribedUserBridge.updateProductName(castroFounder.name, shops[castro_ID].ID,45,"recommended");
        assertTrue(update);

        ProductInShop p = subscribedUserBridge.searchProductInShop(castroFounder.name, 45,shops[castro_ID].ID);
        assertEquals("recommended",p.product.name);
    }

    @Test
    public void testUpdateProductNameFailureNoPermissions(){
        boolean update = subscribedUserBridge.updateProductName(MegaSportFounder.name, shops[castro_ID].ID,45,"failed");
        assertFalse(update);

        ProductInShop p = subscribedUserBridge.searchProductInShop(castroFounder.name, 45,shops[castro_ID].ID);
        assertEquals("shoes",p.product.name);
    }

    @Test
    public void testUpdateProductQuantitySuccess(){
        boolean update = subscribedUserBridge.updateProductQuantity(MegaSportFounder.name,shops[MegaSport_ID].ID,13,100);
        assertTrue(update);

        ProductInShop p = subscribedUserBridge.searchProductInShop(MegaSportFounder.name, 13,shops[MegaSport_ID].ID);
        assertEquals(100,p.quantity);
        assertEquals(shops[MegaSport_ID].ID,p.shopID);
        assertEquals(13,p.ID);
        testUpdateProductQuantityKeepSame();
    }

    @Test
    public void testUpdateProductQuantityKeepSame(){
        boolean update = subscribedUserBridge.updateProductQuantity(MegaSportFounder.name,shops[MegaSport_ID].ID,13,30);
        assertTrue(update);

        ProductInShop p = subscribedUserBridge.searchProductInShop(MegaSportFounder.name, 13,shops[MegaSport_ID].ID);
        assertEquals(30,p.quantity);
        assertEquals(shops[MegaSport_ID].ID,p.shopID);
        assertEquals(13,p.ID);
    }

    @Test
    public void testUpdateProductQuantityFailureBadQuantity(){
        boolean update = subscribedUserBridge.updateProductQuantity(MegaSportFounder.name,shops[MegaSport_ID].ID,13,-30);
        assertFalse(update);

        ProductInShop p = subscribedUserBridge.searchProductInShop(MegaSportFounder.name, 13,shops[MegaSport_ID].ID);
        assertEquals(30,p.quantity);
        assertEquals(shops[MegaSport_ID].ID,p.shopID);
        assertEquals(13,p.ID);
    }

    @Test
    public void testAppointShopOwnerSuccess(){
        boolean result = subscribedUserBridge.appointOwner(shops[castro_ID].ID,castroFounder.name,u1.name);
        assertTrue(result);

        Map<String,Appointment> roles = subscribedUserBridge.getShopAppointments(u1.name,shops[castro_ID].ID);
        assertNotNull(roles);
        Appointment appointment = roles.getOrDefault(u1.name,null);
        assertNotNull(appointment);
        assertEquals(Appointment.Role.OWNER,appointment.getRole());
        assertEquals(castroFounder.name,appointment.appointer);
        removeU1OwnerCastro = true;
    }

    @Test
    public void testAppointShopOwnerByOwnerSuccess(){
        testAppointShopOwnerSuccess();

        boolean result = subscribedUserBridge.appointOwner(shops[castro_ID].ID,u1.name,u2.name);
        assertTrue(result);

        Map<String,Appointment> roles = subscribedUserBridge.getShopAppointments(u2.name,shops[castro_ID].ID);
        assertNotNull(roles);
        Appointment appointment = roles.getOrDefault(u2.name,null);
        assertNotNull(appointment);
        assertEquals(Appointment.Role.OWNER,appointment.getRole());
        assertEquals(u1.name,appointment.getAppointer());
    }

    @Test
    public void testAppointShopOwnerFailureDoubleAppointment(){
        try{
            testAppointShopOwnerSuccess();
            testAppointShopOwnerSuccess();
            throw new IllegalStateException("double appointment!");
        }
        catch (AssertionError ignored){

        }
    }

    @Test
    public void testAppointShopOwnerFailureAppointmentByGuest(){
        Guest newGuest = subscribedUserBridge.visit();

        boolean result = subscribedUserBridge.appointOwner(shops[castro_ID].ID,newGuest.name,u1.name);
        assertFalse(result);

        boolean exitResult = subscribedUserBridge.exit(newGuest.name);
        assertTrue(exitResult);

        Map<String,Appointment> appointmentMap = subscribedUserBridge.getShopAppointments(castroFounder.name,shops[castro_ID].ID);
        assertNotNull(appointmentMap);
        Appointment appointment = appointmentMap.getOrDefault(u1.name,null);
        assertNull(appointment);
    }

    @Test
    public void testAppointShopOwnerFailureAppointmentByManagerNotOwner(){
        boolean result = subscribedUserBridge.appointOwner(shops[castro_ID].ID,MegaSportFounder.name,u1.name);
        assertFalse(result);

        Map<String,Appointment> appointmentMap = subscribedUserBridge.getShopAppointments(castroFounder.name,shops[castro_ID].ID);
        assertNotNull(appointmentMap);
        Appointment appointment = appointmentMap.getOrDefault(u1.name,null);
        assertNull(appointment);
    }

    @Test
    public void testAppointShopManagerSuccess(){
        boolean result = subscribedUserBridge.appointManager(shops[castro_ID].ID,castroFounder.name,u1.name);
        assertTrue(result);

        Map<String,Appointment> appointmentMap = subscribedUserBridge.getShopAppointments(castroFounder.name,shops[castro_ID].ID);
        assertNotNull(appointmentMap);
        Appointment appointment = appointmentMap.getOrDefault(u1.name,null);
        assertNotNull(appointment);
        assertEquals(Appointment.Role.MANAGER,appointment.getRole());
        assertEquals(castroFounder.name,appointment.appointer);
        removeU1ManagerCastro = true;
    }

    @Test
    public void testConcurrentManagerAppointment() throws InterruptedException {
        testAppointShopOwnerSuccess(); // u1 is owner at castro
        Thread MegaSportFounderAppointment = new FounderAppointManager(shops[castro_ID].ID,castroFounder);
        Thread ACEFounderAppointment = new OwnerAppointManager(shops[castro_ID].ID,u1);

        MegaSportFounderAppointment.start();
        ACEFounderAppointment.start();
        MegaSportFounderAppointment.join();
        ACEFounderAppointment.join();

        Map<String ,Appointment> appointments = subscribedUserBridge.getShopAppointments(u1.name,shops[castro_ID].ID);
        assertNotNull(appointments);
        assertEquals(5,appointments.size());

        Appointment founderAppointment = appointments.getOrDefault(castroFounder.name,null);
        Appointment ownerAppointment = appointments.getOrDefault(u1.name,null);
        Appointment managerAppointment = appointments.getOrDefault(u3.name,null);

        assertNotNull(founderAppointment);
        assertNotNull(ownerAppointment);
        assertNotNull(managerAppointment);

        assertEquals(castroFounder.name,founderAppointment.appointer);
        assertEquals(castroFounder.name,ownerAppointment.appointer);
        assertTrue(Objects.equals(managerAppointment.appointer, u1.name) || Objects.equals(managerAppointment.appointer, castroFounder.name));

        assertEquals(Appointment.Role.FOUNDER,founderAppointment.role);
        assertEquals(Appointment.Role.OWNER,ownerAppointment.role);
        assertEquals(Appointment.Role.MANAGER,managerAppointment.role);

        if(Objects.equals(managerAppointment.appointer, u1.name))
            removeU3ManagerCastro = 0;
        else removeU3ManagerCastro = 1;
    }

    @Test
    public void testAppointShopManagerFailureAlreadyManager(){
        try{
            testAppointShopManagerSuccess();
            testAppointShopManagerSuccess();
            fail();
        } catch (AssertionError ignored) {

        }
    }

    @Test
    public void testAppointShopManagerFailureNotOwner(){
        boolean result = subscribedUserBridge.appointManager(shops[MegaSport_ID].ID,castroFounder.name,u1.name);
        assertFalse(result);

        Map<String,Appointment> appointmentMap = subscribedUserBridge.getShopAppointments(MegaSportFounder.name,shops[MegaSport_ID].ID);
        assertNotNull(appointmentMap);
        Appointment appointment = appointmentMap.getOrDefault(u1.name,null);
        assertNull(appointment);
    }

    @Test
    public void testCloseShopSuccess(){
        testOpenShopSuccess(); // open supersal
        closeSupersal = false;
        ShopFilter shopFilterName = (s) -> s.name.equals("supersal");

        boolean result = subscribedUserBridge.closeShop(supersal.ID,supersalFounder.name);
        assertTrue(result);

        List<Shop> searchResult = subscribedUserBridge.getShopsInfo(supersalFounder.name, shopFilterName);
        assertNull(searchResult);

        List<ProductInShop> productInShops = subscribedUserBridge.searchShopProducts(supersalFounder.name, supersal.ID);
        assertNull(productInShops);

        List<Notification> notifications = subscribedUserBridge.getNotifications(supersalFounder.name);
        assertNotNull(notifications);
        assertEquals(1,notifications.size());
        assertEquals("superfounder has close the shop "+supersal.ID,notifications.get(0).getContent());
    }

    @Test
    public void testAddManagerPermissionsSuccess(){
        testAppointShopManagerSuccess(); // u1 - manager at castro
        List<SubscribedUser.Permission> newPermissions = new LinkedList<>();
        newPermissions.add(SubscribedUser.Permission.ROLE_INFO);
        newPermissions.add(SubscribedUser.Permission.SET_PURCHASE_POLICY);
        newPermissions.add(SubscribedUser.Permission.HISTORY_INFO);
        boolean result = subscribedUserBridge.changeAdminPermission(shops[castro_ID].ID,castroFounder.name,u1.name, newPermissions);
        assertTrue(result);

        Map<String,List<SubscribedUser.Permission>> permissions =  subscribedUserBridge.getShopPermissions(u1.name,shops[castro_ID].ID);
        List<SubscribedUser.Permission> u1Permissions = permissions.getOrDefault(u1.name,null);

        assertNotNull(u1Permissions);
        assertEquals(3,u1Permissions.size());
        assertTrue(u1Permissions.contains(SubscribedUser.Permission.ROLE_INFO));
        assertTrue(u1Permissions.contains(SubscribedUser.Permission.HISTORY_INFO));
        assertTrue(u1Permissions.contains(SubscribedUser.Permission.SET_PURCHASE_POLICY));
    }

    @Test
    public void testAddManagerPermissionsFailureNotAppointer(){
        testAppointShopManagerSuccess(); // u1 - manager at castro
        List<SubscribedUser.Permission> newPermissions = new LinkedList<>();
        newPermissions.add(SubscribedUser.Permission.ROLE_INFO);
        newPermissions.add(SubscribedUser.Permission.SET_PURCHASE_POLICY);
        boolean result = subscribedUserBridge.changeAdminPermission(shops[castro_ID].ID,ACEFounder.name,u1.name,newPermissions);
        assertFalse(result);

        Map<String,List<SubscribedUser.Permission>> permissions = subscribedUserBridge.getShopPermissions(ACEFounder.name,shops[castro_ID].ID);
        assertNotNull(permissions);
        List<SubscribedUser.Permission> u1Permissions = permissions.getOrDefault(u1.name,null);
        assertNotNull(u1Permissions);
        assertEquals(1,u1Permissions.size());
        assertTrue(u1Permissions.contains(SubscribedUser.Permission.HISTORY_INFO));
    }

    @Test
    public void testAddOwnerPermissionsSuccess(){

        boolean result = subscribedUserBridge.changeAdminPermission(shops[ACE_ID].ID,ACEFounder.name,MegaSportFounder.name, Arrays.stream(defaultFounderPermissions).toList());
        assertTrue(result);

        List<Notification> notifications =  subscribedUserBridge.getNotifications(MegaSportFounder.name);
        assertNotNull(notifications);
        assertEquals(1,notifications.size());
        assertEquals("ACEFounder has change your permission as administrator of shop 0 to[1, 2, 4, 6, 7, 9, 10, 11, 13, 5, 12]",notifications.get(0).getContent());

        Map<String,List<SubscribedUser.Permission>> permissions = subscribedUserBridge.getShopPermissions(MegaSportFounder.name,shops[ACE_ID].ID);
        assertNotNull(permissions);
        List<SubscribedUser.Permission> ownerPermissions = permissions.getOrDefault(MegaSportFounder.name,null);
        assertNotNull(ownerPermissions);
        assertEquals(defaultFounderPermissions.length, ownerPermissions.size());
        assertTrue(ownerPermissions.containsAll(List.of(defaultFounderPermissions)));
        // cancel side-effects
        subscribedUserBridge.changeAdminPermission(shops[ACE_ID].ID,ACEFounder.name,MegaSportFounder.name, Arrays.stream(defaultOwnerPermissions).toList());
    }

    @Test
    public void testAddOwnerPermissionsFailureNotAppointer(){
        testAppointShopOwnerSuccess(); // u1 - owner at castro

        boolean result = subscribedUserBridge.changeAdminPermission(shops[castro_ID].ID,ACEFounder.name,u1.name, Arrays.stream(defaultFounderPermissions).toList());
        assertFalse(result);

        Map<String,List<SubscribedUser.Permission>> permissions = subscribedUserBridge.getShopPermissions(ACEFounder.name,shops[castro_ID].ID);

        assertNotNull(permissions);

        List<SubscribedUser.Permission> ownerPermissions = permissions.getOrDefault(u1.name,null);

        assertNotNull(ownerPermissions);
        assertEquals(defaultOwnerPermissions.length,ownerPermissions.size());
        assertTrue(ownerPermissions.containsAll(List.of(defaultOwnerPermissions)));
    }

    @Test
    public void testGetRoleInformationByFounderSuccessNoAppointmentsButSetUp(){
        Map<String,Appointment> appointmentMap = subscribedUserBridge.getShopAppointments(castroFounder.name,shops[castro_ID].ID);

        assertNotNull(appointmentMap);
        assertEquals(3,appointmentMap.size());

        Appointment appointment = appointmentMap.getOrDefault(castroFounder.name,null);

        assertNotNull(appointment);
        assertEquals(Appointment.Role.FOUNDER,appointment.role);
        assertEquals(castroFounder.name,appointment.appointer);
    }

    @Test
    public void testGetRoleInformationByFounderSuccessWithAppointments(){
        testAppointShopOwnerSuccess(); // u1 - owner at castro
        Map<String,Appointment> appointmentMap = subscribedUserBridge.getShopAppointments(castroFounder.name,shops[castro_ID].ID);

        assertNotNull(appointmentMap);
        assertEquals(4,appointmentMap.size());

        Appointment founder = appointmentMap.getOrDefault(castroFounder.name,null);
        Appointment owner = appointmentMap.getOrDefault(u1.name,null);
        assertNotNull(founder);
        assertNotNull(owner);
        assertEquals(Appointment.Role.FOUNDER,founder.role);
        assertEquals(Appointment.Role.OWNER,owner.role);
        assertEquals(castroFounder.name,founder.appointer);
        assertEquals(castroFounder.name,owner.appointer);;
    }

    @Test
    public void testGetRoleInformationByManagerSuccess(){
        testAddManagerPermissionsSuccess(); // u1 - manager at castro and can see role info

        Map<String,Appointment> appointmentMap = subscribedUserBridge.getShopAppointments(u1.name,shops[castro_ID].ID);
        assertNotNull(appointmentMap);
        assertEquals(4,appointmentMap.size());

        Appointment founder =  appointmentMap.getOrDefault(castroFounder.name,null);
        Appointment manager = appointmentMap.getOrDefault(u1.name,null);

        assertNotNull(founder);
        assertNotNull(manager);
        assertEquals(Appointment.Role.FOUNDER,founder.getRole());
        assertEquals(Appointment.Role.MANAGER,manager.getRole());
        assertEquals(castroFounder.name,founder.getAppointer());
        assertEquals(castroFounder.name,manager.getAppointer());
    }

    @Test
    public void testGetRoleInformationByOwnerSuccess(){
        testAppointShopOwnerSuccess();

        Map<String,Appointment>  appointmentMap = subscribedUserBridge.getShopAppointments(u1.name,shops[castro_ID].ID);

        assertNotNull(appointmentMap);
        assertEquals(4,appointmentMap.size());

        Appointment founder = appointmentMap.getOrDefault(castroFounder.name,null);
        Appointment owner = appointmentMap.getOrDefault(u1.name,null);

        assertNotNull(founder);
        assertNotNull(owner);
        assertEquals(Appointment.Role.FOUNDER,founder.role);
        assertEquals(Appointment.Role.OWNER,owner.role);
        assertEquals(castroFounder.name,founder.getAppointer());
        assertEquals(castroFounder.name,owner.getAppointer());
    }

    @Test
    public void testGetRoleInformationFailureUserNotShopOwnerOrManager(){

        Map<String,Appointment> appointments = subscribedUserBridge.getShopAppointments(u1.name,shops[castro_ID].ID);
        Map<String,List<SubscribedUser.Permission>> permissions = subscribedUserBridge.getShopPermissions(u1.name,shops[castro_ID].ID);

        assertNull(appointments);
        assertNull(permissions);
    }

    @Test
    public void testGetRoleInformationFailureNoSuchShop(){
        Map<String,Appointment> appointments = subscribedUserBridge.getShopAppointments(castroFounder.name,1111111);
        Map<String,List<SubscribedUser.Permission>> permissions = subscribedUserBridge.getShopPermissions(castroFounder.name,11);

        assertNull(appointments);
        assertNull(permissions);
    }

    @Test
    public void testAddProductToCartWhenOwnerDeletesIt() throws InterruptedException {
        FounderDeletesProduct founderDeletesProduct = new FounderDeletesProduct(shops[castro_ID].ID,45,castroFounder);
        UserBuysProduct userBuysProduct = new UserBuysProduct(shops[castro_ID].ID,45,u1);

        founderDeletesProduct.start();
        userBuysProduct.start();
        founderDeletesProduct.join();
        userBuysProduct.join();

        boolean purchaseStatus = userBuysProduct.getStatus();
        boolean productRemovalStatus = founderDeletesProduct.getStatus();

        assertTrue(productRemovalStatus);

        ProductInShop pis = subscribedUserBridge.searchProductInShop(u1.name,45,shops[castro_ID].ID);
        ShoppingCart cart = subscribedUserBridge.checkCart(u1.name);
        assertNotNull(cart);

        ShopBasket basket = cart.getShopBasket(shops[castro_ID].ID);

        assertNull(pis);
        if(!purchaseStatus){
            assertNull(basket);
            assertEquals(0,cart.numOfProductsInCart());
            assertEquals(0,cart.getNumberOfBaskets());
        }
        else{
            assertNotNull(basket);
            assertEquals(1,cart.getNumberOfBaskets());
            assertEquals(1,basket.numOfProducts());
            // CHECK FOR NOTIFICATIONS
        }

        // cancel side-effects
        Product p = new Product("shoes","recommended","china");
        subscribedUserBridge.addProductToShop(castroFounder.name, shops[castro_ID].ID, p, 45, 40, 50);
        subscribedUserBridge.updateCart(u1.name, 45,shops[castro_ID].ID,0);
    }

    @Test
    public void testConcurrentPurchase() throws InterruptedException {
        Thread MegaSportFounderPurchase = new MegaSportFounderPurchase();
        Thread ACEFounderPurchase = new ACEFounderPurchase();

        MegaSportFounderPurchase.start();
        ACEFounderPurchase.start();
        MegaSportFounderPurchase.join();
        ACEFounderPurchase.join();

        ProductInShop product = userBridge.searchProductInShop(MegaSportFounder.name, 2,shops[castro_ID].ID);
        assertNotNull(product);
        assertTrue(product.quantity == 30 || product.quantity == 9);

        if(product.quantity == 9){
            // check for correct notifications
        }
        else {
            // check for correct notifications
        }
        // cancel side-effects
        Product p1 = new Product("T-shirt","recommended","china");
        subscribedUserBridge.addProductToShop(castroFounder.name, shops[castro_ID].ID, p1, 2, 30, 30);
    }

    @Test
    public void testReopenShop(){
        testCloseShopSuccess();
        boolean reopened = subscribedUserBridge.reOpenShop(supersalFounder.name,supersal.ID);
        assertTrue(reopened);
        List<Shop> shops = subscribedUserBridge.getShopsInfo(supersalFounder.name, shop -> shop.name.equals("supersal"));
        assertEquals(1, shops.size());
        assertEquals("supersal", shops.get(0).name);
        assertEquals(supersal.ID,shops.get(0).ID);
        closeSupersal = true;
    }

    @Test
    public void testCreateProductByQuantityDiscountSuccess(){
        policyID = subscribedUserBridge.createProductByQuantityDiscount(ACEFounder.name, 0,5,0.25,1,shops[ACE_ID].ID);
        assertNotEquals(-1,policyID);
    }

    @Test
    public void testCreateProductByQuantityDiscountSuccessFailureNoSuchShop(){
        policyID = subscribedUserBridge.createProductByQuantityDiscount(ACEFounder.name, 0,5,0.25,1,-2);
        assertEquals(-1,policyID);
    }

    @Test
    public void testCreateProductByQuantityDiscountSuccessFailureNoPermission(){
        policyID = subscribedUserBridge.createProductByQuantityDiscount(MegaSportFounder.name, 0,5,0.25,1,shops[castro_ID].ID);
        assertEquals(-1,policyID);
    }

    @Test
    public void testPurchaseCartWithProductByQuantityDiscountSuccess(){
        testCreateProductByQuantityDiscountSuccess();
        boolean added = subscribedUserBridge.addProductToCart(u1.name,shops[ACE_ID].ID,0,10);
        assertTrue(added);

        double payed = subscribedUserBridge.purchaseCart(u1.name, "4800470023456848", 674, 7, 2025);
        assertEquals(0.75*10*20,payed,0.0);

        List<Notification> notifications = subscribedUserBridge.getNotifications(ACEFounder.name);
        assertNotNull(notifications);
        assertEquals(1,notifications.size());
        assertEquals("testUser3 has buy from your shop \"ACE\" 10 for product #0 lamp",notifications.get(0).getContent());

        subscribedUserBridge.updateProductQuantity(ACEFounder.name, shops[ACE_ID].ID,0,30);
    }

    @Test
    public void testPurchaseCartWithProductByQuantityDiscountSuccessNoDiscountBadQuantity(){
        testCreateProductByQuantityDiscountSuccess();
        boolean added = subscribedUserBridge.addProductToCart(u1.name,shops[ACE_ID].ID,0,5);
        assertTrue(added);

        double payed = subscribedUserBridge.purchaseCart(u1.name, "4800470023456848", 674, 7, 2025);
        assertEquals(5*20,payed,0.0);

        List<Notification> notifications = subscribedUserBridge.getNotifications(ACEFounder.name);
        assertNotNull(notifications);
        assertEquals(1,notifications.size());
        assertEquals("testUser3 has buy from your shop \"ACE\" 5 for product #0 lamp",notifications.get(0).getContent());

        subscribedUserBridge.updateProductQuantity(ACEFounder.name, shops[ACE_ID].ID,0,30);
    }

    @Test
    public void testPurchaseCartWithProductByQuantityDiscountOtherProduct(){
        testCreateProductByQuantityDiscountSuccess();
        boolean added = subscribedUserBridge.addProductToCart(u1.name,shops[ACE_ID].ID,1,5);
        assertTrue(added);

        double payed = subscribedUserBridge.purchaseCart(u1.name, "4800470023456848", 674, 7, 2025);
        assertEquals(5*25,payed,0.0);
        subscribedUserBridge.updateProductQuantity(ACEFounder.name, shops[ACE_ID].ID,1,100);
    }

    @Test
    public void testPurchaseCartWithProductByQuantityDiscountMixedProducts(){
        testCreateProductByQuantityDiscountSuccess();
        boolean added = subscribedUserBridge.addProductToCart(u1.name,shops[ACE_ID].ID,0,5);
        assertTrue(added);

        added = subscribedUserBridge.addProductToCart(u1.name,shops[ACE_ID].ID,1,5);
        assertTrue(added);

        double payed = subscribedUserBridge.purchaseCart(u1.name, "4800470023456848", 674, 7, 2025);
        assertEquals(5*20 + 5*25,payed,0.0);
        subscribedUserBridge.updateProductQuantity(ACEFounder.name, shops[ACE_ID].ID,0,30);
        subscribedUserBridge.updateProductQuantity(ACEFounder.name, shops[ACE_ID].ID,1,100);
    }

    @Test
    public void testCreateProductDiscountSuccess(){
        policyID = subscribedUserBridge.createProductDiscount(ACEFounder.name, 0,0.25,1, shops[ACE_ID].ID);
        assertNotEquals(-1, policyID);
    }

    @Test
    public void testCreateProductDiscountFailureNoSuchShop(){
        policyID = subscribedUserBridge.createProductDiscount(ACEFounder.name, 0,0.11,1,-2);
        assertEquals(-1, policyID);
    }

    @Test
    public void testCreateProductDiscountFailureNoPermission(){
        policyID = subscribedUserBridge.createProductDiscount(MegaSportFounder.name,0,0.12,1,shops[castro_ID].ID);
        assertEquals(-1,policyID);
    }

    @Test
    public void testPurchaseCartWithProductDiscountSuccess(){
        testCreateProductDiscountSuccess();
        boolean added = subscribedUserBridge.addProductToCart(u1.name,shops[ACE_ID].ID,0,4);
        assertTrue(added);

        double payed = subscribedUserBridge.purchaseCart(u1.name,"4800470023456848", 674, 7, 2025);
        assertEquals(0.75*20*4,payed,0);
        subscribedUserBridge.updateProductQuantity(ACEFounder.name, shops[ACE_ID].ID,0,30);
    }

    @Test
    public void testPurchaseCartWithProductDiscountOtherProduct(){
        testCreateProductDiscountSuccess();
        boolean added = subscribedUserBridge.addProductToCart(u1.name,shops[ACE_ID].ID,1,4);
        assertTrue(added);

        double payed = subscribedUserBridge.purchaseCart(u1.name,"4800470023456848", 674, 7, 2025);
        assertEquals(25*4,payed,0);
        subscribedUserBridge.updateProductQuantity(ACEFounder.name, shops[ACE_ID].ID,1,100);
    }

    @Test
    public void testPurchaseCartWithProductDiscountMixedProducts(){
        testCreateProductDiscountSuccess();
        boolean added = subscribedUserBridge.addProductToCart(u1.name,shops[ACE_ID].ID,0,4);
        assertTrue(added);

        added = subscribedUserBridge.addProductToCart(u1.name,shops[ACE_ID].ID,1,4);
        assertTrue(added);

        double payed = subscribedUserBridge.purchaseCart(u1.name,"4800470023456848", 674, 7, 2025);
        assertEquals(0.75*20*4 + 25*4,payed,0);
        subscribedUserBridge.updateProductQuantity(ACEFounder.name, shops[ACE_ID].ID,0,30);
        subscribedUserBridge.updateProductQuantity(ACEFounder.name, shops[ACE_ID].ID,1,100);
    }

    @Test
    public void testCreateProductQuantityInPriceDiscount(){
        policyID = subscribedUserBridge.createProductQuantityInPriceDiscount(ACEFounder.name, 0,5,10,1,shops[ACE_ID].ID);
        assertNotEquals(-1,policyID);
    }

    @Test
    public void testCreateProductQuantityInPriceDiscountFailureNoSuchShop(){
        policyID = subscribedUserBridge.createProductQuantityInPriceDiscount(ACEFounder.name, 0,5,10,1,-2);
        assertEquals(-1,policyID);
    }

    @Test
    public void testCreateProductQuantityInPriceDiscountFailureNoPermission(){
        policyID = subscribedUserBridge.createProductQuantityInPriceDiscount(MegaSportFounder.name, 0,5,10,1,shops[castro_ID].ID);
        assertEquals(-1,policyID);
    }

    @Test
    public void testPurchaseCartWithProductQuantityInPriceDiscount(){
        testCreateProductQuantityInPriceDiscount();
        boolean added = subscribedUserBridge.addProductToCart(u1.name, shops[ACE_ID].ID,0,7);
        assertTrue(added);

        double payed = subscribedUserBridge.purchaseCart(u1.name, "4800470023456848", 674, 7, 2025);
        assertEquals(140 - (7*20 - (7/5)*10 - 2*20),payed,0);
        subscribedUserBridge.updateProductQuantity(ACEFounder.name, shops[ACE_ID].ID,0,30);
    }

    @Test
    public void testPurchaseCartWithProductQuantityInPriceDiscountOtherProduct(){
        testCreateProductQuantityInPriceDiscount();
        boolean added = subscribedUserBridge.addProductToCart(u1.name, shops[ACE_ID].ID,1,7);
        assertTrue(added);

        double payed = subscribedUserBridge.purchaseCart(u1.name,"4800470023456848", 674, 7, 2025);
        assertEquals(7*25,payed,0);
        subscribedUserBridge.updateProductQuantity(ACEFounder.name, shops[ACE_ID].ID,1,100);
    }

    @Test
    public void testPurchaseCartWithProductQuantityInPriceDiscountMixed(){
        testCreateProductQuantityInPriceDiscount();
        boolean added = subscribedUserBridge.addProductToCart(u1.name, shops[ACE_ID].ID,0,7);
        assertTrue(added);

        added = subscribedUserBridge.addProductToCart(u1.name, shops[ACE_ID].ID,1,7);
        assertTrue(added);

        double payed = subscribedUserBridge.purchaseCart(u1.name,"4800470023456848", 674, 7, 2025);
        assertEquals(140 - (7*20 - (7/5)*10 - 2*20) + 7*25,payed,0);
        subscribedUserBridge.updateProductQuantity(ACEFounder.name, shops[ACE_ID].ID,0,30);
        subscribedUserBridge.updateProductQuantity(ACEFounder.name, shops[ACE_ID].ID,1,100);
    }

    @Test
    public void testCreateRelatedGroupDiscountSuccess(){
        policyID = subscribedUserBridge.createRelatedGroupDiscount(ACEFounder.name, "not expensive",0.6,1,shops[ACE_ID].ID);
        assertNotEquals(-1,policyID);
    }

    @Test
    public void testCreateRelatedGroupDiscountFailureNoSuchShop(){
        policyID = subscribedUserBridge.createRelatedGroupDiscount(ACEFounder.name, "not expensive",0.6,1,-2);
        assertEquals(-1,policyID);
    }

    @Test
    public void testCreateRelatedGroupDiscountFailureNoPermission(){
        policyID = subscribedUserBridge.createRelatedGroupDiscount(MegaSportFounder.name, "relatedProducts",0.6,1,shops[castro_ID].ID);
        assertEquals(-1,policyID);
    }

    @Test
    public void testPurchaseCartWithRelatedGroupDiscountAllRelated(){
        testCreateRelatedGroupDiscountSuccess();
        boolean added = subscribedUserBridge.addProductToCart(u1.name,shops[ACE_ID].ID,0,10);
        assertTrue(added);

        added = subscribedUserBridge.addProductToCart(u1.name,shops[ACE_ID].ID,1,8);
        assertTrue(added);

        double payed = subscribedUserBridge.purchaseCart(u1.name,"4800470023456848", 674, 7, 2025);
        assertEquals(0.4*(10*20 + 8*25),payed,0);
        subscribedUserBridge.updateProductQuantity(ACEFounder.name, shops[ACE_ID].ID,0,30);
        subscribedUserBridge.updateProductQuantity(ACEFounder.name, shops[ACE_ID].ID,1,100);
    }

    @Test
    public void testPurchaseCartWithRelatedGroupDiscountNoneRelated(){
        testCreateRelatedGroupDiscountSuccess();
        boolean added = subscribedUserBridge.addProductToCart(u1.name, shops[ACE_ID].ID,2,15);
        assertTrue(added);

        double payed = subscribedUserBridge.purchaseCart(u1.name,"4800470023456848", 674, 7, 2025);
        assertEquals(15*40, payed,0);
        subscribedUserBridge.updateProductQuantity(ACEFounder.name, shops[ACE_ID].ID,2,40);
    }

    @Test
    public void testPurchaseCartWithRelatedGroupDiscountSomeRelatedSomeNot(){
        testCreateRelatedGroupDiscountSuccess();
        boolean added = subscribedUserBridge.addProductToCart(u1.name,shops[ACE_ID].ID,0,10);
        assertTrue(added);

        added = subscribedUserBridge.addProductToCart(u1.name,shops[ACE_ID].ID,1,8);
        assertTrue(added);

        added = subscribedUserBridge.addProductToCart(u1.name, shops[ACE_ID].ID,2,15);
        assertTrue(added);

        double payed = subscribedUserBridge.purchaseCart(u1.name,"4800470023456848", 674, 7, 2025);
        assertEquals(15*40 + 0.4*(10*20 + 8*25),payed,0);
        subscribedUserBridge.updateProductQuantity(ACEFounder.name, shops[ACE_ID].ID,0,30);
        subscribedUserBridge.updateProductQuantity(ACEFounder.name, shops[ACE_ID].ID,1,100);
        subscribedUserBridge.updateProductQuantity(ACEFounder.name, shops[ACE_ID].ID,2,40);
    }

    @Test
    public void testCreateShopDiscountSuccess(){
        policyID = subscribedUserBridge.createShopDiscount(ACEFounder.name, 5,0.5,1,shops[ACE_ID].ID);
        assertNotEquals(-1,policyID);
    }

    @Test
    public void testCreateShopDiscountFailureNoSuchShop(){
        policyID = subscribedUserBridge.createShopDiscount(ACEFounder.name, 5,0.5,1,-2);
        assertEquals(-1,policyID);
    }

    @Test
    public void testCreateShopDiscountFailureNoPermission(){
        policyID = subscribedUserBridge.createShopDiscount(MegaSportFounder.name, 5,0.5,1,shops[castro_ID].ID);
        assertEquals(-1,policyID);
    }

    @Test
    public void testPurchaseCartWithShopDiscount(){
        testCreateShopDiscountSuccess();
        boolean added = subscribedUserBridge.addProductToCart(u1.name, shops[ACE_ID].ID,0,5);
        assertTrue(added);

        double payed = subscribedUserBridge.purchaseCart(u1.name,"4800470023456848", 674, 7, 2025);
        assertEquals(0.5*5*20,payed,0);
        subscribedUserBridge.updateProductQuantity(ACEFounder.name, shops[ACE_ID].ID,0,30);
    }

    @Test
    public void testPurchaseCartWithShopDiscountOtherShop(){
        testCreateShopDiscountSuccess();
        boolean added = subscribedUserBridge.addProductToCart(u1.name, shops[castro_ID].ID,2,5);
        assertTrue(added);

        double payed = subscribedUserBridge.purchaseCart(u1.name,"4800470023456848", 674, 7, 2025);
        assertEquals(5*30,payed,0);
        subscribedUserBridge.updateProductQuantity(castroFounder.name, shops[castro_ID].ID,2,30);
    }

    @Test
    public void testPurchaseCartWithShopDiscountMixedShops(){
        testCreateShopDiscountSuccess();
        boolean added = subscribedUserBridge.addProductToCart(u1.name, shops[ACE_ID].ID,0,5);
        assertTrue(added);

        added = subscribedUserBridge.addProductToCart(u1.name, shops[castro_ID].ID,2,5);
        assertTrue(added);

        double payed = subscribedUserBridge.purchaseCart(u1.name,"4800470023456848", 674, 7, 2025);
        assertEquals( 0.5*5*20+ 5*30,payed,0);
        subscribedUserBridge.updateProductQuantity(ACEFounder.name, shops[ACE_ID].ID,0,30);
        subscribedUserBridge.updateProductQuantity(castroFounder.name, shops[castro_ID].ID,2,30);
    }

    @Test
    public void testCreateDiscountAndPolicySuccess(){
        DiscountRules rule = new ProductDiscount(1,0, 0.25);
        DiscountPred pred = new ValidateBasketQuantityDiscount(1, 5, true);

        policyID = subscribedUserBridge.createDiscountAndPolicy(ACEFounder.name, pred,rule,1,shops[ACE_ID].ID);
        assertNotEquals(-1,policyID);

        int ID = subscribedUserBridge.createValidateBasketValueDiscount(ACEFounder.name, 40,false,policyID,shops[ACE_ID].ID);
        assertNotEquals(-1,ID);
    }

    @Test
    public void testCreateDiscountAndPolicyFailureNoPermission(){
        DiscountRules rule = new ProductDiscount(policyID,0, 0.25);
        DiscountPred pred = new ValidateBasketQuantityDiscount(1, 5, true);

        policyID = subscribedUserBridge.createDiscountAndPolicy(castroFounder.name, pred,rule,1,shops[ACE_ID].ID);
        assertEquals(-1,policyID);
    }

    @Test
    public void testPurchaseBasketWithAndDiscountSuccess(){
        testCreateDiscountAndPolicySuccess();
        boolean added = subscribedUserBridge.addProductToCart(u1.name, shops[ACE_ID].ID, 0,4);
        assertTrue(added);

        double payed = subscribedUserBridge.purchaseCart(u1.name,"4800470023456848", 674, 7, 2025);
        assertEquals(0.75*4*20,payed,0);
        subscribedUserBridge.updateProductQuantity(ACEFounder.name, shops[ACE_ID].ID,0,30);
    }

    @Test
    public void testPurchaseBasketWithAndDiscountFailureBadQuantity(){
        testCreateDiscountAndPolicySuccess();
        boolean added = subscribedUserBridge.addProductToCart(u1.name, shops[ACE_ID].ID, 0,6);
        assertTrue(added);

        double payed = subscribedUserBridge.purchaseCart(u1.name,"4800470023456848", 674, 7, 2025);
        assertEquals(6*20,payed,0);
        subscribedUserBridge.updateProductQuantity(ACEFounder.name, shops[ACE_ID].ID,0,30);
    }

        @Test
    public void testPurchaseBasketWithAndDiscountFailureBadPrice(){
        testCreateDiscountAndPolicySuccess();
        boolean added = subscribedUserBridge.addProductToCart(u1.name, shops[ACE_ID].ID, 0,1);
        assertTrue(added);

        double payed = subscribedUserBridge.purchaseCart(u1.name,"4800470023456848", 674, 7, 2025);
        assertEquals(20,payed,0);
        subscribedUserBridge.updateProductQuantity(ACEFounder.name, shops[ACE_ID].ID,0,30);
    }

    @Test
    public void testCreateDiscountMaxPolicySuccess(){
        DiscountRules rule = new ProductDiscount(1,0, 0.25);
        DiscountRules rule1 = new ProductDiscount(2,0, 0.5);
        Collection<DiscountRules> rules = new ArrayList<>();
        rules.add(rule);
        rules.add(rule1);
        DiscountMaxPolicy maxPolicy = new DiscountMaxPolicy(1,rules);

        policyID = subscribedUserBridge.createDiscountMaxPolicy(ACEFounder.name, maxPolicy,1,shops[ACE_ID].ID);
        assertNotEquals(-1,policyID);
    }

    @Test
    public void testCreateDiscountMaxPolicyFailureNoPermission(){
        DiscountRules rule = new ProductDiscount(1,0, 0.25);
        DiscountRules rule1 = new ProductDiscount(2,0, 0.5);
        Collection<DiscountRules> rules = new ArrayList<>();
        rules.add(rule);
        rules.add(rule1);
        DiscountMaxPolicy maxPolicy = new DiscountMaxPolicy(1,rules);

        policyID = subscribedUserBridge.createDiscountMaxPolicy(castroFounder.name, maxPolicy,1,shops[ACE_ID].ID);
        assertEquals(-1,policyID);
    }

    @Test
    public void testPurchaseBasketWithMaxDiscount(){
        testCreateDiscountMaxPolicySuccess();
        boolean added = subscribedUserBridge.addProductToCart(u1.name,shops[ACE_ID].ID,0,5);
        assertTrue(added);

        double payed = subscribedUserBridge.purchaseCart(u1.name,"4800470023456848", 674, 7, 2025);
        assertEquals(0.5*5*20,payed,0);
        subscribedUserBridge.updateProductQuantity(ACEFounder.name, shops[ACE_ID].ID,0,30);
    }

    @Test
    public void testPurchaseBasketWithMaxDiscountNoDiscount(){
        testCreateDiscountMaxPolicySuccess();
        boolean added = subscribedUserBridge.addProductToCart(u1.name,shops[ACE_ID].ID,1,5);
        assertTrue(added);

        double payed = subscribedUserBridge.purchaseCart(u1.name,"4800470023456848", 674, 7, 2025);
        assertEquals(5*25,payed,0);
        subscribedUserBridge.updateProductQuantity(ACEFounder.name, shops[ACE_ID].ID,1,100);
    }

    @Test
    public void testAddUserAgeValidation(){
        purchasePolicyID = subscribedUserBridge.createValidateUserPurchase(ACEFounder.name, 40,1,shops[ACE_ID].ID);
        assertNotEquals(-1,purchasePolicyID);
    }

    @Test
    public void testAddUserAgeValidationNoSuchShop(){
        purchasePolicyID = subscribedUserBridge.createValidateUserPurchase(ACEFounder.name, 40,1,-2);
        assertEquals(-1,purchasePolicyID);
    }

    @Test
    public void testAddUserAgeValidationNoPermission(){
        purchasePolicyID = subscribedUserBridge.createValidateUserPurchase(castroFounder.name, 40,1,shops[ACE_ID].ID);
        assertEquals(-1,purchasePolicyID);
    }

    @Test
    public void testPurchaseWithAgeValidation(){
        testAddUserAgeValidation();
        boolean added = subscribedUserBridge.addProductToCart(u1.name,shops[ACE_ID].ID,0,5);
        assertTrue(added);

        double payed = subscribedUserBridge.purchaseCart(u1.name,"4800470023456848", 674, 7, 2025);
        assertEquals(0,payed,0);

        subscribedUserBridge.updateCart(u1.name, 0,shops[ACE_ID].ID,0);
    }

    @Test
    public void testCreateCategoryPurchase(){
        purchasePolicyID = subscribedUserBridge.createValidateCategoryPurchase(ACEFounder.name, "not expensive",5,true,1,shops[ACE_ID].ID);
        assertNotEquals(-1,purchasePolicyID);
    }

    @Test
    public void testCreateCategoryPurchaseNoSuchShop(){
        purchasePolicyID = subscribedUserBridge.createValidateCategoryPurchase(ACEFounder.name, "not expensive",5,true,1,-2);
        assertEquals(-1,purchasePolicyID);
    }

    @Test
    public void testCreateCategoryPurchaseNoPermission(){
        purchasePolicyID = subscribedUserBridge.createValidateCategoryPurchase(castroFounder.name, "not expensive",5,true,1,shops[ACE_ID].ID);
        assertEquals(-1,purchasePolicyID);
    }

    @Test
    public void testPurchaseWithCategoryPolicySuccess(){
        testCreateCategoryPurchase();
        boolean added = subscribedUserBridge.addProductToCart(u1.name, shops[ACE_ID].ID,0,4);
        assertTrue(added);

        subscribedUserBridge.addProductToCart(u1.name, shops[ACE_ID].ID,1,4);
        assertTrue(added);

        double payed = subscribedUserBridge.purchaseCart(u1.name, "4800470023456848", 674, 7, 2025);
        assertEquals(4*45,payed,0);
        subscribedUserBridge.updateCart(u1.name, 0,shops[ACE_ID].ID,0);
        subscribedUserBridge.updateCart(u1.name, 1,shops[ACE_ID].ID,0);
    }

    @Test
    public void testPurchaseWithCategoryPolicyFail(){
        testCreateCategoryPurchase();
        boolean added = subscribedUserBridge.addProductToCart(u1.name, shops[ACE_ID].ID,0,6);
        assertTrue(added);

        subscribedUserBridge.addProductToCart(u1.name, shops[ACE_ID].ID,1,4);
        assertTrue(added);

        double payed = subscribedUserBridge.purchaseCart(u1.name, "4800470023456848", 674, 7, 2025);
        assertEquals(0,payed,0);
        subscribedUserBridge.updateCart(u1.name, 0,shops[ACE_ID].ID,0);
        subscribedUserBridge.updateCart(u1.name, 1,shops[ACE_ID].ID,0);
    }

    @Test
    public void testCreateDiscountOrPolicySuccess(){
        DiscountRules rule = new ProductDiscount(1,0, 0.25);
        DiscountPred pred = new ValidateBasketQuantityDiscount(1, 5, false);

        policyID = subscribedUserBridge.createDiscountOrPolicy(ACEFounder.name, pred,rule,1,shops[ACE_ID].ID);
        assertNotEquals(-1,policyID);

        int ID = subscribedUserBridge.createValidateBasketValueDiscount(ACEFounder.name, 40,false,policyID,shops[ACE_ID].ID);
        assertNotEquals(-1,ID);
    }

    @Test
    public void testCreateDiscountOrPolicyFailureNoPermission(){
        DiscountRules rule = new ProductDiscount(1,0, 0.25);
        DiscountPred pred = new ValidateBasketQuantityDiscount(1, 5, true);

        policyID = subscribedUserBridge.createDiscountOrPolicy(castroFounder.name, pred,rule,1,shops[ACE_ID].ID);
        assertEquals(-1,policyID);
    }

    @Test
    public void testPurchaseBasketWithOrDiscountBothConditions(){
        testCreateDiscountOrPolicySuccess();
        boolean added = subscribedUserBridge.addProductToCart(u1.name, shops[ACE_ID].ID,0,4);
        assertTrue(added);

        double payed = subscribedUserBridge.purchaseCart(u1.name,"4800470023456848", 674, 7, 2025);
        assertEquals(0.75*4*20,payed,0);
        subscribedUserBridge.updateProductQuantity(ACEFounder.name, shops[ACE_ID].ID,0,30);
    }

    @Test
    public void testPurchaseBasketWithOrDiscountFirstCondition(){
        testCreateDiscountOrPolicySuccess();
        boolean added = subscribedUserBridge.addProductToCart(u1.name, shops[ACE_ID].ID,0,5);
        assertTrue(added);

        double payed = subscribedUserBridge.purchaseCart(u1.name,"4800470023456848", 674, 7, 2025);
        assertEquals(0.75*5*20,payed,0);
        subscribedUserBridge.updateProductQuantity(ACEFounder.name, shops[ACE_ID].ID,0,30);
    }

    @Test
    public void testPurchaseBasketOrDiscountNoDiscount(){
        testCreateDiscountOrPolicySuccess();
        boolean added = subscribedUserBridge.addProductToCart(u1.name, shops[ACE_ID].ID,1,1);
        assertTrue(added);

        double payed = subscribedUserBridge.purchaseCart(u1.name,"4800470023456848", 674, 7, 2025 );
        assertEquals(25,payed,0);
        subscribedUserBridge.updateProductQuantity(ACEFounder.name, shops[ACE_ID].ID,1,100);
    }

    @Test
    public void testPurchaseBasketWithOrDiscountNoDiscount(){
        testCreateDiscountOrPolicySuccess();
        boolean added = subscribedUserBridge.addProductToCart(u1.name, shops[ACE_ID].ID,0,1);
        assertTrue(added);

        double payed = subscribedUserBridge.purchaseCart(u1.name,"4800470023456848", 674, 7, 2025);
        assertEquals(20,payed,0);
        subscribedUserBridge.updateProductQuantity(ACEFounder.name, shops[ACE_ID].ID,0,30);
    }

    @Test
    public void testCreateXorDiscountSuccess(){
        DiscountRules rule1 = new ProductDiscount(1,0, 0.25);
        DiscountRules rule2 = new ShopDiscount(2, 3, 0.2);
        DiscountPred tieBreaker = new ValidateProductQuantityDiscount(3,0,4,true);

        policyID = subscribedUserBridge.createDiscountXorPolicy(ACEFounder.name, rule1,rule2,tieBreaker,1,shops[ACE_ID].ID);
        assertNotEquals(-1,policyID);
    }

    @Test
    public void testCreateXorFailureNoPermission(){
        DiscountRules rule1 = new ProductDiscount(1,0, 0.25);
        DiscountRules rule2 = new ShopDiscount(2, 3, 0.2);
        DiscountPred tieBreaker = new ValidateProductQuantityDiscount(3,0,4,true);

        policyID = subscribedUserBridge.createDiscountXorPolicy(castroFounder.name, rule1,rule2,tieBreaker,1,shops[ACE_ID].ID);
        assertEquals(-1,policyID);
    }

    @Test
    public void testPurchaseWithXorDiscountNoConditionsHold(){
        testCreateXorDiscountSuccess();
        boolean added = subscribedUserBridge.addProductToCart(u1.name, shops[ACE_ID].ID,1,2);
        assertTrue(added);

        double payed = subscribedUserBridge.purchaseCart(u1.name, "4800470023456848", 674, 7, 2025);
        assertEquals(2*25,payed,0);
        subscribedUserBridge.updateProductQuantity(ACEFounder.name, shops[ACE_ID].ID,1,100);
    }

    @Test
    public void testPurchaseWithXorDiscountFirstConditionHolds(){
        testCreateXorDiscountSuccess();
        boolean added = subscribedUserBridge.addProductToCart(u1.name, shops[ACE_ID].ID,0,2);
        assertTrue(added);

        double payed = subscribedUserBridge.purchaseCart(u1.name, "4800470023456848", 674, 7, 2025);
        assertEquals(0.75*2*20,payed,0);
        subscribedUserBridge.updateProductQuantity(ACEFounder.name, shops[ACE_ID].ID,0,30);
    }

    @Test
    public void testPurchaseWithXorSecondConditionHolds(){
        testCreateXorDiscountSuccess();
        boolean added = subscribedUserBridge.addProductToCart(u1.name, shops[ACE_ID].ID,1,4);
        assertTrue(added);

        double payed = subscribedUserBridge.purchaseCart(u1.name, "4800470023456848", 674, 7, 2025);
        assertEquals(0.8*4*25,payed,0);
        subscribedUserBridge.updateProductQuantity(ACEFounder.name, shops[ACE_ID].ID,1,100);
    }

    @Test
    public void testScenario1(){
        boolean updated =subscribedUserBridge.updateProductPrice(castroFounder.name,shops[castro_ID].ID,45,12.5);
        assertTrue(updated);

        boolean added = subscribedUserBridge.addProductToCart(u1.name, shops[castro_ID].ID,45,3);
        assertTrue(added);

        double payed = subscribedUserBridge.purchaseCart(u1.name, "4800470023456848", 674, 7, 2025);
        assertEquals(3*12.5,payed,0);

        testCreateProductByQuantityDiscountSuccess();

        added = subscribedUserBridge.addProductToCart(u1.name, shops[ACE_ID].ID,0,6);
        assertTrue(added);

        payed = subscribedUserBridge.purchaseCart(u1.name, "4800470023456848", 674, 7, 2025);
        assertEquals(6*20*0.75,payed,0);

        subscribedUserBridge.updateProductPrice(castroFounder.name,shops[castro_ID].ID,45,50);
        subscribedUserBridge.updateProductQuantity(castroFounder.name, shops[castro_ID].ID,45,40);
        subscribedUserBridge.updateProductQuantity(ACEFounder.name, shops[ACE_ID].ID,0,30);
    }

    @Test
    public void testScenario2(){
        testAppointShopOwnerSuccess(); // u1 - owner at castro
        policyID = subscribedUserBridge.createProductByQuantityDiscount(u1.name, 2,5,0.5,2,shops[castro_ID].ID);
        assertNotEquals(-1,policyID);
        removeCastro = true;

        boolean added = subscribedUserBridge.addProductToCart(u1.name, shops[castro_ID].ID,2,10);
        assertTrue(added);

        added = subscribedUserBridge.addProductToCart(u1.name,shops[castro_ID].ID,45,15);
        assertTrue(added);

        added = subscribedUserBridge.addProductToCart(u2.name, shops[castro_ID].ID,2,30);
        assertTrue(added);

        double payed = subscribedUserBridge.purchaseCart(u1.name,"4800470023456848", 674, 7, 2025);
        assertEquals(0.5*10*30+15*50,payed,0);

        payed = subscribedUserBridge.purchaseCart(u2.name,"4800470023456848", 674, 7, 2025);
        assertEquals(0,payed,0);

        subscribedUserBridge.updateProductQuantity(u1.name, shops[castro_ID].ID,2,30);
        subscribedUserBridge.updateProductQuantity(u1.name,shops[castro_ID].ID,45,40);
    }

    @Test
    public void testScenario3(){
        boolean added = subscribedUserBridge.addProductToCart(u1.name, shops[castro_ID].ID,2,10);
        assertTrue(added);

        added = subscribedUserBridge.addProductToCart(u1.name,shops[ACE_ID].ID,0,1);
        assertTrue(added);

        boolean closed = subscribedUserBridge.closeShop(shops[castro_ID].ID, castroFounder.name);
        assertTrue(closed);

        List<Notification> notifications = subscribedUserBridge.getNotifications(castroFounder.name);
        assertNotNull(notifications);
        assertEquals(1,notifications.size());
        assertEquals("castroFounder has close the shop 1",notifications.get(0).getContent());

        notifications = subscribedUserBridge.getNotifications(ACEFounder.name);
        assertNotNull(notifications);
        assertEquals(1,notifications.size());
        assertEquals("castroFounder has close the shop 1",notifications.get(0).getContent());

        notifications = subscribedUserBridge.getNotifications(MegaSportFounder.name);
        assertNotNull(notifications);
        assertEquals(1,notifications.size());
        assertEquals("castroFounder has close the shop 1",notifications.get(0).getContent());

        added = subscribedUserBridge.addProductToCart(u1.name,shops[castro_ID].ID,45,15);
        assertFalse(added);

        testReopenShop();

        double payed = subscribedUserBridge.purchaseCart(u1.name, "4800470023456848", 674, 7, 2025);
        assertEquals(20,payed,0);

        boolean opened = subscribedUserBridge.reOpenShop(castroFounder.name, shops[castro_ID].ID);
        assertTrue(opened);

        payed = subscribedUserBridge.purchaseCart(u1.name, "4800470023456848", 674, 7, 2025);
        assertEquals(10*30+15*50,payed,0);

        subscribedUserBridge.updateProductQuantity(castroFounder.name, shops[castro_ID].ID,2,30);
        subscribedUserBridge.updateProductQuantity(castroFounder.name, shops[castro_ID].ID,45,40);
        subscribedUserBridge.updateProductQuantity(ACEFounder.name,shops[ACE_ID].ID,0,30);
    }

    @Test
    public void testPurchaseWithXorDiscountBothConditionsHold(){
        testCreateXorDiscountSuccess();
        boolean added = subscribedUserBridge.addProductToCart(u1.name, shops[ACE_ID].ID,0,4);
        assertTrue(added);

        double payed = subscribedUserBridge.purchaseCart(u1.name, "4800470023456848", 674, 7, 2025);
        assertEquals(0.75*4*20,payed,0);
        List<Notification> notifications = subscribedUserBridge.getNotifications(ACEFounder.name);
        assertNotNull(notifications);
        assertEquals(1,notifications.size());
        assertEquals("testUser3 has buy from your shop \"ACE\" 4 for product #0 lamp",notifications.get(0).getContent());
        subscribedUserBridge.updateProductQuantity(ACEFounder.name,shops[ACE_ID].ID,0,30);

        notifications = subscribedUserBridge.getNotifications(ACEFounder.name);
        assertNotNull(notifications);
        assertEquals(1,notifications.size());
        assertEquals("product 0 from shop 0 quantity has change to: 30",notifications.get(0).getContent());
    }

    @Test
    public void testPurchaseWithDiscountAndPurchasePolicyFailPolicy(){
        testCreateShopDiscountSuccess();
        testAddUserAgeValidation();
        boolean added = subscribedUserBridge.addProductToCart(u1.name, shops[ACE_ID].ID,0,6);
        assertTrue(added);

        double payed = subscribedUserBridge.purchaseCart(u1.name, "4800470023456848", 674, 7, 2025);
        assertEquals(0,payed,0);
        subscribedUserBridge.updateCart(u1.name, 0,shops[ACE_ID].ID,0);
    }

    @Test
    public void testPurchaseWithDiscountAndPurchasePolicySuccess(){
        testCreateShopDiscountSuccess();
        testCreateCategoryPurchase();
        boolean added = subscribedUserBridge.addProductToCart(u1.name, shops[ACE_ID].ID,0,4);
        assertTrue(added);

        subscribedUserBridge.addProductToCart(u1.name, shops[ACE_ID].ID,1,4);
        assertTrue(added);

        double payed = subscribedUserBridge.purchaseCart(u1.name, "4800470023456848", 674, 7, 2025);
        assertEquals(0.5*4*45,payed,0);
        subscribedUserBridge.updateProductQuantity(ACEFounder.name,shops[ACE_ID].ID,0,30);
        subscribedUserBridge.updateProductQuantity(ACEFounder.name,shops[ACE_ID].ID,1,100);
    }

    @Test
    public void testDelayedNotification(){
        testAppointShopOwnerSuccess(); // u1 - owner at castro

        boolean exited = subscribedUserBridge.exit(u1.name);
        assertTrue(exited);

        boolean closed = subscribedUserBridge.closeShop(shops[castro_ID].ID, castroFounder.name);
        assertTrue(closed);

        Guest guest = subscribedUserBridge.visit();
        u1 = subscribedUserBridge.login(guest.name,new RegistrationInfo(userNames[0], passwords[0]));

        List<Notification> notifications = subscribedUserBridge.getDelayNotification(u1.name);
        assertNotNull(notifications);
        assertEquals(1,notifications.size());
        assertEquals("castroFounder has close the shop 1",notifications.get(0).getContent());
        subscribedUserBridge.reOpenShop(castroFounder.name, shops[castro_ID].ID);
    }

    public User enter() {
        Guest g = subscribedUserBridge.visit();
        return subscribedUserBridge.login(g.name,new RegistrationInfo( "enterUser","enterPass"));
    }
}