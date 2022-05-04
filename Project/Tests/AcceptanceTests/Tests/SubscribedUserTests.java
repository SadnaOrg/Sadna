package AcceptanceTests.Tests;

import AcceptanceTests.Bridge.SubscribedUserBridge;
import AcceptanceTests.Bridge.SubscribedUserProxy;
import AcceptanceTests.Threads.FounderAppointManager;
import AcceptanceTests.Threads.FounderDeletesProduct;
import AcceptanceTests.Threads.OwnerAppointManager;
import AcceptanceTests.Threads.UserBuysProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import AcceptanceTests.DataObjects.*;

import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
public class SubscribedUserTests extends UserTests {
    private static final SubscribedUserBridge subscribedUserBridge = new SubscribedUserProxy();

    private static Shop supersal;

    private final static String[] userNames = new String[]{"testUser3","buyer100","michael"};
    private final static String[] passwords = new String[]{"42","secret","leahcim"};

    private final static String[] defaultFounderPermissions = new String[]{"inventory management","change policies","set consistency constraints",
    "appoint owner","remove owner","appoint manager","remove manager","change managers' permissions","close shop","reopen shop",
    "get appointments in shop","get purchase history","respond to customers questions"};

    private final static String[] defaultOwnerPermissions = new String[]{"inventory management","change policies","set consistency constraints",
            "appoint owner","remove owner","appoint manager","remove manager","change managers' permissions","reopen shop",
            "get appointments in shop","get purchase history","respond to customers questions"};

    private final static String[] defaultManagerPermissions = new String[]{"view information"};

    private int counter = 0;
    private static SubscribedUser u1;
    private static SubscribedUser u2;
    private static SubscribedUser u3;
    private SubscribedUser supersalFounder;

    public static int getU1ID(){
        return u1.ID;
    }

    public static int getU2ID(){
        return u2.ID;
    }

    public static int getU3ID(){
        return u3.ID;
    }

    public static SubscribedUserBridge getUserBridge(){return subscribedUserBridge;}

    @BeforeEach
    public void setUpUsers(){
        Guest setUpU1 = subscribedUserBridge.visit();
        Guest setUpU2 = subscribedUserBridge.visit();
        Guest setUpU3 = subscribedUserBridge.visit();

        String name1 = userNames[0].concat(String.valueOf(counter));
        String pass1 = passwords[0].concat(String.valueOf(counter));
        String name2 = userNames[1].concat(String.valueOf(counter));
        String pass2 = passwords[1].concat(String.valueOf(counter));
        String name3 = userNames[2].concat(String.valueOf(counter));
        String pass3 = passwords[2].concat(String.valueOf(counter));

        u1 = subscribedUserBridge.register(setUpU1.ID,new RegistrationInfo(name1,pass1));
        u2 = subscribedUserBridge.register(setUpU2.ID,new RegistrationInfo(name2,pass2));
        u3 = subscribedUserBridge.register(setUpU3.ID,new RegistrationInfo(name3,pass3));

        counter += 1;
    }

    @Test
    public void testExitOldUser() {
        Guest newGuest = subscribedUserBridge.visit();
        SubscribedUser u = subscribedUserBridge.login(newGuest.ID,new RegistrationInfo(u1.username, u1.password));
        boolean res = subscribedUserBridge.exit(u.ID);
        assertTrue(res);
    }

    @Test
    public void testExitNewUserWithItems() {
        String testUserName = "newRegistered";
        String testPassword = "testExit";

        Guest newGuest = subscribedUserBridge.visit();
        SubscribedUser u = subscribedUserBridge.register(newGuest.ID,new RegistrationInfo(testUserName, testPassword));
        boolean added = subscribedUserBridge.addProductToCart(u.ID,shops[castro_ID].ID,2,10);
        assertTrue(added);

        Guest newG = subscribedUserBridge.logout(u.ID);

        u = subscribedUserBridge.login(newG.ID,new RegistrationInfo(testUserName, testPassword));
        ShoppingCart userCart = subscribedUserBridge.checkCart(u.ID);
        assertNotNull(userCart);
        assertEquals(10,userCart.numOfProductsInCart());
        assertEquals(1,userCart.getNumberOfBaskets());

        ShopBasket basket = userCart.getShopBasket(shops[castro_ID].ID);
        assertNotNull(basket);

        int quantity = basket.getProductQuantity(2);
        assertEquals(10,quantity);

        //cancel side-effects
        subscribedUserBridge.updateCart(u.ID,new int[]{2},new int[]{shops[castro_ID].ID},new int[]{0});

        boolean exitResult = subscribedUserBridge.exit(u.ID);
        assertTrue(exitResult);

    }

    @Test
    public void testEnterNewUserSuccess() {
        Guest newGuest = subscribedUserBridge.visit();
        SubscribedUser u = subscribedUserBridge.register(newGuest.ID,new RegistrationInfo("newName","newPassword"));

        assertNotNull(u);
        assertEquals("newName",u.password);
        assertEquals("newPassword",u.username);

        boolean exitResult = subscribedUserBridge.exit(u.ID);
        assertTrue(exitResult);
    }

    @Test
    public void testEnterNewUserFailure() {
        Guest newGuest = subscribedUserBridge.visit();
        SubscribedUser u = subscribedUserBridge.register(newGuest.ID,new RegistrationInfo(u1.username, u1.password));
        assertNull(u);
    }

    @Test
    public void testOpenShopSuccess(){
        Guest newGuest = subscribedUserBridge.visit();
        supersalFounder = subscribedUserBridge.login(newGuest.ID,new RegistrationInfo(u1.username, u1.password));
        supersal = subscribedUserBridge.openShop(supersalFounder.ID,"supersal","food");

        assertNotNull(supersal);
        assertEquals("supersal",supersal.name);
        assertEquals("food",supersal.category);

        Appointment appointment = supersalFounder.getRole(supersal.ID);
        assertEquals("Founder",appointment.role);

        //the shop is closed as a part of another test.

        boolean exitResult = subscribedUserBridge.exit(supersalFounder.ID);
        assertTrue(exitResult);
    }

    @Test
    public void testOpenShopFailure(){
        Guest newGuest = subscribedUserBridge.visit();
        SubscribedUser u = subscribedUserBridge.login(newGuest.ID,new RegistrationInfo(userNames[0], passwords[0]));
        Shop s = subscribedUserBridge.openShop(u.ID,"castro","fashion");

        assertNull(s);

        Appointment appointment = u.getRole(shops[castro_ID].ID);
        assertNull(appointment);

        boolean exitResult = subscribedUserBridge.exit(u.ID);
        assertTrue(exitResult);
    }

    @Test
    public void testAddProductToShopByFounderSuccess(){
        Guest newGuest = subscribedUserBridge.visit();
        SubscribedUser u = subscribedUserBridge.login(newGuest.ID,new RegistrationInfo(castroFounder.username,castroFounder.password));

        Appointment appointment = u.getRole(shops[castro_ID].ID);
        assertEquals("Founder",appointment.role);
        Product newproduct = new Product("belt","china");

        subscribedUserBridge.addProductToShop(u.ID,shops[castro_ID].ID,newproduct,222,4.2,12,67.5);
        ProductInShop pis  = subscribedUserBridge.searchProductInShop(222,shops[castro_ID].ID);

        assertNotNull(pis);
        assertEquals(222,pis.ID);
        assertEquals(12,pis.quantity);
        assertEquals(67.5, pis.price, 0.0);

        //cancel side-effects
        subscribedUserBridge.deleteProductFromShop(u.ID,shops[castro_ID].ID,222);

        boolean exitResult = subscribedUserBridge.exit(u.ID);
        assertTrue(exitResult);
    }

    @Test
    public void testAddProductToShopByFounderFailureBadQuantity(){
        Guest newGuest = subscribedUserBridge.visit();
        SubscribedUser u = subscribedUserBridge.login(newGuest.ID,new RegistrationInfo(castroFounder.username,castroFounder.password));

        Appointment appointment = u.getRole(shops[castro_ID].ID);
        assertEquals("Founder",appointment.role);
        Product newproduct = new Product("belt","china");

        boolean added = subscribedUserBridge.addProductToShop(u.ID,shops[castro_ID].ID,newproduct,222,4.2,-12,67.5);
        assertFalse(added);

        ProductInShop pis  = subscribedUserBridge.searchProductInShop(222,shops[castro_ID].ID);
        assertNull(pis);

        boolean exitResult = subscribedUserBridge.exit(u.ID);
        assertTrue(exitResult);
    }

    @Test
    public void testAddProductToShopByFounderFailureBadPrice(){
        Guest newGuest = subscribedUserBridge.visit();
        SubscribedUser u = subscribedUserBridge.login(newGuest.ID,new RegistrationInfo(castroFounder.username,castroFounder.password));

        Appointment appointment = u.getRole(shops[castro_ID].ID);
        assertEquals("Founder",appointment.role);
        Product newproduct = new Product("belt","china");

        boolean added = subscribedUserBridge.addProductToShop(u.ID,shops[castro_ID].ID,newproduct,222,4.2,12,-12);
        assertFalse(added);

        ProductInShop pis  = subscribedUserBridge.searchProductInShop(222,shops[castro_ID].ID);
        assertNull(pis);

        boolean exitResult = subscribedUserBridge.exit(u.ID);
        assertTrue(exitResult);
    }

    @Test
    public void testAddProductToShopByFounderFailureBadPriceBadQuantity(){
        Guest newGuest = subscribedUserBridge.visit();
        SubscribedUser u = subscribedUserBridge.login(newGuest.ID,new RegistrationInfo(castroFounder.username,castroFounder.password));

        Appointment appointment = u.getRole(shops[castro_ID].ID);
        assertEquals("Founder",appointment.role);
        Product newproduct = new Product("belt","china");

        boolean added = subscribedUserBridge.addProductToShop(u.ID,shops[castro_ID].ID,newproduct,222,4.2,-12,-12);
        assertFalse(added);

        ProductInShop pis  = subscribedUserBridge.searchProductInShop(222,shops[castro_ID].ID);
        assertNull(pis);

        boolean exitResult = subscribedUserBridge.exit(u.ID);
        assertTrue(exitResult);
    }

    @Test
    public void testDeleteProductFromShopByManagerSuccess(){
        Guest newGuest = subscribedUserBridge.visit();
        SubscribedUser u = subscribedUserBridge.login(newGuest.ID,new RegistrationInfo(castroFounder.username,castroFounder.password));

        Appointment appointment = u.getRole(shops[MegaSport_ID].ID);
        assertEquals("Manager",appointment.role);

        boolean deleted = subscribedUserBridge.deleteProductFromShop(u.ID,shops[MegaSport_ID].ID,13);
        assertTrue(deleted);

        ProductInShop pis  = subscribedUserBridge.searchProductInShop(13,shops[MegaSport_ID].ID);
        assertNull(pis);

        //cancel side-effects
        Product p1 = new Product("running shoes","china");
        subscribedUserBridge.addProductToShop(u.ID,shops[MegaSport_ID].ID,p1,13,3,30,40);

        boolean exitResult = subscribedUserBridge.exit(u.ID);
        assertTrue(exitResult);
    }

    @Test
    public void testDeleteProductFromShopByManagerFailureBadID(){
        Guest newGuest = subscribedUserBridge.visit();
        SubscribedUser u = subscribedUserBridge.login(newGuest.ID,new RegistrationInfo(castroFounder.username,castroFounder.password));

        Appointment appointment = u.getRole(shops[MegaSport_ID].ID);
        assertEquals("Manager",appointment.role);

        boolean deleted = subscribedUserBridge.deleteProductFromShop(u.ID,shops[MegaSport_ID].ID,5);
        assertFalse(deleted);

        ProductInShop pis  = subscribedUserBridge.searchProductInShop(5,shops[MegaSport_ID].ID);
        assertNull(pis);

        boolean exitResult = subscribedUserBridge.exit(u.ID);
        assertTrue(exitResult);
    }

    @Test
    public void testUpdateProductByFounderSuccess(){
        Guest newGuest = subscribedUserBridge.visit();
        SubscribedUser u = subscribedUserBridge.login(newGuest.ID,new RegistrationInfo(castroFounder.username,castroFounder.password));

        Appointment appointment = u.getRole(shops[castro_ID].ID);
        assertEquals("Founder",appointment.role);

        boolean updated = subscribedUserBridge.updateProduct(u.ID,shops[castro_ID].ID,45,45,310,55.5);
        assertFalse(updated);

        ProductInShop pis  = subscribedUserBridge.searchProductInShop(45,shops[castro_ID].ID);
        assertNotNull(pis);
        assertEquals(45,pis.ID);
        assertEquals(310,pis.quantity);
        assertEquals(55.5,pis.price,0);

        //cancel side-effects
        subscribedUserBridge.updateProduct(u.ID,shops[castro_ID].ID,45,45,40,50);

        boolean exitResult = subscribedUserBridge.exit(u.ID);
        assertTrue(exitResult);
    }

    @Test
    public void testUpdateProductByManagerFailureBadID(){
        Guest newGuest = subscribedUserBridge.visit();
        SubscribedUser u = subscribedUserBridge.login(newGuest.ID,new RegistrationInfo(castroFounder.username,castroFounder.password));

        Appointment appointment = u.getRole(shops[MegaSport_ID].ID);
        assertEquals("Manager",appointment.role);

        boolean updated = subscribedUserBridge.updateProduct(u.ID,shops[MegaSport_ID].ID,2,4,50,77.99);
        assertFalse(updated);

        ProductInShop pis  = subscribedUserBridge.searchProductInShop(2,shops[MegaSport_ID].ID);
        assertNull(pis);

        boolean exitResult = subscribedUserBridge.exit(u.ID);
        assertTrue(exitResult);
    }

    @Test
    public void testUpdateProductByManagerFailureBadPrice(){
        Guest newGuest = subscribedUserBridge.visit();
        SubscribedUser u = subscribedUserBridge.login(newGuest.ID,new RegistrationInfo(castroFounder.username,castroFounder.password));

        Appointment appointment = u.getRole(shops[MegaSport_ID].ID);
        assertEquals("Manager",appointment.role);

        boolean updated = subscribedUserBridge.updateProduct(u.ID,shops[MegaSport_ID].ID,45,45,50,-3);
        assertFalse(updated);

        ProductInShop pis  = subscribedUserBridge.searchProductInShop(2,shops[MegaSport_ID].ID);
        assertNull(pis);

        boolean exitResult = subscribedUserBridge.exit(u.ID);
        assertTrue(exitResult);
    }

    @Test
    public void testUpdateProductByManagerFailureBadPriceBadID(){
        Guest newGuest = subscribedUserBridge.visit();
        SubscribedUser u = subscribedUserBridge.login(newGuest.ID,new RegistrationInfo(castroFounder.username,castroFounder.password));

        Appointment appointment = u.getRole(shops[MegaSport_ID].ID);
        assertEquals("Manager",appointment.role);

        boolean updated = subscribedUserBridge.updateProduct(u.ID,shops[MegaSport_ID].ID,2,45,50,-3);
        assertFalse(updated);

        ProductInShop pis  = subscribedUserBridge.searchProductInShop(2,shops[MegaSport_ID].ID);
        assertNull(pis);

        boolean exitResult = subscribedUserBridge.exit(u.ID);
        assertTrue(exitResult);
    }

    @Test
    public void testAppointShopOwnerSuccess(){
        Guest newGuest = subscribedUserBridge.visit();
        SubscribedUser u = subscribedUserBridge.login(newGuest.ID,new RegistrationInfo(castroFounder.username,castroFounder.password));

        boolean result = subscribedUserBridge.appointOwner(shops[castro_ID].ID,castroFounder.ID,u1.ID);
        assertTrue(result);

        Appointment role = u1.getRole(castro_ID);
        assertNotNull(role);
        assertEquals("Owner",role.getRole());
        assertEquals(castroFounder.ID,role.getAppointer());

        boolean exitResult = subscribedUserBridge.exit(u.ID);
        assertTrue(exitResult);
    }

    @Test
    public void testAppointShopOwnerByOwnerSuccess(){
        Guest newGuest = subscribedUserBridge.visit();
        SubscribedUser u = subscribedUserBridge.login(newGuest.ID,new RegistrationInfo(MegaSportFounder.username,MegaSportFounder.password));

        boolean result = subscribedUserBridge.appointOwner(shops[ACE_ID].ID,MegaSportFounder.ID,u1.ID);
        assertTrue(result);

        Appointment role = u1.getRole(ACE_ID);
        assertNotNull(role);
        assertEquals("Owner",role.getRole());
        assertEquals(MegaSportFounder.ID,role.getAppointer());

        boolean exitResult = subscribedUserBridge.exit(u.ID);
        assertTrue(exitResult);
    }

    @Test
    public void testAppointShopOwnerFailureDoubleAppointment(){
        Guest newGuest = subscribedUserBridge.visit();
        SubscribedUser u = subscribedUserBridge.login(newGuest.ID,new RegistrationInfo(castroFounder.username,castroFounder.password));

        subscribedUserBridge.appointOwner(shops[castro_ID].ID,castroFounder.ID,u2.ID);
        boolean result = subscribedUserBridge.appointOwner(shops[castro_ID].ID,castroFounder.ID,u2.ID);
        assertFalse(result);

        boolean exitResult = subscribedUserBridge.exit(u.ID);
        assertTrue(exitResult);
    }

    @Test
    public void testAppointShopOwnerFailureCircularAppointment(){
        Guest newGuest = subscribedUserBridge.visit();
        SubscribedUser u = subscribedUserBridge.login(newGuest.ID,new RegistrationInfo(MegaSportFounder.username,MegaSportFounder.password));

        boolean appointment1 = subscribedUserBridge.appointOwner(shops[MegaSport_ID].ID,u.ID,u1.ID);
        assertTrue(appointment1);

        boolean appointment2 = subscribedUserBridge.appointOwner(shops[MegaSport_ID].ID,u1.ID,u2.ID);
        assertTrue(appointment2);

        boolean appointment3 = subscribedUserBridge.appointOwner(shops[MegaSport_ID].ID,u2.ID,u3.ID);
        assertTrue(appointment3);

        boolean result = subscribedUserBridge.appointOwner(shops[MegaSport_ID].ID,u3.ID,u1.ID);
        assertFalse(result);

        boolean exitResult = subscribedUserBridge.exit(u.ID);
        assertTrue(exitResult);
    }

    @Test
    public void testAppointShopOwnerFailureAppointmentByGuest(){
        Guest newGuest = subscribedUserBridge.visit();

        boolean result = subscribedUserBridge.appointOwner(shops[castro_ID].ID,newGuest.ID,u1.ID);
        assertFalse(result);

        boolean exitResult = subscribedUserBridge.exit(newGuest.ID);
        assertTrue(exitResult);
    }

    @Test
    public void testAppointShopOwnerFailureAppointmentByManagerNotOwner(){
        Guest newGuest = subscribedUserBridge.visit();
        SubscribedUser u = subscribedUserBridge.login(newGuest.ID,new RegistrationInfo(castroFounder.username,castroFounder.password));

        boolean result = subscribedUserBridge.appointOwner(shops[MegaSport_ID].ID,u.ID,u1.ID);
        assertFalse(result);

        boolean exitResult = subscribedUserBridge.exit(newGuest.ID);
        assertTrue(exitResult);
    }

    @Test
    public void testAppointShopManagerSuccess(){
        Guest newGuest = subscribedUserBridge.visit();
        SubscribedUser u = subscribedUserBridge.login(newGuest.ID,new RegistrationInfo(castroFounder.username,castroFounder.password));

        boolean result = subscribedUserBridge.appointManager(shops[castro_ID].ID,u.ID,u1.ID);
        assertTrue(result);

        Appointment role = u1.getRole(castro_ID);
        assertNotNull(role);
        assertEquals("Manager",role.getRole());
        assertEquals(castroFounder.ID,role.getAppointer());

        boolean exitResult = subscribedUserBridge.exit(newGuest.ID);
        assertTrue(exitResult);
    }

    @Test
    public void testConcurrentManagerAppointment() throws InterruptedException {
        Guest g = subscribedUserBridge.visit();
        SubscribedUser u = subscribedUserBridge.login(g.ID,new RegistrationInfo(u1.username, u1.password));

        Shop s = subscribedUserBridge.openShop(u.ID,"new Shop","gaming");
        boolean appointed = subscribedUserBridge.appointOwner(s.ID,u.ID,u2.ID);
        assertTrue(appointed);

        boolean exit = subscribedUserBridge.exit(u.ID);
        assertTrue(exit);

        Thread MegaSportFounderAppointment = new FounderAppointManager(s.ID,u);
        Thread ACEFounderAppointment = new OwnerAppointManager(s.ID,u2);

        MegaSportFounderAppointment.start();
        ACEFounderAppointment.start();
        MegaSportFounderAppointment.join();
        ACEFounderAppointment.join();

        g = subscribedUserBridge.visit();
        u = subscribedUserBridge.login(g.ID,new RegistrationInfo(u1.username, u1.password));

        Map<Integer,Appointment> appointments = subscribedUserBridge.getShopAppointments(u.ID,s.ID);
        assertNotNull(appointments);
        assertEquals(3,appointments.size());

        Appointment founderAppointment = appointments.getOrDefault(u.ID,null);
        Appointment ownerAppointment = appointments.getOrDefault(u2.ID,null);
        Appointment managerAppointment = appointments.getOrDefault(u3.ID,null);

        assertNotNull(founderAppointment);
        assertNotNull(ownerAppointment);
        assertNotNull(managerAppointment);

        assertEquals(-1,founderAppointment.appointer);
        assertEquals(u.ID,ownerAppointment.appointer);
        assertTrue(managerAppointment.appointer == u2.ID || managerAppointment.appointer == u.ID);

        assertEquals("Founder",founderAppointment.role);
        assertEquals("Owner",ownerAppointment.role);
        assertEquals("Manager",managerAppointment.role);

        exit = subscribedUserBridge.exit(u.ID);
        assertTrue(exit);
    }

    @Test
    public void testAppointShopManagerFailureAlreadyManager(){
        Guest newGuest = subscribedUserBridge.visit();
        SubscribedUser u = subscribedUserBridge.login(newGuest.ID,new RegistrationInfo(castroFounder.username,castroFounder.password));

        boolean result = subscribedUserBridge.appointManager(shops[castro_ID].ID,u.ID,u1.ID);
        assertTrue(result);

        Appointment role = u1.getRole(castro_ID);
        assertNotNull(role);
        assertEquals("Manager",role.getRole());
        assertEquals(castroFounder.ID,role.getAppointer());

        result = subscribedUserBridge.appointManager(shops[castro_ID].ID,u.ID,u1.ID);
        assertFalse(result);

        boolean exitResult = subscribedUserBridge.exit(newGuest.ID);
        assertTrue(exitResult);
    }

    @Test
    public void testAppointShopManagerFailureNotOwner(){
        Guest newGuest = subscribedUserBridge.visit();
        SubscribedUser u = subscribedUserBridge.login(newGuest.ID,new RegistrationInfo(castroFounder.username,castroFounder.password));

        boolean result = subscribedUserBridge.appointManager(shops[MegaSport_ID].ID,u.ID,u1.ID);
        assertFalse(result);

        boolean exitResult = subscribedUserBridge.exit(newGuest.ID);
        assertTrue(exitResult);
    }

    @Test
    public void testCloseShopSuccess(){
        Guest newGuest = subscribedUserBridge.visit();
        SubscribedUser u = subscribedUserBridge.login(newGuest.ID,new RegistrationInfo(supersalFounder.username, supersalFounder.password));
        ShopFilter shopFilterName = (s) -> s.name.equals("supersal");

        boolean result = subscribedUserBridge.closeShop(supersal.ID,u.ID);
        assertTrue(result);

        List<Shop> searchResult = subscribedUserBridge.getShopsInfo(shopFilterName);
        assertEquals(0,searchResult.size());

        List<ProductInShop> productInShops = subscribedUserBridge.searchShopProducts(supersal.ID);
        assertNull(productInShops);

        List<String> supersalFounderNotifications = u2.notifications;
        assertEquals(1,supersalFounderNotifications.size());
        assertEquals("supersal closed",supersalFounderNotifications.get(0));

        boolean exitResult = subscribedUserBridge.exit(newGuest.ID);
        assertTrue(exitResult);
    }

    @Test
    public void testAddManagerPermissionsSuccess(){
        Guest newGuest = subscribedUserBridge.visit();
        SubscribedUser u = subscribedUserBridge.login(newGuest.ID,new RegistrationInfo(MegaSportFounder.username,MegaSportFounder.password));

        boolean result = subscribedUserBridge.addManagerPermission(shops[MegaSport_ID].ID,u.ID,castroFounder.ID,"change policies");
        assertTrue(result);

        List<String> permissions = castroFounder.getPermissions(shops[MegaSport_ID].ID);
        assertTrue(permissions.contains("change policies"));
        assertTrue(permissions.contains("view information"));
        assertEquals(2,permissions.size());

        boolean exitResult = subscribedUserBridge.exit(newGuest.ID);
        assertTrue(exitResult);
    }

    @Test
    public void testAddManagerPermissionsFailureNotAppointer(){
        Guest newGuest = subscribedUserBridge.visit();
        SubscribedUser u = subscribedUserBridge.login(newGuest.ID,new RegistrationInfo(ACEFounder.username,ACEFounder.password));

        boolean result = subscribedUserBridge.appointManager(shops[ACE_ID].ID,u.ID,u1.ID);
        assertTrue(result);

        result = subscribedUserBridge.addManagerPermission(shops[ACE_ID].ID,u1.ID,MegaSportFounder.ID,"change policies");
        assertFalse(result);

        List<String> permissions = MegaSportFounder.getPermissions(shops[ACE_ID].ID);
        for (String permission:
             defaultOwnerPermissions) {
            assertTrue(MegaSportFounder.getPermissions(shops[ACE_ID].ID).contains(permission));
        }

        assertEquals(defaultOwnerPermissions.length,permissions.size());

        boolean exitResult = subscribedUserBridge.exit(newGuest.ID);
        assertTrue(exitResult);
    }

    @Test
    public void testAddOwnerPermissionsSuccess(){
        Guest newGuest = subscribedUserBridge.visit();
        SubscribedUser u = subscribedUserBridge.login(newGuest.ID,new RegistrationInfo(ACEFounder.username,ACEFounder.password));

        boolean result = subscribedUserBridge.addOwnerPermission(shops[ACE_ID].ID,u.ID,MegaSportFounder.ID,"change policies");
        assertTrue(result);

        List<String> permissions = MegaSportFounder.getPermissions(shops[MegaSport_ID].ID);
        assertTrue(permissions.contains("change policies"));
        assertTrue(permissions.contains("view information"));
        assertEquals(2, permissions.size());

        boolean exitResult = subscribedUserBridge.exit(newGuest.ID);
        assertTrue(exitResult);
    }

    @Test
    public void testAddOwnerPermissionsFailureNotAppointer(){
        Guest newGuest = subscribedUserBridge.visit();
        SubscribedUser u = subscribedUserBridge.login(newGuest.ID,new RegistrationInfo(MegaSportFounder.username,MegaSportFounder.password));

        boolean result = subscribedUserBridge.appointOwner(shops[MegaSport_ID].ID,u.ID,u1.ID);
        assertTrue(result);

        List<String> ownerPermissions = u1.getPermissions(shops[MegaSport_ID].ID);

        for (String permission:
                defaultManagerPermissions) {
            assertTrue(ownerPermissions.contains(permission));
        }

        result = subscribedUserBridge.addManagerPermission(shops[ACE_ID].ID,u1.ID,castroFounder.ID,"change policies");
        assertFalse(result);

        List<String> permissions = castroFounder.getPermissions(shops[ACE_ID].ID);
        for (String permission:
             defaultManagerPermissions) {
            assertTrue(permissions.contains(permission));
        }

        boolean exitResult = subscribedUserBridge.exit(newGuest.ID);
        assertTrue(exitResult);
    }

    @Test
    public void testGetRoleInformationByFounderSuccessNoAppointments(){
        Guest newGuest = subscribedUserBridge.visit();
        SubscribedUser u = subscribedUserBridge.login(newGuest.ID,new RegistrationInfo(u1.username,u1.password));

        Shop s = subscribedUserBridge.openShop(u.ID,"testShop1","technology");

        Map<Integer,Appointment> appointmentsInShop = subscribedUserBridge.getShopAppointments(u.ID,s.ID);
        Map<Integer,List<String>> permissionsInShop = subscribedUserBridge.getShopPermissions(u.ID,s.ID);

        assertNotNull(appointmentsInShop);
        assertNotNull(permissionsInShop);
        assertEquals(1,appointmentsInShop.size());
        assertEquals(1,permissionsInShop.size());

        Appointment founderAppointment = appointmentsInShop.getOrDefault(u.ID,null);
        assertNotNull(founderAppointment);
        assertEquals("Founder",founderAppointment.role);
        assertEquals(-1,founderAppointment.appointer);

        List<String> founderPermissions = permissionsInShop.getOrDefault(u.ID,null);
        assertNotNull(founderPermissions);

        assertEquals(defaultFounderPermissions.length,founderPermissions.size());
        for (String permission:
                defaultFounderPermissions) {
            assertTrue(founderPermissions.contains(permission));
        }

        //cancel side-effects
        subscribedUserBridge.closeShop(s.ID,u.ID);

        boolean exit = subscribedUserBridge.exit(u.ID);
        assertTrue(exit);
    }

    @Test
    public void testGetRoleInformationByFounderSuccessWithAppointments(){
        Guest newGuest1 = subscribedUserBridge.visit();
        SubscribedUser founder = subscribedUserBridge.login(newGuest1.ID,new RegistrationInfo(u1.username,u1.password));

        Shop s = subscribedUserBridge.openShop(founder.ID,"testShop1","technology");

        boolean appointed = subscribedUserBridge.appointManager(s.ID,founder.ID,u2.ID);
        assertTrue(appointed);

        boolean addedPermission = subscribedUserBridge.addManagerPermission(s.ID,founder.ID,u2.ID,"change policies");
        assertTrue(addedPermission);

        Map<Integer,Appointment> appointmentsInShop = subscribedUserBridge.getShopAppointments(founder.ID,s.ID);
        Map<Integer,List<String>> permissionsInShop = subscribedUserBridge.getShopPermissions(founder.ID,s.ID);

        assertNotNull(appointmentsInShop);
        assertNotNull(permissionsInShop);
        assertEquals(2,appointmentsInShop.size());
        assertEquals(2,permissionsInShop.size());

        Appointment founderAppointment = appointmentsInShop.getOrDefault(founder.ID,null);
        Appointment managerAppointment = appointmentsInShop.getOrDefault(u2.ID,null);
        assertNotNull(founderAppointment);
        assertNotNull(managerAppointment);

        assertEquals("Founder",founderAppointment.role);
        assertEquals(-1,founderAppointment.appointer);
        assertEquals("Manager",managerAppointment.role);
        assertEquals(founder.ID,managerAppointment.appointer);

        List<String> founderPermissions = permissionsInShop.getOrDefault(founder.ID,null);
        List<String> managerPermissions = permissionsInShop.getOrDefault(u2.ID,null);
        assertNotNull(founderPermissions);
        assertNotNull(managerPermissions);

        assertEquals(defaultFounderPermissions.length,founderPermissions.size());
        assertEquals(2,managerPermissions.size());
        for (String permission:
             defaultFounderPermissions) {
            assertTrue(founderPermissions.contains(permission));
        }
        assertTrue(managerPermissions.contains("change policies"));
        assertTrue(managerPermissions.contains("view information"));

        //cancel side-effects
        subscribedUserBridge.closeShop(s.ID,founder.ID);
        boolean exit = subscribedUserBridge.exit(founder.ID);
        assertTrue(exit);
    }

    @Test
    public void testGetRoleInformationByManagerSuccess(){
        Guest newGuest1 = subscribedUserBridge.visit();
        SubscribedUser founder = subscribedUserBridge.login(newGuest1.ID,new RegistrationInfo(u1.username,u1.password));

        Shop s = subscribedUserBridge.openShop(founder.ID,"testShop1","technology");

        boolean appointed = subscribedUserBridge.appointManager(s.ID,founder.ID,u2.ID);
        assertTrue(appointed);

        boolean addedPermission = subscribedUserBridge.addManagerPermission(s.ID,founder.ID,u2.ID,"change policies");
        assertTrue(addedPermission);

        boolean exit = subscribedUserBridge.exit(founder.ID);
        assertTrue(exit);

        Guest newGuest = subscribedUserBridge.visit();
        SubscribedUser manager = subscribedUserBridge.login(newGuest.ID,new RegistrationInfo(u2.username,u2.password));

        Map<Integer,Appointment> appointmentsInShop = subscribedUserBridge.getShopAppointments(manager.ID,s.ID);
        Map<Integer,List<String>> permissionsInShop = subscribedUserBridge.getShopPermissions(manager.ID,s.ID);

        assertNotNull(appointmentsInShop);
        assertNotNull(permissionsInShop);
        assertEquals(2,appointmentsInShop.size());
        assertEquals(2,permissionsInShop.size());

        Appointment founderAppointment = appointmentsInShop.getOrDefault(founder.ID,null);
        Appointment managerAppointment = appointmentsInShop.getOrDefault(manager.ID,null);
        assertNotNull(founderAppointment);
        assertNotNull(managerAppointment);
        assertEquals("Founder",founderAppointment.role);
        assertEquals(-1,founderAppointment.appointer);
        assertEquals("Manager",managerAppointment.role);
        assertEquals(founder.ID,managerAppointment.appointer);

        List<String> founderPermissions = permissionsInShop.getOrDefault(founder.ID,null);
        List<String> managerPermissions = permissionsInShop.getOrDefault(manager.ID,null);
        assertNotNull(founderPermissions);
        assertNotNull(managerPermissions);

        assertEquals(defaultFounderPermissions.length,founderPermissions.size());
        assertEquals(1,managerPermissions.size());
        for (String permission:
                defaultFounderPermissions) {
            assertTrue(founderPermissions.contains(permission));
        }
        assertTrue(managerPermissions.contains("view information"));
        assertEquals(1,managerPermissions.size());

        exit = subscribedUserBridge.exit(manager.ID);
        assertTrue(exit);

        newGuest1 = subscribedUserBridge.visit();
        founder = subscribedUserBridge.login(newGuest1.ID,new RegistrationInfo(u1.username,u1.password));

        //cancel side-effects
        subscribedUserBridge.closeShop(s.ID,founder.ID);

        exit = subscribedUserBridge.exit(founder.ID);
        assertTrue(exit);
    }

    @Test
    public void testGetRoleInformationByOwnerSuccess(){
        Guest newGuest1 = subscribedUserBridge.visit();
        SubscribedUser founder = subscribedUserBridge.login(newGuest1.ID,new RegistrationInfo(u1.username,u1.password));

        Shop s = subscribedUserBridge.openShop(founder.ID,"testShop1","technology");

        boolean appointed = subscribedUserBridge.appointOwner(s.ID,founder.ID,u2.ID);
        assertTrue(appointed);

        boolean exit = subscribedUserBridge.exit(founder.ID);
        assertTrue(exit);

        Guest newGuest = subscribedUserBridge.visit();
        SubscribedUser owner = subscribedUserBridge.login(newGuest.ID,new RegistrationInfo(u2.username,u2.password));

        Map<Integer,Appointment> appointmentsInShop = subscribedUserBridge.getShopAppointments(owner.ID,s.ID);
        Map<Integer,List<String>> permissionsInShop = subscribedUserBridge.getShopPermissions(owner.ID,s.ID);

        assertNotNull(appointmentsInShop);
        assertNotNull(permissionsInShop);
        assertEquals(2,appointmentsInShop.size());
        assertEquals(2,permissionsInShop.size());

        Appointment founderAppointment = appointmentsInShop.getOrDefault(founder.ID,null);
        Appointment ownerAppointment = appointmentsInShop.getOrDefault(owner.ID,null);
        assertNotNull(founderAppointment);
        assertNotNull(ownerAppointment);
        assertEquals("Founder",founderAppointment.role);
        assertEquals(-1,founderAppointment.appointer);
        assertEquals("Owner",ownerAppointment.role);
        assertEquals(founder.ID,ownerAppointment.appointer);

        List<String> founderPermissions = permissionsInShop.getOrDefault(founder.ID,null);
        List<String> ownerPermissions = permissionsInShop.getOrDefault(owner.ID,null);
        assertNotNull(founderPermissions);
        assertNotNull(ownerPermissions);

        assertEquals(defaultFounderPermissions.length,founderPermissions.size());
        assertEquals(defaultOwnerPermissions.length,ownerPermissions.size());
        for (String permission:
                defaultFounderPermissions) {
            assertTrue(founderPermissions.contains(permission));
        }

        for (String permission:
             defaultOwnerPermissions) {
            assertTrue(ownerPermissions.contains(permission));
        }

        exit = subscribedUserBridge.exit(owner.ID);
        assertTrue(exit);

        newGuest1 = subscribedUserBridge.visit();
        founder = subscribedUserBridge.login(newGuest1.ID,new RegistrationInfo(u1.username,u1.password));

        //cancel side-effects
        subscribedUserBridge.closeShop(s.ID,founder.ID);
        exit = subscribedUserBridge.exit(founder.ID);
        assertTrue(exit);
    }

    @Test
    public void testGetRoleInformationFailureUserNotShopOwnerOrManager(){
        Guest guest = subscribedUserBridge.visit();
        SubscribedUser u = subscribedUserBridge.login(guest.ID,new RegistrationInfo(u1.username, u1.password));

        Map<Integer,Appointment> appointments = subscribedUserBridge.getShopAppointments(u.ID,shops[castro_ID].ID);
        Map<Integer,List<String>> permissions = subscribedUserBridge.getShopPermissions(u.ID,shops[castro_ID].ID);

        assertNull(appointments);
        assertNull(permissions);

        boolean exit = subscribedUserBridge.exit(u.ID);
        assertTrue(exit);
    }

    @Test
    public void testGetRoleInformationFailurNoSuchShop(){
        Guest guest = subscribedUserBridge.visit();
        SubscribedUser u = subscribedUserBridge.login(guest.ID,new RegistrationInfo(castroFounder.username, castroFounder.password));

        Map<Integer,Appointment> appointments = subscribedUserBridge.getShopAppointments(u.ID,11);
        Map<Integer,List<String>> permissions = subscribedUserBridge.getShopPermissions(u.ID,11);

        assertNull(appointments);
        assertNull(permissions);

        boolean exit = subscribedUserBridge.exit(u.ID);
        assertTrue(exit);
    }

    @Test
    public void testAddProductToCartWhenOwnerDeletesIt() throws InterruptedException {
        Guest g = subscribedUserBridge.visit();
        SubscribedUser u = subscribedUserBridge.login(g.ID,new RegistrationInfo(u1.username, u1.password));

        Shop s = subscribedUserBridge.openShop(u.ID,"new Shop","gaming");
        subscribedUserBridge.addProductToShop(u.ID,s.ID,new Product("x-box","microsoft"),4,4.3,10,2000);

        FounderDeletesProduct founderDeletesProduct = new FounderDeletesProduct(s.ID,4,u);
        UserBuysProduct userBuysProduct = new UserBuysProduct(s.ID,4,u2);

        founderDeletesProduct.start();
        userBuysProduct.start();
        founderDeletesProduct.join();
        userBuysProduct.join();

        boolean purchaseStatus = userBuysProduct.getStatus();
        boolean productRemovalStatus = founderDeletesProduct.getStatus();

        assertFalse(purchaseStatus && productRemovalStatus);
        assertTrue(purchaseStatus || productRemovalStatus);

        ProductInShop pis = subscribedUserBridge.searchProductInShop(4,s.ID);
        ShoppingCart cart = subscribedUserBridge.checkCart(u2.ID);
        assertNotNull(cart);

        ShopBasket basket = cart.getShopBasket(s.ID);

        assertNull(pis);
        if(productRemovalStatus){
            assertNull(basket);
            assertEquals(0,cart.numOfProductsInCart());
            assertEquals(0,cart.getNumberOfBaskets());
        }
        else{
            assertNotNull(basket);
            assertEquals(1,cart.getNumberOfBaskets());
            assertEquals(1,basket.numOfProducts());
        }

        boolean exit = subscribedUserBridge.exit(u.ID);
        assertTrue(exit);

        exit = subscribedUserBridge.exit(u2.ID);
        assertTrue(exit);
    }

    public User enter() {
        Guest g = subscribedUserBridge.visit();
        return subscribedUserBridge.login(g.ID,new RegistrationInfo( userNames[0].concat("0"),passwords[0].concat("0")));
    }
}