package BusinessLayer.Users;

import BusinessLayer.Products.Product;
import BusinessLayer.Products.Users.Basket;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BasketTest {

    private Product p1 = mock(Product.class);

    private Basket b;

    @Before
    public void setUp(){
        b = new Basket(0);
    }

    @After
    public void tearDown(){
        ConcurrentHashMap<Integer, Integer> basketProducts = b.getProducts();
        for (Integer productID:
                basketProducts.keySet()) {
            b.removeProduct(productID);
        }
    }

    @Test
    public void addProductToBasketSuccess(){
        when(p1.getID()).thenReturn(0);
        when(p1.getQuantity()).thenReturn(10);
        int bought = p1.getQuantity() - 2;
        boolean saved = b.saveProducts(p1.getID(), bought);
        assertTrue(saved);

        ConcurrentHashMap<Integer, Integer> products = b.getProducts();
        int quantity = products.getOrDefault(p1.getID(),-1);
        assertEquals(bought, quantity);
    }

    @Test
    public void removeProductSuccess(){
        addProductToBasketSuccess();
        boolean removed = b.removeProduct(0);
        assertTrue(removed);

        ConcurrentHashMap<Integer, Integer> products = b.getProducts();
        int productsInBasket = products.size();
        assertEquals(0, productsInBasket);
    }

    @Test
    public void removeProductFailure(){
        addProductToBasketSuccess();
        try {
            b.removeProduct(1);
            fail("Removed a product that isn't in the basket!");
        }
        catch (Exception e){
            ConcurrentHashMap<Integer, Integer> products = b.getProducts();
            int productsInBasket = products.size();
            assertEquals(1, productsInBasket);
        }
    }

    @Test
    public void editProductQuantitySuccess(){
        addProductToBasketSuccess();
        int newQuantity = 5;
        boolean edited = b.editProductQuantity(0, newQuantity);
        assertTrue(edited);

        ConcurrentHashMap<Integer, Integer> products = b.getProducts();
        int p0Quantity = products.getOrDefault(0, -1);
        assertEquals(newQuantity, p0Quantity);
    }

    @Test
    public void editProductQuantityProductNotInCart(){
        try {
            b.editProductQuantity(2, 4);
            fail("Edited the quantity of a product that isn't in the basket!");
        }
        catch (Exception e){
            ConcurrentHashMap<Integer, Integer> products = b.getProducts();
            int productInBasket = products.size();
            assertEquals(0, productInBasket);
        }
    }

    @Test
    public void editProductQuantityNegativeQuantity(){
        addProductToBasketSuccess();
        try {
            b.editProductQuantity(0, -1);
            fail("Edited quantity of product to a negative quantity!");
        }
        catch (Exception e){
            ConcurrentHashMap<Integer, Integer> products = b.getProducts();
            int p0Quantity = products.getOrDefault(0, -1);
            assertEquals(8, p0Quantity);
        };
    }

    @Test
    public void editProductQuantityZeroQuantity(){
        addProductToBasketSuccess();
        boolean edited = b.editProductQuantity(0, 0);
        assertTrue(edited);

        ConcurrentHashMap<Integer, Integer> products = b.getProducts();
        int productInBasket = products.size();
        assertEquals(0, productInBasket);
    }
}