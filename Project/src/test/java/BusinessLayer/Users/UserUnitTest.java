package BusinessLayer.Users;

import BusinessLayer.Products.Product;
import BusinessLayer.Products.Users.Basket;
import BusinessLayer.Products.Users.SubscribedUser;
import BusinessLayer.Products.Users.User;
import BusinessLayer.Shops.Shop;
import BusinessLayer.System.PaymentMethod;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
public class UserUnitTest {
    Shop s1;
    User u1;
    Product p1;
    ConcurrentHashMap<Integer, Integer> prods = createProducts();
    private SubscribedUser founder = createFounder();
    private Basket basket = createBasket();
    private PaymentMethod method = createMethod();

    @Before
    public void setUp() {
        s1 = createShopWithProduct();
        u1 = createUser();
        when(u1.getUserName()).thenReturn("Yuval");
        assertSame("Yuval", u1.getUserName());
        when(u1.saveProducts(anyInt(), anyInt(), anyInt(),anyDouble())).thenReturn(true);
        when(s1.getId()).thenReturn(1);
        when(p1.getID()).thenReturn(1);
        u1.saveProducts(s1.getId(), p1.getID(), quantity,p1.getPrice());
        when(u1.getProducts(s1.getId())).thenReturn(prods);
        when(prods.get(p1.getID())).thenReturn(quantity);
        assertSame(u1.getProducts(s1.getId()).get(p1.getID()), quantity);
    }

    @Test
    public void testSaveProductsSuccess() {
        assertEquals(quantity, (int) u1.getProducts(s1.getId()).get(p1.getID()));
    }

    @Test
    public void testSaveProductsFail() {
        when(u1.saveProducts(s1.getId(), p1.getID(), quantity, p1.getPrice())).thenReturn(false);
        //saving a second time
        try {
            assertFalse(u1.saveProducts(s1.getId(), p1.getID(), quantity, p1.getPrice()));
        }
        catch(Exception ignored) {
            assertEquals(quantity, (int) u1.getProducts(s1.getId()).get(p1.getID()));
        }
    }

    @Test
    public void testRemoveProductSuccess() {
        when(u1.removeProduct(s1.getId(), p1.getID())).thenReturn(true);
        assertTrue(u1.removeProduct(s1.getId(),p1.getID()));
        assertFalse(u1.getProducts(s1.getId()).containsKey(p1.getID()));
    }

    @Test
    public void testRemoveProductFailNonExistentItem() {
        when(u1.removeProduct(s1.getId(), 3)).thenReturn(false);
        when(u1.getBasket(s1.getId())).thenReturn(basket);
        when(basket.getProducts()).thenReturn(prods);
        when(prods.size()).thenReturn(1);
        assertFalse(u1.removeProduct(s1.getId(), 3));
        //checks the product was not removed
        assertEquals(1, u1.getBasket(s1.getId()).getProducts().size());
    }

    @Test
    public void testRemoveProductFailNonExistentShop() {
        when(u1.removeProduct(2, p1.getID())).thenReturn(false);
        when(u1.getBasket(s1.getId())).thenReturn(basket);
        when(basket.getProducts()).thenReturn(prods);
        when(prods.size()).thenReturn(1);
        assertFalse(u1.removeProduct(2, p1.getID()));
        assertEquals(1, u1.getBasket(s1.getId()).getProducts().size());
    }

    @Test
    public void testEditProductQuantitySuccess() {
        when(u1.editProductQuantity(s1.getId(), p1.getID(), newQuantity)).thenReturn(true);
        when(u1.getProducts(s1.getId())).thenReturn(prods);
        when(prods.get(s1.getId())).thenReturn(newQuantity);
        assertTrue(u1.editProductQuantity(s1.getId(), p1.getID(), newQuantity));
        assertEquals(newQuantity, (int) u1.getProducts(s1.getId()).get(p1.getID()));
    }

    @Test
    public void testEditProductQuantityFail() {
        when(u1.getBasket(s1.getId())).thenReturn(basket);
        when(basket.getProducts()).thenReturn(prods);
        when(prods.get(s1.getId())).thenReturn(quantity);
        assertFalse(u1.editProductQuantity(s1.getId(),3, newQuantity));
        //checks the quantity was not changed from var "quality" to "newQuality"
        assertNotEquals(newQuantity, (int) u1.getBasket(s1.getId()).getProducts().get(p1.getID()));
    }

    @Test
    public void testEditProductQuantityFailNonExistentShop() {
        when(u1.getBasket(s1.getId())).thenReturn(basket);
        when(basket.getProducts()).thenReturn(prods);
        when(prods.get(s1.getId())).thenReturn(quantity);
        assertFalse(u1.editProductQuantity(2, p1.getID(), newQuantity));
        //checks the quantity was not changed from var "quality" to "newQuality"
        assertNotEquals(newQuantity, (int) u1.getBasket(s1.getId()).getProducts().get(p1.getID()));
    }

    @Test
    public void testUpdatePaymentMethod() {
        when(u1.getMethod()).thenReturn(null);
        assertNull(u1.getMethod());
        u1.updatePaymentMethod(method);
        when(u1.getMethod()).thenReturn(method);
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
        return mock(Shop.class);
    }

    private User createUser() {
        return mock(User.class);
    }

    private Product createProduct() {
        return mock(Product.class);
    }

    private ConcurrentHashMap<Integer, Integer> createProducts() {
        return mock(ConcurrentHashMap.class);
    }

    private SubscribedUser createFounder() {
        return mock(SubscribedUser.class);
    }

    private Basket createBasket(){
        return mock(Basket.class);
    }

    private PaymentMethod createMethod(){
        return mock(PaymentMethod.class);
    }
}
