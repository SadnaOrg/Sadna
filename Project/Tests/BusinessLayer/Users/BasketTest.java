package BusinessLayer.Users;

import BusinessLayer.Products.Product;
import BusinessLayer.Shops.Shop;
import BusinessLayer.Users.Basket;
import BusinessLayer.Users.SubscribedUser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BasketTest {

    private final SubscribedUser founder = createFounder();
    private Shop s1;
    private Basket b1;
    private Product p1;
    private Product p2;
    @BeforeAll
    public void setUp() throws Exception {
        s1 = createShop();
        b1= new Basket(s1.getId());
        p1 = new Product(1, "a", 5, 100);
        p2 =new Product(2, "b", 10, 100);
        createShopWithTwoProducts();
    }

    @Test
    public void saveProducts() {
        b1.saveProducts(p1.getID(), p1.getQuantity());
        assertEquals(1,b1.getProducts().size());
        b1.saveProducts(p2.getID(), p2.getQuantity());
        assertEquals(2,b1.getProducts().size());
        try {
            b1.saveProducts(p1.getID(), p1.getQuantity());
            fail("the product is already in the basket");
        }
        catch (Exception e)
        {
            assertEquals(2,b1.getProducts().size());
        }
        assertTrue(p1.getQuantity()==b1.getProducts().get(p1.getID()));
    }

    @Test
    public void removeProduct() {
        b1.saveProducts(p1.getID(), p1.getQuantity());
        b1.saveProducts(p2.getID(), p2.getQuantity());
        b1.removeProduct(p1.getID());
        assertEquals(1,b1.getProducts().size());
        try
        {
            b1.removeProduct(p1.getID());
            fail("cant remove item that do not exist");
        }
        catch (Exception e)
        {
            assertEquals(1,b1.getProducts().size());
        }

    }

    @Test
    public void editProductQuantity() {
        b1.saveProducts(p1.getID(), p1.getQuantity());
        int newquantity = 513;
        b1.editProductQuantity(p1.getID(),newquantity);
        assertTrue(newquantity== b1.getProducts().get(p1.getID()));
        try {
            int newnewquantity = 513;
            b1.editProductQuantity(p2.getID(),newnewquantity);
            fail("can't change item who dont exist");
        }
        catch (Exception e)
        {
            assertTrue(newquantity== b1.getProducts().get(p1.getID()));
        }
    }

    private Shop createShop() {
        return new Shop(100, "shop", founder);
    }
    private SubscribedUser createFounder() {
        return new SubscribedUser("Founder Guy","Guy123456");
    }

    private Shop createShopWithTwoProducts() throws IllegalStateException {
        s1.addProduct(p1);
        s1.addProduct(p2);
        return s1;
    }
}