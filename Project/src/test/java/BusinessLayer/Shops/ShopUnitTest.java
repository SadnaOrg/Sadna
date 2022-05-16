package BusinessLayer.Shops;
import BusinessLayer.Products.Product;
import BusinessLayer.Products.ProductFilters;
import BusinessLayer.Users.*;
import org.junit.*;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;
public class ShopUnitTest {

    private Shop s1;

    @Mock
    private SubscribedUser founder;
    @Mock
    private Product p1;
    @Mock
    private Product p2;
    @Mock
    private Product p3;
    @Mock
    private Product p4;
    @Mock
    private Product p5;
    @Mock
    private  User u1;
    @Mock
    private  User u2;
    @Mock
    private Basket b1;
    @Mock
    private Basket b2;
    @Mock
    private ProductFilters productFilters;
    @Mock
    private SubscribedUser subscribedUser;
    @Mock
    private ShopAdministrator sa1;

    @Rule public MockitoRule rule = MockitoJUnit.rule();


    @Before
    public void setUp() {
        when(founder.getName()).thenReturn("Meir");
        s1 = new Shop(100, "shop", founder);
        when(p1.getID()).thenReturn(1);
        when(p1.getName()).thenReturn("a");
        when(p1.getPrice()).thenReturn(5.0);
        when(p1.getQuantity()).thenReturn(100);
        when(p2.getID()).thenReturn(2);
        when(p2.getName()).thenReturn("c");
        when(p2.getPrice()).thenReturn(15.0);
        when(p2.getQuantity()).thenReturn(500);
        when(u1.getName()).thenReturn("Guy");
        when(u2.getName()).thenReturn("Maor");
        when(b1.getShopid()).thenReturn(s1.getId());
        when(subscribedUser.getUserName()).thenReturn("MeirMaor");
        when(sa1.getUser()).thenReturn(subscribedUser);
        when(sa1.getUserName()).thenReturn("MeirMaor");
    }

    @Test
    public void closeShopCheckWhenClosedAddProduct() {
        s1.addProduct(p1);
        assertTrue(s1.close());
        try {
            s1.addProduct(p2);
        }
        catch (IllegalStateException e)
        {
            Assert.assertEquals(e.getMessage(),"The shop is closed");
            assertTrue(s1.open());
            Assert.assertEquals(1, s1.getProducts().size());
            Assert.assertEquals(p1.getName(), s1.getProducts().get(p1.getID()).getName());

        }
    }


    @Test
    public void closeShopCheckWhenClosedChangeProduct() {
        s1.addProduct(p1);
        assertTrue(s1.close());
        try {
            p2 = new Product(1, "c", 15, 500);
            s1.changeProduct(p2);
        }
        catch (IllegalStateException e)
        {
            Assert.assertEquals(e.getMessage(),"The shop is closed");
            assertTrue(s1.open());
            Assert.assertEquals(p1.getName(),s1.getProducts().get(p2.getID()).getName());
        }
    }

    @Test
    public void closeShopCheckWhenClosedRemoveProduct() {
        s1.addProduct(p1);
        assertTrue(s1.close());
        try {
            s1.removeProduct(p1.getID());
        }
        catch (IllegalStateException e)
        {
            Assert.assertEquals(e.getMessage(),"The shop is closed");
            assertTrue(s1.open());
            Assert.assertEquals(1, s1.getProducts().size());
            Assert.assertEquals(p1.getName(), s1.getProducts().get(p1.getID()).getName());

        }
    }

    @Test
    public void closeShopCheckWhenClosedGetProducts() {
        s1.addProduct(p1);
        assertTrue(s1.close());
        try {
            s1.getProducts();
        }
        catch (IllegalStateException e)
        {
            Assert.assertEquals(e.getMessage(),"The shop is closed");
            assertTrue(s1.open());
            Assert.assertEquals(1, s1.getProducts().size());
            Assert.assertEquals(p1.getName(), s1.getProducts().get(p1.getID()).getName());

        }
    }


    @Test
    public void closeShopCheckWhenClosedSearchProducts() {
        s1.addProduct(p1);
        assertTrue(s1.close());
        try {
            s1.searchProducts(productFilters);
        }
        catch (IllegalStateException e)
        {
            Assert.assertEquals(e.getMessage(),"The shop is closed");
            assertTrue(s1.open());
            Assert.assertEquals(1, s1.getProducts().size());
            Assert.assertEquals(p1.getName(), s1.getProducts().get(p1.getID()).getName());
        }
    }

    @Test
    public void closeShopCheckWhenClosedPurchaseBasket() {
        s1.addProduct(p1);
        when(u1.getName()).thenReturn("Guy");
        assertTrue(s1.close());
        try {
            s1.purchaseBasket(u1.getName());
        }
        catch (IllegalStateException e)
        {
            Assert.assertEquals(e.getMessage(),"The shop is closed");
            assertTrue(s1.open());
            Assert.assertEquals(1, s1.getProducts().size());
            Assert.assertEquals(p1.getName(), s1.getProducts().get(p1.getID()).getName());
        }
    }

    @Test
    public void closeShopCheckWhenClosedCheckIfUserHasBasket() {
        s1.addProduct(p1);

        assertTrue(s1.close());
        try {
            s1.checkIfUserHasBasket(u1.getName());
        }
        catch (IllegalStateException e)
        {
            Assert.assertEquals(e.getMessage(),"The shop is closed");
            assertTrue(s1.open());
            Assert.assertEquals(1, s1.getProducts().size());
            Assert.assertFalse(s1.checkIfUserHasBasket(u1.getName()));
        }
    }
    @Test
    public void closeShopCheckWhenClosedGetUsersBaskets() {
        s1.addProduct(p1);
        s1.addBasket(u1.getName(),b1);
        Assert.assertEquals(1, s1.getUsersBaskets().size());
        assertTrue(s1.close());
        try {
            s1.getUsersBaskets();
        }
        catch (IllegalStateException e)
        {
            Assert.assertEquals(e.getMessage(),"The shop is closed");
            assertTrue(s1.open());
            Assert.assertEquals(1, s1.getUsersBaskets().size());
            Assert.assertEquals(b1, s1.getUsersBaskets().get(u1.getName()));
        }
    }

    @Test
    public void closeShopCheckWhenClosedAddBasket() {
        s1.addProduct(p1);
        assertTrue(s1.close());
        try {
            s1.addBasket(u1.getName(),b1);
        }
        catch (IllegalStateException e)
        {
            Assert.assertEquals(e.getMessage(),"The shop is closed");
            assertTrue(s1.open());
            Assert.assertEquals(0, s1.getUsersBaskets().size());
        }
    }

    @Test
    public void closeShopCheckWhenClosedAddAdministrator() {
        s1.addProduct(p1);
        assertTrue(s1.close());
        try {
            s1.addAdministrator(sa1.getUser().getUserName(),sa1);
        }
        catch (IllegalStateException e)
        {
            Assert.assertEquals(e.getMessage(),"The shop is closed");
            assertTrue(s1.open());
            Assert.assertEquals(1, s1.getShopAdministrators().size());
            Assert.assertNull(s1.getShopAdministrator(sa1.getUser().getUserName()));
        }
    }

    @Test
    public void addProductAndOtherProduct() {
        s1.addProduct(p1);
        Assert.assertEquals(1, s1.getProducts().size());
        s1.addProduct(p2);
        Assert.assertEquals(2, s1.getProducts().size());
        Assert.assertEquals(p1.getName(), s1.getProducts().get(p1.getID()).getName());
        Assert.assertNotEquals(p1.getName(), s1.getProducts().get(p2.getID()).getName());
        Assert.assertEquals(p2.getName(), s1.getProducts().get(p2.getID()).getName());
    }


    @Test
    public void addProductWhenProductIdAlreadyInTheShop() {
        s1.addProduct(p1);
        when(p3.getID()).thenReturn(1);
        when(p3.getName()).thenReturn("fake");
        when(p3.getPrice()).thenReturn(50.0);
        when(p3.getQuantity()).thenReturn(1000);
        Assert.assertEquals(1, s1.getProducts().size());
        try {
            s1.addProduct(p3);
        }
        catch (IllegalStateException e)
        {
            Assert.assertEquals(e.getMessage(),"The product is already in the shop");
            Assert.assertEquals(1, s1.getProducts().size());
            Assert.assertEquals(p1.getName(), s1.getProducts().get(p3.getID()).getName());
            Assert.assertNotEquals(p3.getName(), s1.getProducts().get(p3.getID()).getName());
        }
    }
//
//    @Test
//    public void changeProductAndChangeItAgainAndAnotherOne() {
//        s1.addProduct(p1);
//        Assert.assertEquals(1, s1.getProducts().size());
//        s1.addProduct(p2);
//        Assert.assertEquals(2, s1.getProducts().size());
//        Assert.assertEquals(p1.getName(), s1.getProducts().get(p1.getID()).getName());
//        Assert.assertNotEquals(p1.getName(), s1.getProducts().get(p2.getID()).getName());
//        Assert.assertEquals(p2.getName(), s1.getProducts().get(p2.getID()).getName());
//
//        when(p3.getID()).thenReturn(1);
//        when(p3.getName()).thenReturn("Meir");
//        when(p3.getPrice()).thenReturn(222.0);
//        when(p3.getQuantity()).thenReturn(2223);
//
//        s1.changeProduct(p3);
//        Assert.assertEquals(2, s1.getProducts().size());
//        Assert.assertEquals(p3.getName(), s1.getProducts().get(p1.getID()).getName());
//        Assert.assertEquals(p1.getName(), s1.getProducts().get(p3.getID()).getName());
//        Assert.assertEquals(p2.getName(), s1.getProducts().get(p2.getID()).getName());
//
//        when(p4.getID()).thenReturn(1);
//        when(p4.getName()).thenReturn("Maouer");
//        when(p4.getPrice()).thenReturn(4222.0);
//        when(p4.getQuantity()).thenReturn(22523);
//
//
//        s1.changeProduct(p4);
//        Assert.assertEquals(2, s1.getProducts().size());
//        Assert.assertEquals(p4.getName(), s1.getProducts().get(p3.getID()).getName());
//        Assert.assertNotEquals(p3.getName(), s1.getProducts().get(p4.getID()).getName());
//        Assert.assertEquals(p2.getName(), s1.getProducts().get(p2.getID()).getName());
//
//        Product p5= new Product(2 , "wello", 32,32);
//        when(p5.getID()).thenReturn(2);
//        when(p5.getName()).thenReturn("wello");
//        when(p5.getPrice()).thenReturn(32.0);
//        when(p5.getQuantity()).thenReturn(32);
//
//
//        s1.changeProduct(p5);
//        Assert.assertEquals(2, s1.getProducts().size());
//        Assert.assertEquals(p4.getName(), s1.getProducts().get(p3.getID()).getName());
//        Assert.assertNotEquals(p5.getName(), s1.getProducts().get(p4.getID()).getName());
//        Assert.assertEquals(p5.getName(), s1.getProducts().get(p2.getID()).getName());
//
//    }

    @Test
    public void ChangeProductWhenProductIdNotInTheShop() {
        s1.addProduct(p1);
        Product p3 = new Product(32, "fake", 50, 1000);
        Assert.assertEquals(1, s1.getProducts().size());
        try {
            s1.changeProduct(p3);
        }
        catch (IllegalStateException e)
        {
            Assert.assertEquals(e.getMessage(),"The product is not in the shop");
            Assert.assertEquals(1, s1.getProducts().size());
            Assert.assertEquals(p1.getName(), s1.getProducts().get(p1.getID()).getName());
            Assert.assertFalse(s1.getProducts().containsKey(p3.getID()));
        }
    }


    @Test
    public void removeProductAndRemoveItAgain() throws IllegalStateException {
        s1.addProduct(p1);
        s1.addProduct(p2);
        s1.removeProduct(p1.getID());
        Assert.assertEquals(1, s1.getProducts().size());
        Assert.assertFalse(s1.getProducts().containsKey(p1.getID()));
        Assert.assertTrue(s1.getProducts().containsKey(p2.getID()));
        try {
            s1.removeProduct(p1.getID());
        }
        catch (IllegalStateException e)
        {
            Assert.assertEquals(e.getMessage(),"The product is not in the shop");
            Assert.assertEquals(1, s1.getProducts().size());
            Assert.assertFalse(s1.getProducts().containsKey(p1.getID()));
            Assert.assertTrue(s1.getProducts().containsKey(p2.getID()));
        }
        s1.removeProduct(p2.getID());
        Assert.assertEquals(0, s1.getProducts().size());
    }
    @Test
    public void removeProductNotExist() throws IllegalStateException {
        s1.addProduct(p1);
        s1.addProduct(p2);
        Assert.assertEquals(2, s1.getProducts().size());
        try {
            s1.removeProduct(p3.getID());
        }
        catch (IllegalStateException e)
        {
            Assert.assertEquals(e.getMessage(),"The product is not in the shop");
            Assert.assertEquals(2, s1.getProducts().size());
            Assert.assertEquals(p1.getName(), s1.getProducts().get(p1.getID()).getName());
            Assert.assertEquals(p2.getName(), s1.getProducts().get(p2.getID()).getName());
            Assert.assertFalse(s1.getProducts().containsKey(p3.getID()));
        }
    }

    @Test
    public void purchaseBasketProductsExistAndInStock() {
        s1.addProduct(p1);
        s1.addProduct(p2);
        s1.addBasket(u1.getName(), b1);
        ConcurrentHashMap<Integer,Integer> map= new ConcurrentHashMap<>();
        map.put(p1.getID(),10);
        map.put(p2.getID(),50);
        when(b1.getProducts()).thenReturn(map);
        when(p1.purchaseProduct(10)).thenReturn(5.0*10);
        when(p2.purchaseProduct(50)).thenReturn(15.0*50);
        double price =s1.purchaseBasket(u1.getName());
        Assert.assertEquals(price, 5.0*10+15.0*50, 0.0);
    }


    @Test
    public void purchaseBasketOneOfTheProductsNotInShop() {
        s1.addProduct(p1);
        s1.addProduct(p2);
        when(p3.getID()).thenReturn(32);
        when(p3.getName()).thenReturn("fake");
        when(p3.getPrice()).thenReturn(50.0);
        when(p3.getQuantity()).thenReturn(1000);
        ConcurrentHashMap<Integer,Integer> map= new ConcurrentHashMap<>();
        map.put(p1.getID(),10);
        map.put(p2.getID(),50);
        map.put(p3.getID(),51);
        when(b1.getProducts()).thenReturn(map);
        s1.addBasket(u1.getName(), b1);
        try {
            double price = s1.purchaseBasket(u1.getName());
            fail("shouldn't end here p3 not in s1");
        }
        catch (IllegalStateException e) {
            Assert.assertEquals(e.getMessage(), "The product is not in the shop");
            Assert.assertEquals(s1.getUsersBaskets().get(u1.getName()), b1);
            Assert.assertEquals(3,b1.getProducts().size());
            Assert.assertEquals(s1.getUsersBaskets().get(u1.getName()).getProducts().size(),b1.getProducts().size());

        }
    }

    @Test
    public void purchaseBasketOneOfTheProductsOutOfStock() {
        s1.addProduct(p1);
        s1.addProduct(p2);
        when(p3.getID()).thenReturn(32);
        when(p3.getName()).thenReturn("fake");
        when(p3.getPrice()).thenReturn(50.0);
        when(p3.getQuantity()).thenReturn(10);
        s1.addProduct(p3);
        ConcurrentHashMap<Integer,Integer> map= new ConcurrentHashMap<>();
        map.put(p1.getID(),10);
        map.put(p2.getID(),50);
        map.put(p3.getID(),51);
        when(b1.getProducts()).thenReturn(map);
        s1.addBasket(u1.getName(), b1);
        try {
            double price = s1.purchaseBasket(u1.getName());
            fail("shouldn't end here p3 not in s1");
        }
        catch (IllegalStateException e) {
            Assert.assertEquals(e.getMessage(), "Try to buy out of stock product from the shop");
            Assert.assertEquals(s1.getUsersBaskets().get(u1.getName()), b1);
            Assert.assertEquals(3,b1.getProducts().size());
            Assert.assertEquals(s1.getUsersBaskets().get(u1.getName()).getProducts().size(),b1.getProducts().size());

        }
    }

    @Test
    public void checkIfUserHasBasketTrueAndFalseCases() {
        s1.addProduct(p1);
        s1.addProduct(p2);
        ConcurrentHashMap<Integer,Integer> map= new ConcurrentHashMap<>();
        map.put(p1.getID(),10);
        map.put(p2.getID(),50);
        when(b1.getProducts()).thenReturn(map);
        s1.addBasket(u1.getName(), b1);
        Assert.assertTrue(s1.checkIfUserHasBasket(u1.getName()));
        Assert.assertFalse(s1.checkIfUserHasBasket(u2.getName()));
    }

    @Test
    public void addBasketTrueAndFalseCases() {
        s1.addProduct(p1);
        s1.addProduct(p2);
        when(b2.getShopid()).thenReturn(s1.getId());
        b1.saveProducts(p1.getID(),10);
        b1.saveProducts(p2.getID(),50);
        ConcurrentHashMap<Integer,Integer> map= new ConcurrentHashMap<>();
        map.put(p1.getID(),10);
        map.put(p2.getID(),50);
        when(b1.getProducts()).thenReturn(map);
        s1.addBasket(u1.getName(), b1);
        ConcurrentHashMap<Integer,Integer> map2= new ConcurrentHashMap<>();
        map2.put(p1.getID(),10);
        when(b1.getProducts()).thenReturn(map2);
        Assert.assertTrue(s1.checkIfUserHasBasket(u1.getName()));
        Assert.assertFalse(s1.checkIfUserHasBasket(u2.getName()));
        try
        {
            s1.addBasket(u1.getName(),b2);
            fail("Basket already in the shop");
        }
        catch (IllegalStateException e)
        {
            Assert.assertEquals(e.getMessage(), "The user' basket is already in the shop");
            Assert.assertTrue(s1.checkIfUserHasBasket(u1.getName()));
            Assert.assertEquals(1,s1.getUsersBaskets().size());
        }
    }
    @Test

    public void addAdministratorNewOne() {
        Assert.assertEquals(1, s1.getShopAdministrators().size());
        Assert.assertTrue(s1.addAdministrator(sa1.getUserName(),sa1));
        Assert.assertEquals(2, s1.getShopAdministrators().size());
        Assert.assertFalse(s1.addAdministrator(sa1.getUserName(),sa1));
        Assert.assertEquals(2, s1.getShopAdministrators().size());
    }
}
