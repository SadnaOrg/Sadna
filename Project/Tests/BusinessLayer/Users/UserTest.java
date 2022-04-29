package BusinessLayer.Users;

import BusinessLayer.Shops.Product;
import BusinessLayer.Shops.ProductImpl;
import BusinessLayer.Shops.Shop;
import BusinessLayer.Shops.ShopImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserTest {
    Shop s1;
    User u1;
    @Before
    public void setUp() throws Exception {
        s1 = createShopWithProduct();
        u1 = createUser();
    }

    @Test
    public void testSaveProducts() {
        u1.saveProducts(100,1,10);

        assertEquals(10, (int) u1.getShoppingCart().get(100).getProducts().get(1));

    }





    private Shop createShopWithProduct() {
        Shop s1 = createShop();
        Product p1 = createProduct();
        s1.addProduct(p1);
        return s1;
    }

    private Shop createShop() {
        return new ShopImpl(100, "shop");
    }

    private User createUser()
    {
        return new User() { };
    }

    private Product createProduct() {
        return new ProductImpl(1, "a", 5, 100);
    }
}