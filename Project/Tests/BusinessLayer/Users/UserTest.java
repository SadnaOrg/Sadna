package BusinessLayer.Users;

import BusinessLayer.Products.Product;
import BusinessLayer.Shops.Shop;
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

    }

    @Test
    public void testSaveProducts() {
        assertTrue(u1.saveProducts(s1.getId(), p1.getID(), 10));
        assertEquals(10, (int) u1.getShoppingCart().get(s1.getId()).getProducts().get(p1.getID()));

    }

    @Test
    public void testRemoveproduct()
    {
        u1.saveProducts(s1.getId(), p1.getID(),10);
        testRemoveproductSuccess();
        u1.saveProducts(s1.getId(), p1.getID(),10);
        testRemoveproductFail();
    }

    @Test
    public void testEditProductQuantity()
    {
        u1.saveProducts(s1.getId(), p1.getID(),10);
        testEditProductQuantitySuccess();
        testEditProductQuantityFail();
    }

    private void testRemoveproductSuccess()
    {
        assertTrue(u1.removeproduct(s1.getId(),p1.getID()));
        assertFalse(u1.getShoppingCart().get(s1.getId()).getProducts().containsKey(p1.getID()));
    }

    private void testRemoveproductFail()
    {
        assertFalse(u1.removeproduct(2,p1.getID()));
        assertFalse(u1.removeproduct(s1.getId(),3));
    }

    public void testEditProductQuantitySuccess()
    {
        int newquantity =55;
        assertTrue(u1.editProductQuantity(s1.getId(),p1.getID(),newquantity));
        assertEquals(newquantity,(int) u1.getShoppingCart().get(s1.getId()).getProducts().get(p1.getID()));
    }

    public void testEditProductQuantityFail()
    {
        int newquantity =55;
        assertFalse(u1.editProductQuantity(2,p1.getID(),newquantity));
        assertFalse(u1.editProductQuantity(s1.getId(),3,newquantity));
    }


    private Shop createShopWithProduct() {
        Shop s1 = createShop();
        p1 = createProduct();
        s1.addProduct(p1);
        return s1;
    }

    private Shop createShop() {
        return new Shop(100, "shop", founder);
    }

    private User createUser()
    {
        return new Guest("Yuval");
    }

    private Product createProduct() {
        return new Product(1, "a", 5, 100);
    }

    private SubscribedUser createFounder() {
        return new SubscribedUser("Founder Guy");
    }
}