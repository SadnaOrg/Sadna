package AcceptanceTests.Tests;

import AcceptanceTests.Bridge.SubscribedUserBridge;
import AcceptanceTests.Bridge.SubscribedUserProxy;
import AcceptanceTests.Bridge.UserProxy;
import AcceptanceTests.Threads.FounderAppointManager;
import AcceptanceTests.Threads.FounderDeletesProduct;
import AcceptanceTests.Threads.OwnerAppointManager;
import AcceptanceTests.Threads.UserBuysProduct;

import AcceptanceTests.DataObjects.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
// TODO: add tear down after tests - look at tests and cancel side effects (permissions, shop opening, appointments, new products)
// TODO: refactor login of users
// TODO: fix concurrent tests - buy & delete , double appointment

// u1 is owner of castro!
// u2 is owner of castro!
// owner appointments need flags!
// close supersal after openShop!
// look at double owner appointment!
// look at owner / manager appointment for teardown!
public class SubscribedUserTests extends UserTests {
    private static final SubscribedUserBridge subscribedUserBridge = new SubscribedUserProxy((UserProxy) UserTests.getUserBridge());

    private static Shop supersal;

    private final static String[] userNames = new String[]{"testUser3","buyer100","michael","superfounder"};
    private final static String[] passwords = new String[]{"42","secret","leahcim","superpassword"};

    private final static String[] defaultFounderPermissions = new String[]{"inventory management","change policies","set consistency constraints",
    "appoint owner","remove owner","appoint manager","remove manager","change managers' permissions","close shop","reopen shop",
    "get appointments in shop","get purchase history","respond to customers questions"};

    private final static String[] defaultOwnerPermissions = new String[]{"inventory management","change policies","set consistency constraints",
            "appoint owner","remove owner","appoint manager","remove manager","change managers' permissions","reopen shop",
            "get appointments in shop","get purchase history","respond to customers questions"};

    private final static String[] defaultManagerPermissions = new String[]{"view information"};

    private static SubscribedUser u1;
    private static SubscribedUser u2;
    private static SubscribedUser u3;
    private static SubscribedUser supersalFounder;

    public static String getU1Name(){
        return u1.name;
    }

    public static String getU2Name(){
        return u2.name;
    }

    public static String getU3Name(){
        return u3.name;
    }

    public static SubscribedUserBridge getUserBridge(){return subscribedUserBridge;}

    @BeforeClass
    public static void setUpUsers(){
        UserTests.setUpTests();
        Guest setUpU1 = subscribedUserBridge.visit();
        Guest setUpU2 = subscribedUserBridge.visit();
        Guest setUpU3 = subscribedUserBridge.visit();
        Guest setUpSupersal = subscribedUserBridge.visit();

        RegistrationInfo u1Reg = new RegistrationInfo(userNames[0], passwords[0]);
        RegistrationInfo u2Reg = new RegistrationInfo(userNames[1],passwords[1]);
        RegistrationInfo u3Reg = new RegistrationInfo(userNames[2],passwords[2]);
        RegistrationInfo supersalReg= new RegistrationInfo(userNames[3], passwords[3]);
        subscribedUserBridge.register(u1Reg);
        subscribedUserBridge.register(u2Reg);
        subscribedUserBridge.register(u3Reg);
        subscribedUserBridge.register(supersalReg);

        u1 = subscribedUserBridge.login(setUpU1.getName(),u1Reg);
        u2 = subscribedUserBridge.login(setUpU2.getName(), u2Reg);
        u3 = subscribedUserBridge.login(setUpU3.getName(), u3Reg);
        supersalFounder = subscribedUserBridge.login(setUpSupersal.name, supersalReg);

        subscribedUserBridge.exit(u1.name);
        subscribedUserBridge.exit(u2.name);
        subscribedUserBridge.exit(u3.name);
        subscribedUserBridge.exit(supersalFounder.name);
    }

    @Test
    public void testExitOldUser() {
        Guest newGuest = subscribedUserBridge.visit();
        u1 = subscribedUserBridge.login(newGuest.name,new RegistrationInfo(u1.name, passwords[0]));
        assertEquals("testUser3",u1.name);
        boolean res = subscribedUserBridge.exit(u1.name);
        assertTrue(res);
    }

    @Test
    public void testExitNewUserWithItems() {
        String testUserName = "newRegistered";
        String testPassword = "testExit";

        Guest newGuest = subscribedUserBridge.visit();
        userBridge.register(new RegistrationInfo(testUserName, testPassword));
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
        subscribedUserBridge.updateCart(u.name,new int[]{2},new int[]{shops[castro_ID].ID},new int[]{0});

        boolean exitResult = subscribedUserBridge.exit(u.name);
        assertTrue(exitResult);

    }

    @Test
    public void testEnterNewUserSuccess() {
        Guest newGuest = subscribedUserBridge.visit();
        boolean res = subscribedUserBridge.register(new RegistrationInfo("newName","newPassword"));
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
        boolean res = subscribedUserBridge.register(new RegistrationInfo(u1.name, passwords[0]));
        assertFalse(res);
        res = subscribedUserBridge.exit(newGuest.name);
        assertTrue(res);
    }

    @Test
    public void testOpenShopSuccess(){
        Guest newGuest = subscribedUserBridge.visit();
        supersalFounder = subscribedUserBridge.login(newGuest.name,new RegistrationInfo(supersalFounder.name,  passwords[3]));
        supersal = subscribedUserBridge.openShop(supersalFounder.name,"supersal","food");

        assertNotNull(supersal);
        assertEquals("supersal",supersal.name);
        assertEquals("food",supersal.desc);

        Appointment appointment = supersalFounder.getRole(supersal.ID);
        assertEquals("Founder",appointment.role);
        // check for appointer

        boolean exitResult = subscribedUserBridge.exit(supersalFounder.name);
        assertTrue(exitResult);
    }

    @Test
    public void testOpenShopFailure(){
        Guest newGuest = subscribedUserBridge.visit();
        u1 = subscribedUserBridge.login(newGuest.name,new RegistrationInfo(userNames[0], passwords[0]));
        Shop s = subscribedUserBridge.openShop(u1.name,"castro","fashion");

        assertNull(s);

        Appointment appointment = u1.getRole(shops[castro_ID].ID);
        assertNull(appointment);

        boolean exitResult = subscribedUserBridge.exit(u1.name);
        assertTrue(exitResult);
    }

    @Test
    public void testAddProductToShopByFounderSuccess(){
        Guest newGuest = subscribedUserBridge.visit();
        castroFounder = subscribedUserBridge.login(newGuest.name,new RegistrationInfo(castroFounder.name,"castro_rocks"));

        Appointment appointment = castroFounder.getRole(shops[castro_ID].ID);
        assertEquals("Founder",appointment.role);
        Product newproduct = new Product("belt","brown","china");

        boolean added = subscribedUserBridge.addProductToShop(castroFounder.name,shops[castro_ID].ID,newproduct,222,12,67.5);
        assertTrue(added);
        ProductInShop pis  = subscribedUserBridge.searchProductInShop(222,shops[castro_ID].ID);

        assertNotNull(pis);
        assertEquals(222,pis.ID);
        assertEquals(12,pis.quantity);
        assertEquals(67.5, pis.price, 0.0);

        boolean exitResult = subscribedUserBridge.exit(castroFounder.name);
        assertTrue(exitResult);
    }

    @Test
    public void testAddProductToShopByFounderFailureBadQuantity(){
        Guest newGuest = subscribedUserBridge.visit();
        castroFounder = subscribedUserBridge.login(newGuest.name,new RegistrationInfo(castroFounder.name,"castro_rocks"));

        Appointment appointment = castroFounder.getRole(shops[castro_ID].ID);
        assertEquals("Founder",appointment.role);
        Product newproduct = new Product("belt","brown","china");

        boolean added = subscribedUserBridge.addProductToShop(castroFounder.name,shops[castro_ID].ID,newproduct,221,-12,67.5);
        assertFalse(added);

        ProductInShop pis  = subscribedUserBridge.searchProductInShop(221,shops[castro_ID].ID);
        assertNull(pis);

        boolean exitResult = subscribedUserBridge.exit(castroFounder.name);
        assertTrue(exitResult);
    }

    @Test
    public void testAddProductToShopByFounderFailureBadPrice(){
        Guest newGuest = subscribedUserBridge.visit();
        castroFounder = subscribedUserBridge.login(newGuest.name,new RegistrationInfo(castroFounder.name,"castro_rocks"));

        Appointment appointment = castroFounder.getRole(shops[castro_ID].ID);
        assertEquals("Founder",appointment.role);
        Product newproduct = new Product("belt","brown","china");

        boolean added = subscribedUserBridge.addProductToShop(castroFounder.name,shops[castro_ID].ID,newproduct,221,12,-12);
        assertFalse(added);

        ProductInShop pis  = subscribedUserBridge.searchProductInShop(222,shops[castro_ID].ID);
        assertNull(pis);

        boolean exitResult = subscribedUserBridge.exit(castroFounder.name);
        assertTrue(exitResult);
    }

    @Test
    public void testAddProductToShopByFounderFailureBadPriceBadQuantity(){
        Guest newGuest = subscribedUserBridge.visit();
        castroFounder = subscribedUserBridge.login(newGuest.name,new RegistrationInfo(castroFounder.name,"castro_rocks"));

        Appointment appointment = castroFounder.getRole(shops[castro_ID].ID);
        assertEquals("Founder",appointment.role);
        Product newproduct = new Product("belt","brown","china");

        boolean added = subscribedUserBridge.addProductToShop(castroFounder.name,shops[castro_ID].ID,newproduct,221,-12,-12);
        assertFalse(added);

        ProductInShop pis  = subscribedUserBridge.searchProductInShop(221,shops[castro_ID].ID);
        assertNull(pis);

        boolean exitResult = subscribedUserBridge.exit(castroFounder.name);
        assertTrue(exitResult);
    }
    // TODO: ADD FLAG FOR TEARDOWN!!!!!
    @Test
    public void testDeleteProductFromShopByOwnerSuccess(){
        testAddProductToShopByFounderSuccess();

        Guest newGuest = subscribedUserBridge.visit();
        ACEFounder = subscribedUserBridge.login(newGuest.name,new RegistrationInfo(ACEFounder.name,"ACE_rocks"));

        Appointment appointment = ACEFounder.getRole(shops[castro_ID].ID);
        assertEquals("Owner",appointment.role);

        boolean deleted = subscribedUserBridge.deleteProductFromShop(ACEFounder.name,shops[castro_ID].ID,222);
        assertTrue(deleted);

        ProductInShop pis  = subscribedUserBridge.searchProductInShop(222,shops[castro_ID].ID);
        assertNull(pis);

        boolean exitResult = subscribedUserBridge.exit(ACEFounder.name);
        assertTrue(exitResult);
    }

    @Test
    public void testDeleteProductFromShopByOwnerFailureBadID(){
        Guest newGuest = subscribedUserBridge.visit();
        ACEFounder = subscribedUserBridge.login(newGuest.name,new RegistrationInfo(ACEFounder.name,"ACE_rocks"));

        Appointment appointment = ACEFounder.getRole(shops[castro_ID].ID);
        assertEquals("Owner",appointment.role);

        boolean deleted = subscribedUserBridge.deleteProductFromShop(ACEFounder.name,shops[castro_ID].ID,5);
        assertFalse(deleted);

        ProductInShop pis  = subscribedUserBridge.searchProductInShop(5,shops[castro_ID].ID);
        assertNull(pis);

        boolean exitResult = subscribedUserBridge.exit(ACEFounder.name);
        assertTrue(exitResult);

    }

    @Test
    public void testUpdateProductIncreasePriceByFounderSuccess(){
        Guest newGuest = subscribedUserBridge.visit();
        castroFounder = subscribedUserBridge.login(newGuest.name,new RegistrationInfo(castroFounder.name,"castro_rocks"));

        Appointment appointment = castroFounder.getRole(shops[castro_ID].ID);
        assertEquals("Founder",appointment.role);

        boolean updated =subscribedUserBridge.updateProductPrice(castroFounder.name,shops[castro_ID].ID,45,120);
        assertTrue(updated);

        ProductInShop pis  = subscribedUserBridge.searchProductInShop(45,shops[castro_ID].ID);
        assertNotNull(pis);
        assertEquals(45,pis.ID);
        assertEquals(40,pis.quantity);
        assertEquals(120,pis.price,0);

        boolean exitResult = subscribedUserBridge.exit(castroFounder.name);
        assertTrue(exitResult);

        testUpdateKeepProductPriceByFounderSuccess();
    }

    @Test
    public void testUpdateProductDecreasePriceByFounderSuccess(){
        Guest newGuest = subscribedUserBridge.visit();
        castroFounder = subscribedUserBridge.login(newGuest.name,new RegistrationInfo(castroFounder.name,"castro_rocks"));

        Appointment appointment = castroFounder.getRole(shops[castro_ID].ID);
        assertEquals("Founder",appointment.role);

        boolean updated =subscribedUserBridge.updateProductPrice(castroFounder.name,shops[castro_ID].ID,45,12.5);
        assertTrue(updated);

        ProductInShop pis  = subscribedUserBridge.searchProductInShop(45,shops[castro_ID].ID);
        assertNotNull(pis);
        assertEquals(45,pis.ID);
        assertEquals(40,pis.quantity);
        assertEquals(12.5,pis.price,0);

        boolean exitResult = subscribedUserBridge.exit(castroFounder.name);
        assertTrue(exitResult);

        testUpdateKeepProductPriceByFounderSuccess();
    }

    @Test
    public void testUpdateKeepProductPriceByFounderSuccess(){
        Guest newGuest = subscribedUserBridge.visit();
        castroFounder = subscribedUserBridge.login(newGuest.name,new RegistrationInfo(castroFounder.name,"castro_rocks"));

        Appointment appointment = castroFounder.getRole(shops[castro_ID].ID);
        assertEquals("Founder",appointment.role);

        boolean updated =subscribedUserBridge.updateProductPrice(castroFounder.name,shops[castro_ID].ID,45,50);
        assertTrue(updated);

        ProductInShop pis  = subscribedUserBridge.searchProductInShop(45,shops[castro_ID].ID);
        assertNotNull(pis);
        assertEquals(45,pis.ID);
        assertEquals(40,pis.quantity);
        assertEquals(50,pis.price,0);

        boolean exitResult = subscribedUserBridge.exit(castroFounder.name);
        assertTrue(exitResult);
    }

    @Test
    public void testUpdateProductByOwnerFailureBadID(){
        Guest newGuest = subscribedUserBridge.visit();
        MegaSportFounder = subscribedUserBridge.login(newGuest.name,new RegistrationInfo(MegaSportFounder.name,"MegaSport_rocks"));

        Appointment appointment = MegaSportFounder.getRole(shops[ACE_ID].ID);
        assertEquals("Owner",appointment.role);

        boolean updated = subscribedUserBridge.updateProductName(MegaSportFounder.name,shops[ACE_ID].ID,3,"fail");
        assertFalse(updated);

        ProductInShop pis  = subscribedUserBridge.searchProductInShop(3,shops[ACE_ID].ID);
        assertNull(pis);

        boolean exitResult = subscribedUserBridge.exit(MegaSportFounder.name);
        assertTrue(exitResult);
    }

    @Test
    public void testUpdateProductByOwnerFailureBadPrice(){
        Guest newGuest = subscribedUserBridge.visit();
        MegaSportFounder = subscribedUserBridge.login(newGuest.name,new RegistrationInfo(MegaSportFounder.name,"MegaSport_rocks"));

        Appointment appointment = MegaSportFounder.getRole(shops[ACE_ID].ID);
        assertEquals("Owner",appointment.role);

        boolean updated =subscribedUserBridge.updateProductPrice(MegaSportFounder.name,shops[ACE_ID].ID,1,-50);
        assertFalse(updated);

        ProductInShop pis  = subscribedUserBridge.searchProductInShop(45,shops[MegaSport_ID].ID);
        assertNotNull(pis);
        assertEquals(pis.ID,1);
        assertEquals(pis.price,40);

        boolean exitResult = subscribedUserBridge.exit(MegaSportFounder.name);
        assertTrue(exitResult);
    }

    @Test
    public void testUpdateProductByOwnerFailureBadPriceBadID(){
        Guest newGuest = subscribedUserBridge.visit();
        MegaSportFounder = subscribedUserBridge.login(newGuest.name,new RegistrationInfo(MegaSportFounder.name,"MegaSport_rocks"));

        Appointment appointment = MegaSportFounder.getRole(shops[ACE_ID].ID);
        assertEquals("Owner",appointment.role);

        boolean updated =subscribedUserBridge.updateProductPrice(MegaSportFounder.name,shops[ACE_ID].ID,11,-50);
        assertFalse(updated);

        ProductInShop pis  = subscribedUserBridge.searchProductInShop(11,shops[ACE_ID].ID);
        assertNull(pis);

        boolean exitResult = subscribedUserBridge.exit(MegaSportFounder.name);
        assertTrue(exitResult);
    }

    @Test
    public void testAppointShopOwnerSuccess(){
        Guest newGuest = subscribedUserBridge.visit();
        castroFounder = subscribedUserBridge.login(newGuest.name,new RegistrationInfo(castroFounder.name,"castro_rocks"));

        boolean result = subscribedUserBridge.appointOwner(shops[castro_ID].ID,castroFounder.name,u1.name);
        assertTrue(result);

        boolean exitResult = subscribedUserBridge.exit(castroFounder.name);
        assertTrue(exitResult);

        newGuest = subscribedUserBridge.visit();
        u1 = subscribedUserBridge.login(newGuest.name, new RegistrationInfo(userNames[0], passwords[0]));

        Appointment role = u1.getRole(castro_ID);
        assertNotNull(role);
        assertEquals("Owner",role.getRole());
        // assertEquals(castroFounder.name,role.getAppointer());

        exitResult = subscribedUserBridge.exit(u1.name);
        assertTrue(exitResult);
    }

    @Test
    public void testAppointShopOwnerByOwnerSuccess(){
        testAppointShopOwnerSuccess();
        Guest newGuest = subscribedUserBridge.visit();
        u1 = subscribedUserBridge.login(newGuest.name,new RegistrationInfo(userNames[0], passwords[0]));

        boolean result = subscribedUserBridge.appointOwner(shops[castro_ID].ID,u1.name,u2.name);
        assertTrue(result);

        boolean exitResult = subscribedUserBridge.exit(u1.name);
        assertTrue(exitResult);

        newGuest = subscribedUserBridge.visit();
        u2 = subscribedUserBridge.login(newGuest.name, new RegistrationInfo(userNames[1], passwords[1]));
        Appointment role = u2.getRole(castro_ID);
        assertNotNull(role);
        assertEquals("Owner",role.getRole());
        //assertEquals(u1.name,role.getAppointer());

        exitResult = subscribedUserBridge.exit(u2.name);
        assertTrue(exitResult);
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

        newGuest = subscribedUserBridge.visit();
        u1 = subscribedUserBridge.login(newGuest.name,new RegistrationInfo(passwords[0],userNames[0]));

        Appointment role = u1.getRole(castro_ID);
        assertNull(role);

        exitResult = subscribedUserBridge.exit(u1.name);
        assertTrue(exitResult);
    }

    @Test
    public void testAppointShopOwnerFailureAppointmentByManagerNotOwner(){
        Guest newGuest = subscribedUserBridge.visit();
        MegaSportFounder = subscribedUserBridge.login(newGuest.name,new RegistrationInfo(MegaSportFounder.name,"MegaSport_rocks"));

        boolean result = subscribedUserBridge.appointOwner(shops[castro_ID].ID,MegaSportFounder.name,u1.name);
        assertFalse(result);

        boolean exitResult = subscribedUserBridge.exit(MegaSportFounder.name);
        assertTrue(exitResult);

        newGuest = subscribedUserBridge.visit();
        u1 = subscribedUserBridge.login(newGuest.name, new RegistrationInfo(userNames[0], passwords[0]));

        Appointment role = u1.getRole(castro_ID);
        assertNull(role);

        exitResult = subscribedUserBridge.exit(u1.name);
        assertTrue(exitResult);
    }

    @Test
    public void testAppointShopManagerSuccess(){
        Guest newGuest = subscribedUserBridge.visit();
        castroFounder = subscribedUserBridge.login(newGuest.name,new RegistrationInfo(castroFounder.name,"castro_rocks"));

        boolean result = subscribedUserBridge.appointManager(shops[castro_ID].ID,castroFounder.name,u1.name);
        assertTrue(result);

        boolean exitResult = subscribedUserBridge.exit(castroFounder.name);
        assertTrue(exitResult);

        newGuest = subscribedUserBridge.visit();
        u1 = subscribedUserBridge.login(newGuest.name, new RegistrationInfo(userNames[0], passwords[0]));

        Appointment role = u1.getRole(castro_ID);
        assertNotNull(role);
        assertEquals("Manager",role.getRole());
        // assertEquals(castroFounder.name,role.getAppointer());
        exitResult = subscribedUserBridge.exit(u1.name);
        assertTrue(exitResult);

    }

    @Test
    public void testConcurrentManagerAppointment() throws InterruptedException {
        testAppointShopManagerSuccess(); // u1 is manager at castro
        Guest g = subscribedUserBridge.visit();
        u1 = subscribedUserBridge.login(g.ID,new RegistrationInfo(u1.username, u1.password));

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
        try{
            testAppointShopManagerSuccess();
            testAppointShopManagerSuccess();
            fail("double manager appointment");
        } catch (Exception ignored) {

        }
    }

    @Test
    public void testAppointShopManagerFailureNotOwner(){
        Guest newGuest = subscribedUserBridge.visit();
        castroFounder = subscribedUserBridge.login(newGuest.name,new RegistrationInfo(castroFounder.name,"castro_rocks"));

        boolean result = subscribedUserBridge.appointManager(shops[MegaSport_ID].ID,castroFounder.name,u1.name);
        assertFalse(result);

        boolean exitResult = subscribedUserBridge.exit(castroFounder.name);
        assertTrue(exitResult);

        newGuest = subscribedUserBridge.visit();
        u1 = subscribedUserBridge.login(newGuest.name, new RegistrationInfo(userNames[0],passwords[0]));

        Appointment role = u1.getRole(castro_ID);
        assertNull(role);

        exitResult = subscribedUserBridge.exit(u1.name);
        assertTrue(exitResult);
    }

    @Test
    public void testCloseShopSuccess(){
        testOpenShopSuccess(); // open supersal

        Guest newGuest = subscribedUserBridge.visit();
        supersalFounder = subscribedUserBridge.login(newGuest.name,new RegistrationInfo(supersalFounder.name, passwords[3]));
        ShopFilter shopFilterName = (s) -> s.name.equals("supersal");

        boolean result = subscribedUserBridge.closeShop(supersal.ID,supersalFounder.name);
        assertTrue(result);

        List<Shop> searchResult = subscribedUserBridge.getShopsInfo(shopFilterName);
        assertEquals(0,searchResult.size());

        List<ProductInShop> productInShops = subscribedUserBridge.searchShopProducts(supersal.ID);
        assertNull(productInShops);

        //List<String> supersalFounderNotifications = u2.notifications;
        //assertEquals(1,supersalFounderNotifications.size());
        //assertEquals("supersal closed",supersalFounderNotifications.get(0));

        boolean exitResult = subscribedUserBridge.exit(supersalFounder.name);
        assertTrue(exitResult);
    }

    @Test
    public void testAddManagerPermissionsSuccess(){
        testAppointShopManagerSuccess(); // u1 - manager at castro
        Guest newGuest = subscribedUserBridge.visit();
        castroFounder = subscribedUserBridge.login(newGuest.name,new RegistrationInfo(castroFounder.name,"castro_rocks"));

        boolean result = subscribedUserBridge.addManagerPermission(shops[castro_ID].ID,castroFounder.name,u1.name,"change policies");
        assertTrue(result);

        boolean exitResult = subscribedUserBridge.exit(castroFounder.name);
        assertTrue(exitResult);

        newGuest = subscribedUserBridge.visit();
        u1 = subscribedUserBridge.login(newGuest.name, new RegistrationInfo(userNames[0], passwords[0]));

        List<String> permissions = u1.getPermissions(shops[castro_ID].ID);
        assertTrue(permissions.contains("change policies"));
        assertTrue(permissions.contains("view information"));
        assertEquals(2,permissions.size());

        exitResult = subscribedUserBridge.exit(u1.name);
        assertTrue(exitResult);
    }

    @Test
    public void testAddManagerPermissionsFailureNotAppointer(){
        testAppointShopManagerSuccess(); // u1 - manager at castro
        Guest newGuest = subscribedUserBridge.visit();
        ACEFounder = subscribedUserBridge.login(newGuest.name,new RegistrationInfo(ACEFounder.name,"ACE_rocks"));

        boolean result = subscribedUserBridge.addManagerPermission(shops[castro_ID].ID,ACEFounder.name,u1.name,"change policies");
        assertFalse(result);

        boolean exitResult = subscribedUserBridge.exit(ACEFounder.name);
        assertTrue(exitResult);

        newGuest = subscribedUserBridge.visit();
        u1 = subscribedUserBridge.login(newGuest.name, new RegistrationInfo(userNames[0], passwords[0]));
        List<String> permission = u1.getPermissions(castro_ID);

        assertFalse(permission.contains("change policies"));

        exitResult = subscribedUserBridge.exit(u1.name);
        assertTrue(exitResult);
    }

    @Test
    public void testAddOwnerPermissionsSuccess(){
        Guest newGuest = subscribedUserBridge.visit();
        ACEFounder = subscribedUserBridge.login(newGuest.name,new RegistrationInfo(ACEFounder.name,"ACE_rocks"));

        boolean result = subscribedUserBridge.addOwnerPermission(shops[ACE_ID].ID,ACEFounder.name,MegaSportFounder.name,"close shop");
        assertTrue(result);

        boolean exitResult = subscribedUserBridge.exit(ACEFounder.name);
        assertTrue(exitResult);

        newGuest = subscribedUserBridge.visit();
        MegaSportFounder = subscribedUserBridge.login(newGuest.name, new RegistrationInfo(MegaSportFounder.name, "MegaSport_rocks"));

        List<String> permissions = MegaSportFounder.getPermissions(shops[MegaSport_ID].ID);
        assertTrue(permissions.contains("close shop"));
        assertEquals(defaultFounderPermissions.length, permissions.size());
    }

    @Test
    public void testAddOwnerPermissionsFailureNotAppointer(){
        testAppointShopOwnerSuccess(); // u1 - owner at castro
        Guest newGuest = subscribedUserBridge.visit();
        ACEFounder = subscribedUserBridge.login(newGuest.name,new RegistrationInfo(ACEFounder.name,"ACE_rocks"));

        boolean result = subscribedUserBridge.addOwnerPermission(shops[castro_ID].ID,ACEFounder.name,u1.name,"close shop");
        assertFalse(result);

        boolean exitResult = subscribedUserBridge.exit(ACEFounder.name);
        assertTrue(exitResult);

        newGuest = subscribedUserBridge.visit();
        u1 = subscribedUserBridge.login(newGuest.name, new RegistrationInfo(userNames[0], passwords[0]));

        List<String> permissions = u1.getPermissions(shops[castro_ID].ID);
        for (String permission:
             defaultOwnerPermissions) {
            assertTrue(permissions.contains(permission));
        }
        assertEquals(defaultOwnerPermissions.length, permissions.size());

        exitResult = subscribedUserBridge.exit(u1.name);
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

        Shop s = subscribedUserBridge.openShop(u.name,"new Shop","gaming");
        subscribedUserBridge.addProductToShop(u.name,s.ID,new Product("x-box","microsoft"),4,4.3,10,2000);

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
        ShoppingCart cart = subscribedUserBridge.checkCart(u2.name);
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

        boolean exit = subscribedUserBridge.exit(u.name);
        assertTrue(exit);

        exit = subscribedUserBridge.exit(u2.name);
        assertTrue(exit);
    }

    public User enter() {
        Guest g = subscribedUserBridge.visit();
        return subscribedUserBridge.login(g.name,new RegistrationInfo( userNames[0].concat("0"),passwords[0].concat("0")));
    }
}