package AcceptanceTests.Tests;

import AcceptanceTests.Bridge.SubscribedUserBridge;
import AcceptanceTests.Bridge.SubscribedUserProxy;
import AcceptanceTests.Bridge.UserProxy;
import AcceptanceTests.DataObjects.*;
import AcceptanceTests.Threads.*;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.Assert.*;

public class SubscribedUserTests extends UserTests {
    private static SubscribedUserBridge subscribedUserBridge = null;

    private static Shop supersal;
    private boolean closeSupersal = false;
    private boolean deleteCastro222 = false;
    private boolean removeU1OwnerCastro = false;
    private boolean removeU1ManagerCastro = false;
    private int removeU3ManagerCastro = -1;

    private final static String[] userNames = new String[]{"testUser3","buyer100","michael","superfounder"};
    private final static String[] passwords = new String[]{"42","secret","leahcim","superpassword"};

    private final static SubscribedUser.Permission[] defaultFounderPermissions = new SubscribedUser.Permission[]{
            SubscribedUser.Permission.STOCK_MANAGEMENT, SubscribedUser.Permission.SET_PURCHASE_POLICY, SubscribedUser.Permission.ASSIGN_SHOP_OWNER,
            SubscribedUser.Permission.ASSIGN_SHOP_MANAGER, SubscribedUser.Permission.CHANGE_MANAGER_PERMISSION,
            SubscribedUser.Permission.CLOSE_SHOP, SubscribedUser.Permission.REOPEN_SHOP, SubscribedUser.Permission.ROLE_INFO,
            SubscribedUser.Permission.HISTORY_INFO
    };

    private final static SubscribedUser.Permission[] defaultOwnerPermissions = new SubscribedUser.Permission[]{
            SubscribedUser.Permission.STOCK_MANAGEMENT, SubscribedUser.Permission.SET_PURCHASE_POLICY, SubscribedUser.Permission.ASSIGN_SHOP_OWNER,
            SubscribedUser.Permission.ASSIGN_SHOP_MANAGER, SubscribedUser.Permission.CHANGE_MANAGER_PERMISSION,
            SubscribedUser.Permission.REOPEN_SHOP, SubscribedUser.Permission.ROLE_INFO,
            SubscribedUser.Permission.HISTORY_INFO
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
    public void testOpenShopFailure(){
        Shop s = subscribedUserBridge.openShop(u1.name,"castro","fashion");

        assertNull(s);

        Map<String,Appointment> appointment = subscribedUserBridge.getShopAppointments(u1.name,shops[castro_ID].ID);
        assertNull(appointment.getOrDefault(u1.name,null));
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

        ProductInShop pis  = subscribedUserBridge.searchProductInShop(castroFounder.name, 222,shops[castro_ID].ID);
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
    }

    @Test
    public void testDeleteProductFromShopByOwnerFailureBadID(){
        boolean deleted = subscribedUserBridge.deleteProductFromShop(ACEFounder.name,shops[castro_ID].ID,5);
        assertFalse(deleted);

        ProductInShop pis  = subscribedUserBridge.searchProductInShop(ACEFounder.name, 5,shops[castro_ID].ID);
        assertNull(pis);
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
    }

    @Test
    public void testUpdateProductByOwnerFailureBadID(){
        boolean updated = subscribedUserBridge.updateProductName(MegaSportFounder.name,shops[ACE_ID].ID,3,"fail");
        assertFalse(updated);

        ProductInShop pis  = subscribedUserBridge.searchProductInShop(MegaSportFounder.name, 3,shops[ACE_ID].ID);
        assertNull(pis);
    }

    @Test
    public void testUpdateProductByOwnerFailureBadPrice(){
        boolean updated =subscribedUserBridge.updateProductPrice(MegaSportFounder.name,shops[ACE_ID].ID,1,-50);
        assertFalse(updated);

        ProductInShop pis  = subscribedUserBridge.searchProductInShop(MegaSportFounder.name, 45,shops[MegaSport_ID].ID);
        assertNotNull(pis);
        assertEquals(pis.ID,1);
        assertEquals(pis.price,40,0);
    }

    @Test
    public void testUpdateProductByOwnerFailureBadPriceBadID(){
        boolean updated =subscribedUserBridge.updateProductPrice(MegaSportFounder.name,shops[ACE_ID].ID,11,-50);
        assertFalse(updated);

        ProductInShop pis  = subscribedUserBridge.searchProductInShop(MegaSportFounder.name, 11,shops[ACE_ID].ID);
        assertNull(pis);
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
            fail("double appointment!");
        }
        catch (Exception ignored){

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
        assertEquals(3,appointments.size());

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
            fail("double manager appointment");
        } catch (Exception ignored) {

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
        assertEquals(0,searchResult.size());

        List<ProductInShop> productInShops = subscribedUserBridge.searchShopProducts(supersalFounder.name, supersal.ID);
        assertNull(productInShops);

        //List<String> supersalFounderNotifications = u2.notifications;
        //assertEquals(1,supersalFounderNotifications.size());
        //assertEquals("supersal closed",supersalFounderNotifications.get(0));
    }

    @Test
    public void testAddManagerPermissionsSuccess(){
        testAppointShopManagerSuccess(); // u1 - manager at castro

        boolean result = subscribedUserBridge.addManagerPermission(shops[castro_ID].ID,castroFounder.name,u1.name, SubscribedUser.Permission.SET_PURCHASE_POLICY);
        assertTrue(result);

        result = subscribedUserBridge.addManagerPermission(shops[castro_ID].ID,castroFounder.name,u1.name, SubscribedUser.Permission.ROLE_INFO);
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

        boolean result = subscribedUserBridge.addManagerPermission(shops[castro_ID].ID,ACEFounder.name,u1.name, SubscribedUser.Permission.SET_PURCHASE_POLICY);
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
        boolean result = subscribedUserBridge.addOwnerPermission(shops[ACE_ID].ID,ACEFounder.name,MegaSportFounder.name, SubscribedUser.Permission.CLOSE_SHOP);
        assertTrue(result);

        Map<String,List<SubscribedUser.Permission>> permissions = subscribedUserBridge.getShopPermissions(MegaSportFounder.name,shops[ACE_ID].ID);
        assertNotNull(permissions);
        List<SubscribedUser.Permission> ownerPermissions = permissions.getOrDefault(MegaSportFounder.name,null);
        assertNotNull(ownerPermissions);
        assertEquals(defaultFounderPermissions.length, permissions.size());
        assertTrue(ownerPermissions.containsAll(List.of(defaultFounderPermissions)));
        // cancel side-effects
        subscribedUserBridge.removePermission(shops[ACE_ID].ID,ACEFounder.name,MegaSportFounder.name, SubscribedUser.Permission.CLOSE_SHOP);
    }

    @Test
    public void testAddOwnerPermissionsFailureNotAppointer(){
        testAppointShopOwnerSuccess(); // u1 - owner at castro

        boolean result = subscribedUserBridge.addOwnerPermission(shops[castro_ID].ID,ACEFounder.name,u1.name,SubscribedUser.Permission.CLOSE_SHOP);
        assertFalse(result);

        Map<String,List<SubscribedUser.Permission>> permissions = subscribedUserBridge.getShopPermissions(ACEFounder.name,shops[castro_ID].ID);

        assertNotNull(permissions);

        List<SubscribedUser.Permission> ownerPermissions = permissions.getOrDefault(u1.name,null);

        assertNotNull(ownerPermissions);
        assertEquals(defaultOwnerPermissions.length,ownerPermissions.size());
        assertTrue(ownerPermissions.containsAll(List.of(defaultOwnerPermissions)));
    }

    @Test
    public void testGetRoleInformationByFounderSuccessNoAppointments(){
        Map<String,Appointment> appointmentMap = subscribedUserBridge.getShopAppointments(castroFounder.name,shops[castro_ID].ID);

        assertNotNull(appointmentMap);
        assertEquals(1,appointmentMap.size());

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
        assertEquals(2,appointmentMap.size());

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
        assertEquals(2,appointmentMap.size());

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
        assertEquals(2,appointmentMap.size());

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

    public User enter() {
        Guest g = subscribedUserBridge.visit();
        return subscribedUserBridge.login(g.name,new RegistrationInfo( "enterUser","enterPass"));
    }
}