package BusinessLayer.Users;

import BusinessLayer.Products.Product;
import BusinessLayer.Products.Users.Guest;
import BusinessLayer.Products.Users.SubscribedUser;
import BusinessLayer.Products.Users.User;
import BusinessLayer.Shops.Shop;
import BusinessLayer.System.PaymentMethod;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserTest {
    Shop s1;
    User u1;
    Product p1;
    private SubscribedUser founder = createFounder();

    @Before
    public void setUp() {
        s1 = createShopWithProduct();
        u1 = createUser();
        assertSame("Yuval", u1.getUserName());
        u1.saveProducts(s1.getId(), p1.getID(), quantity);
        assertSame(u1.getProducts(s1.getId()).get(p1.getID()), quantity);
    }

    @Test
    public void testSaveProductsSuccess() {
        assertEquals(quantity, (int) u1.getProducts(s1.getId()).get(p1.getID()));
    }

    @Test
    public void testSaveProductsFail() {
        //saving a second time
        try {
            assertFalse(u1.saveProducts(s1.getId(), p1.getID(), quantity));
        }
        catch(Exception ignored) {
            assertEquals(quantity, (int) u1.getProducts(s1.getId()).get(p1.getID()));
        }
    }

    @Test
    public void testRemoveProductSuccess() {
        assertTrue(u1.removeProduct(s1.getId(),p1.getID()));
        assertFalse(u1.getProducts(s1.getId()).containsKey(p1.getID()));
    }

    @Test
    public void testRemoveProductFailNonExistentItem() {
        try {
            assertFalse(u1.removeProduct(s1.getId(), 3));
            fail("can't remove not exist item");
        }
        catch (Exception e)
        {
            //checks the product was not removed
            assertEquals(1, u1.getBasket(s1.getId()).getProducts().size());
        }
    }

    @Test
    public void testRemoveProductFailNonExistentShop() {
        assertFalse(u1.removeProduct(2, p1.getID()));
        assertEquals(1, u1.getBasket(s1.getId()).getProducts().size());
    }

    @Test
    public void testEditProductQuantitySuccess() {
        assertTrue(u1.editProductQuantity(s1.getId(), p1.getID(), newQuantity));
        assertEquals(newQuantity, (int) u1.getProducts(s1.getId()).get(p1.getID()));
    }

    @Test
    public void testEditProductQuantityFail() {
        try{
            assertFalse(u1.editProductQuantity(s1.getId(),3,newQuantity));
            fail("can't edit not exist item");
        }
        catch (Exception ignored)
        {
            //checks the quantity was not changed from var "quality" to "newQuality"
            assertNotEquals(newQuantity, (int) u1.getBasket(s1.getId()).getProducts().get(p1.getID()));
        }
    }

    @Test
    public void testEditProductQuantityFailNonExistentShop() {
        assertFalse(u1.editProductQuantity(2,p1.getID(),newQuantity));
        //checks the quantity was not changed from var "quality" to "newQuality"
        assertNotEquals(newQuantity, (int) u1.getBasket(s1.getId()).getProducts().get(p1.getID()));
    }

    @Test
    public void testUpdatePaymentMethod() {
        assertNull(u1.getMethod());
        PaymentMethod method = new PaymentMethod("4580123456789012", 123, 2, 2036);
        u1.updatePaymentMethod(method);
        assertEquals(method, u1.getMethod());
    }

    private final int newQuantity = 55;

    private final int quantity = 100;

    private Shop createShopWithProduct() throws IllegalStateException {
        Shop s1 = createShop();
        p1 = createProduct();
        s1.addProduct(p1);
        return s1;
    }

    private Shop createShop() {
        return new Shop(100, "shop", founder);
    }

    private User createUser() {
        return new Guest("Yuval");
    }

    private Product createProduct() {
        return new Product(1, "a", 5, quantity);
    }

    private SubscribedUser createFounder() {
        return new SubscribedUser("Founder Guy","pass12");
    }
}