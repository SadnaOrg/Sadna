package BusinessLayer.Users.BaseActions;

import BusinessLayer.Products.Product;
import BusinessLayer.Shops.Shop;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StockManagementTest {
    @Mock
    private Shop shop;
    @InjectMocks
    private StockManagement management;
    private ConcurrentHashMap<Integer, Product> products;

    @BeforeEach
    public void setUp(){
        Product testProduct = new Product(0, "fork", 12.5, 20);
        products = new ConcurrentHashMap<>();
        products.put(0, testProduct);
    }

    @Test
    public void changeProductQuantitySuccess(){
        when(shop.getProducts()).thenReturn(products);

        boolean res = management.changeProductQuantity(0,10);
        assertTrue(res);
    }

    @Test
    public void changeProductQuantityFailure(){
        when(shop.getProducts()).thenReturn(products);

        boolean res = management.changeProductQuantity(1,10);
        assertFalse(res);
    }

    @Test
    public void changeProductPriceSuccess(){
        when(shop.getProducts()).thenReturn(products);

        boolean res = management.changeProductPrice(0,13);
        assertTrue(res);
    }

    @Test
    public void changeProductPriceFailure(){
        when(shop.getProducts()).thenReturn(products);

        boolean res = management.changeProductPrice(1,10);
        assertFalse(res);
    }

    @Test
    public void changeProductDescSuccess(){
        when(shop.getProducts()).thenReturn(products);

        boolean res = management.changeProductDesc(0,"awesome!");
        assertTrue(res);
    }

    @Test
    public void changeProductDescFailure(){
        when(shop.getProducts()).thenReturn(products);

        boolean res = management.changeProductDesc(1,"not here");
        assertFalse(res);
    }

    @Test
    public void changeProductNameSuccess(){
        when(shop.getProducts()).thenReturn(products);

        boolean res = management.changeProductName(0,"awesome!");
        assertTrue(res);
    }

    @Test
    public void changeProductNameFailure(){
        when(shop.getProducts()).thenReturn(products);

        boolean res = management.changeProductName(1,"not here");
        assertFalse(res);
    }
}
