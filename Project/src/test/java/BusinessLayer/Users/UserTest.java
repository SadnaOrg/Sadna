package BusinessLayer.Users;

import BusinessLayer.Products.Product;
import BusinessLayer.Products.Users.Guest;
import BusinessLayer.Products.Users.SubscribedUser;
import BusinessLayer.Products.Users.User;
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
        try {
            assertFalse(u1.removeproduct(s1.getId(), 3));
            fail("can't remove not exist item");
        }
        catch (Exception e)
        {

            //System.out.println( u1.getBasket(s1.getId()).getProducts().size());
            assertTrue(1== u1.getBasket(s1.getId()).getProducts().size());

            System.out.println( u1.getBasket(s1.getId()).getProducts().size());
            assertTrue(1== u1.getBasket(s1.getId()).getProducts().size());

        }
    }

    public void testEditProductQuantitySuccess()
    {
        int newQuantity =55;
        assertTrue(u1.editProductQuantity(s1.getId(),p1.getID(),newQuantity));
        assertEquals(newQuantity,(int) u1.getShoppingCart().get(s1.getId()).getProducts().get(p1.getID()));
    }

    public void testEditProductQuantityFail()
    {
        int newQuantity =55;
        assertFalse(u1.editProductQuantity(2,p1.getID(),newQuantity));
        try{
            int newnewQuantity =55;
            assertFalse(u1.editProductQuantity(s1.getId(),3,newnewQuantity));
            fail("can't edit not exist item");
        }
        catch (Exception e)
        {
            assertTrue(newQuantity== u1.getBasket(s1.getId()).getProducts().get(p1.getID()));
        }
    }


    private Shop createShopWithProduct() throws IllegalStateException {
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
        return new SubscribedUser("Founder Guy","pass12");
    }
}