package test.Tests;

import test.Mocks.*;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
//TODO: test on combinations of bad data for update and add
//TODO: test policies
public class SubscribedUserTests extends UserTests{

    private static SubscribedUser u1;
    private static SubscribedUser u2;
    private static SubscribedUser u3;

    private final static String[] userNames = new String[]{"testUser3","buyer100","michael"};
    private final static String[] passwords = new String[]{"42","secret","leahcim"};

    @BeforeClass
    public static void setUp(){
        setUpTests();

        u1 = new SubscribedUser(3,userNames[0], passwords[0]);
        u2 = new SubscribedUser(4,userNames[1], passwords[1]);
        u3 = new SubscribedUser(5,userNames[2], passwords[2]);

        Guest setUpU1Guest = userBridge.visit();
        Guest setUpU2Guest = userBridge.visit();
        Guest setUpU3Guest = userBridge.visit();

        userBridge.register(setUpU1Guest,new RegistrationInfo(userNames[0],passwords[0]));
        userBridge.register(setUpU2Guest,new RegistrationInfo(userNames[1],passwords[1]));
        userBridge.register(setUpU3Guest,new RegistrationInfo(userNames[2],userNames[2]));
    }

    @Test
    public void testExitOldUser() {
        Guest newGuest = userBridge.visit();
        SubscribedUser u = userBridge.login(newGuest,userNames[0], passwords[0]);
        boolean res = userBridge.exit(u);

        assertTrue(res);
    }

    @Test
    public void testExitNewUserWithItems() {
        String testUserName = "newRegistered";
        String testPassword = "testExit";

        Guest newGuest = userBridge.visit();
        SubscribedUser u = userBridge.register(newGuest,new RegistrationInfo(testUserName, testPassword));
        userBridge.addProductToCart(u.ID,shops[castro_ID].ID,2,10);

        Guest newG = userBridge.logout(u);

        u = userBridge.login(newG,testUserName, testPassword);
        ShoppingCart userCart = userBridge.checkCart(u.ID);
        assertEquals(1,userCart.numOfProductsInCart());

        int boughtFrom = userCart.getShop(0);
        int product = userCart.getProduct(0);
        int quantity = userCart.getQuantity(0);

        assertEquals(1,boughtFrom);
        assertEquals(2,product);
        assertEquals(10,quantity);

        boolean exitResult = userBridge.exit(u);
        assertTrue(exitResult);

    }

    @Test
    public void testEnterNewUserSuccess() {
        Guest newGuest = userBridge.visit();
        SubscribedUser u = userBridge.register(newGuest,new RegistrationInfo("newName","newPassword"));

        assertNotNull(u);
        assertEquals("newName",u.password);
        assertEquals("newPassword",u.username);

        boolean exitResult = userBridge.exit(u);
        assertTrue(exitResult);
    }

    @Test
    public void testEnterNewUserFailure() {
        Guest newGuest = userBridge.visit();
        SubscribedUser u = userBridge.register(newGuest,new RegistrationInfo(userNames[0], passwords[0]));
        assertNull(u);

        boolean exitResult = userBridge.exit(u);
        assertTrue(exitResult);
    }

    @Test
    public void testOpenShopSuccess(){
        Guest newGuest = userBridge.visit();
        SubscribedUser u = userBridge.login(newGuest,userNames[2], passwords[2]);
        Shop s = userBridge.openShop(u.ID,"supersal","food");

        assertNotNull(s);
        assertEquals("supersal",s.name);
        assertEquals("food",s.category);

        Appointment appointment = u.getRole(s.ID);
        assertEquals("Founder",appointment.role);

        boolean exitResult = userBridge.exit(u);
        assertTrue(exitResult);
    }

    @Test
    public void testOpenShopFailure(){
        Guest newGuest = userBridge.visit();
        SubscribedUser u = userBridge.login(newGuest,userNames[0], passwords[0]);
        Shop s = userBridge.openShop(u.ID,"castro","fashion");

        assertNull(s);

        Appointment appointment = u.getRole(shops[castro_ID].ID);
        assertNull(appointment);

        boolean exitResult = userBridge.exit(u);
        assertTrue(exitResult);
    }

    @Test
    public void testAddProductToShopByFounderSuccess(){
        Guest newGuest = userBridge.visit();
        SubscribedUser u = userBridge.login(newGuest,castroFounder.username,castroFounder.password);

        Appointment appointment = u.getRole(shops[castro_ID].ID);
        assertEquals("Founder",appointment.role);
        Product newproduct = new Product("belt",4.2,"china");

        userBridge.addProductToShop(u.ID,shops[castro_ID].ID,newproduct,222,12,67.5);
        ProductInShop pis  = userBridge.searchProductInShop(222,shops[castro_ID]);

        assertNotNull(pis);
        assertEquals(222,pis.ID);
        assertEquals(12,pis.quantity);
        assertEquals(67.5, pis.price, 0.0);

        boolean exitResult = userBridge.exit(u);
        assertTrue(exitResult);
    }

    @Test
    public void testAddProductToShopByFounderFailure(){
        Guest newGuest = userBridge.visit();
        SubscribedUser u = userBridge.login(newGuest,castroFounder.username,castroFounder.password);

        Appointment appointment = u.getRole(shops[castro_ID].ID);
        assertEquals("Founder",appointment.role);
        Product newproduct = new Product("belt",4.2,"china");

        userBridge.addProductToShop(u.ID,shops[castro_ID].ID,newproduct,222,-12,67.5);
        ProductInShop pis  = userBridge.searchProductInShop(222,shops[castro_ID]);

        assertNull(pis);

        boolean exitResult = userBridge.exit(u);
        assertTrue(exitResult);
    }

    @Test
    public void testDeleteProductFromShopByManagerSuccess(){
        Guest newGuest = userBridge.visit();
        SubscribedUser u = userBridge.login(newGuest,castroFounder.username,castroFounder.password);

        Appointment appointment = u.getRole(shops[MegaSport_ID].ID);
        assertEquals("Manager",appointment.role);

        boolean deleted = userBridge.deleteProductFromShop(u.ID,shops[MegaSport_ID].ID,13);
        assertTrue(deleted);

        ProductInShop pis  = userBridge.searchProductInShop(13,shops[MegaSport_ID]);
        assertNull(pis);

        boolean exitResult = userBridge.exit(u);
        assertTrue(exitResult);
    }

    @Test
    public void testDeleteProductFromShopByManagerFailureBadID(){
        Guest newGuest = userBridge.visit();
        SubscribedUser u = userBridge.login(newGuest,castroFounder.username,castroFounder.password);

        Appointment appointment = u.getRole(shops[MegaSport_ID].ID);
        assertEquals("Manager",appointment.role);

        boolean deleted = userBridge.deleteProductFromShop(u.ID,shops[MegaSport_ID].ID,5);
        assertFalse(deleted);

        ProductInShop pis  = userBridge.searchProductInShop(5,shops[MegaSport_ID]);
        assertNull(pis);

        boolean exitResult = userBridge.exit(u);
        assertTrue(exitResult);
    }

    @Test
    public void testUpdateProductByFounderSuccess(){
        Guest newGuest = userBridge.visit();
        SubscribedUser u = userBridge.login(newGuest,castroFounder.username,castroFounder.password);

        Appointment appointment = u.getRole(shops[castro_ID].ID);
        assertEquals("Founder",appointment.role);

        boolean updated = userBridge.updateProduct(u.ID,shops[castro_ID].ID,45,45,310,55.5);
        assertFalse(updated);

        ProductInShop pis  = userBridge.searchProductInShop(45,shops[castro_ID]);
        assertNotNull(pis);
        assertEquals(45,pis.ID);
        assertEquals(310,pis.quantity);
        assertEquals(55.5,pis.price,0);

        boolean exitResult = userBridge.exit(u);
        assertTrue(exitResult);
    }

    @Test
    public void testUpdateProductByManagerFailureBadID(){
        Guest newGuest = userBridge.visit();
        SubscribedUser u = userBridge.login(newGuest,castroFounder.username,castroFounder.password);

        Appointment appointment = u.getRole(shops[MegaSport_ID].ID);
        assertEquals("Manager",appointment.role);

        boolean updated = userBridge.updateProduct(u.ID,shops[MegaSport_ID].ID,2,4,50,77.99);
        assertFalse(updated);

        ProductInShop pis  = userBridge.searchProductInShop(2,shops[MegaSport_ID]);
        assertNull(pis);

        boolean exitResult = userBridge.exit(u);
        assertTrue(exitResult);
    }

    @Test
    public void testUpdateProductByManagerFailureBadPrice(){
        Guest newGuest = userBridge.visit();
        SubscribedUser u = userBridge.login(newGuest,castroFounder.username,castroFounder.password);

        Appointment appointment = u.getRole(shops[MegaSport_ID].ID);
        assertEquals("Manager",appointment.role);

        boolean updated = userBridge.updateProduct(u.ID,shops[MegaSport_ID].ID,45,45,50,-3);
        assertFalse(updated);

        ProductInShop pis  = userBridge.searchProductInShop(2,shops[MegaSport_ID]);
        assertNull(pis);

        boolean exitResult = userBridge.exit(u);
        assertTrue(exitResult);
    }

    @Test
    public void testAppointShopOwnerSuccess(){
        Guest newGuest = userBridge.visit();
        SubscribedUser u = userBridge.login(newGuest,castroFounder.username,castroFounder.password);

        boolean result = userBridge.appointOwner(shops[castro_ID].ID,castroFounder.ID,u1.ID);
        assertTrue(result);

        Appointment role = u1.getRole(castro_ID);
        assertEquals("Owner",role.getRole());
        assertEquals(castroFounder.ID,role.getAppointer());

        boolean exitResult = userBridge.exit(u);
        assertTrue(exitResult);
    }

    @Test
    public void testAppointShopOwnerByOwnerSuccess(){
        Guest newGuest = userBridge.visit();
        SubscribedUser u = userBridge.login(newGuest,MegaSportFounder.username,MegaSportFounder.password);

        boolean result = userBridge.appointOwner(shops[ACE_ID].ID,MegaSportFounder.ID,u1.ID);
        assertTrue(result);

        Appointment role = u1.getRole(castro_ID);
        assertEquals("Owner",role.getRole());
        assertEquals(castroFounder.ID,role.getAppointer());

        boolean exitResult = userBridge.exit(u);
        assertTrue(exitResult);
    }

    @Test
    public void testAppointShopOwnerFailureDoubleAppointment(){
        Guest newGuest = userBridge.visit();
        SubscribedUser u = userBridge.login(newGuest,castroFounder.username,castroFounder.password);

        userBridge.appointOwner(shops[castro_ID].ID,castroFounder.ID,u1.ID);
        boolean result = userBridge.appointOwner(shops[castro_ID].ID,castroFounder.ID,u1.ID);
        assertFalse(result);

        boolean exitResult = userBridge.exit(u);
        assertTrue(exitResult);
    }

    @Test
    public void testAppointShopOwnerFailureCircularAppointment(){
        Guest newGuest = userBridge.visit();
        SubscribedUser u = userBridge.login(newGuest,castroFounder.username,castroFounder.password);

        userBridge.appointOwner(shops[castro_ID].ID,castroFounder.ID,u1.ID);
        userBridge.appointOwner(shops[castro_ID].ID,u1.ID,u2.ID);
        userBridge.appointOwner(shops[castro_ID].ID,u2.ID,u3.ID);
        boolean result = userBridge.appointOwner(shops[castro_ID].ID,u3.ID,u1.ID);

        assertFalse(result);

        boolean exitResult = userBridge.exit(u);
        assertTrue(exitResult);
    }

    @Test
    public void testAppointShopOwnerFailureAppointmentByGuest(){
        Guest newGuest = userBridge.visit();

        boolean result = userBridge.appointOwner(shops[castro_ID].ID,newGuest.ID,u1.ID);
        assertFalse(result);

        boolean exitResult = userBridge.exit(newGuest);
        assertTrue(exitResult);
    }

    @Test
    public void testAppointShopOwnerFailureAppointmentByManagerNotOwner(){
        Guest newGuest = userBridge.visit();
        SubscribedUser u = userBridge.login(newGuest,castroFounder.username,castroFounder.password);

        boolean result = userBridge.appointOwner(shops[MegaSport_ID].ID,u.ID,u1.ID);
        assertFalse(result);

        boolean exitResult = userBridge.exit(newGuest);
        assertTrue(exitResult);
    }

    @Test
    public void testAppointShopManagerSuccess(){
        Guest newGuest = userBridge.visit();
        SubscribedUser u = userBridge.login(newGuest,castroFounder.username,castroFounder.password);

        boolean result = userBridge.appointManager(shops[castro_ID].ID,u.ID,u1.ID);
        assertTrue(result);

        Appointment role = u1.getRole(castro_ID);
        assertEquals("Manager",role.getRole());
        assertEquals(castroFounder.ID,role.getAppointer());

        boolean exitResult = userBridge.exit(newGuest);
        assertTrue(exitResult);
    }

    @Test
    public void testAppointShopManagerFailureAlreadyManager(){
        Guest newGuest = userBridge.visit();
        SubscribedUser u = userBridge.login(newGuest,castroFounder.username,castroFounder.password);

        boolean result = userBridge.appointManager(shops[castro_ID].ID,u.ID,u1.ID);
        assertTrue(result);

        Appointment role = u1.getRole(castro_ID);
        assertEquals("Manager",role.getRole());
        assertEquals(castroFounder.ID,role.getAppointer());

        result = userBridge.appointManager(shops[castro_ID].ID,u.ID,u1.ID);
        assertFalse(result);

        boolean exitResult = userBridge.exit(newGuest);
        assertTrue(exitResult);
    }

    @Test
    public void testAppointShopManagerFailureNotOwner(){
        Guest newGuest = userBridge.visit();
        SubscribedUser u = userBridge.login(newGuest,castroFounder.username,castroFounder.password);

        boolean result = userBridge.appointManager(shops[MegaSport_ID].ID,u.ID,u1.ID);
        assertFalse(result);

        boolean exitResult = userBridge.exit(newGuest);
        assertTrue(exitResult);
    }

    @Test
    public void testCloseShopSuccess(){
        Guest newGuest = userBridge.visit();
        SubscribedUser u = userBridge.login(newGuest,castroFounder.username,castroFounder.password);
        ShopFilter shopFilterName = (s) -> s.name.equals("castro");

        boolean result = userBridge.closeShop(shops[castro_ID].ID,u.ID);
        assertTrue(result);

        List<Shop> searchResult = userBridge.getShopsInfo(shopFilterName);
        assertEquals(0,searchResult.size());

        List<String> castroFounderNotifications = castroFounder.notifications;
        assertEquals(1,castroFounderNotifications.size());
        assertEquals("castro closed",castroFounderNotifications.get(0));

        boolean exitResult = userBridge.exit(newGuest);
        assertTrue(exitResult);
    }

    @Test
    public void testAddManagerPermissionsSuccess(){
        Guest newGuest = userBridge.visit();
        SubscribedUser u = userBridge.login(newGuest,MegaSportFounder.username,MegaSportFounder.password);

        boolean result = userBridge.addPermisionToManager(shops[MegaSport_ID].ID,u.ID,castroFounder.ID,"change policies");
        assertTrue(result);

        List<String> permissions = castroFounder.getPermissions(shops[MegaSport_ID].ID);
        assertTrue(permissions.contains("change policies"));

        boolean exitResult = userBridge.exit(newGuest);
        assertTrue(exitResult);
    }

    @Test
    public void testAddManagerPermissionsFailureNotAppointer(){
        Guest newGuest = userBridge.visit();
        SubscribedUser u = userBridge.login(newGuest,MegaSportFounder.username,MegaSportFounder.password);

        boolean result = userBridge.appointManager(shops[ACE_ID].ID,u.ID,u1.ID);
        assertTrue(result);

        result = userBridge.addPermisionToManager(shops[ACE_ID].ID,u.ID,castroFounder.ID,"change policies");
        assertFalse(result);

        List<String> permissions = u1.getPermissions(shops[ACE_ID].ID);
        assertFalse(permissions.contains("change policies"));

        boolean exitResult = userBridge.exit(newGuest);
        assertTrue(exitResult);
    }

    @Override
    public User enter() {
        return null;
    }
}
