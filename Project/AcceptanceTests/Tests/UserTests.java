package Tests;

import Bridge.UserProxy;
import org.junit.jupiter.api.Test;

import DataObjects.*;
import Threads.ACEFounderPurchase;
import Threads.MegaSportFounderPurchase;
import org.testng.annotations.BeforeClass;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class UserTests extends ProjectTests {

    @BeforeClass
    public static void setUp(){
        setUpTests();
        setUserBridge(new UserProxy());
    }

    @Test
    public void testVisitSingleGuest() {
        Guest newGuest = userBridge.visit();
        int guestID = newGuest.getID();
        assertTrue(guestID >= 0);

        ShoppingCart cart = userBridge.checkCart(newGuest.ID);
        assertNotNull(cart);
        assertEquals(0,cart.numOfProductsInCart());
        assertEquals(0,cart.getNumberOfBaskets());
    }

    @Test
    public void testVisitTwoGuests() {
        Guest guest1 = userBridge.visit();
        int guest1ID = guest1.getID();
        assertTrue(guest1ID >= 0);

        Guest guest2 = userBridge.visit();
        int guest2ID = guest2.getID();
        assertTrue(guest2ID >= 0);

        assertTrue(guest1ID != guest2ID);

        userBridge.exit(guest1.ID);
        userBridge.exit(guest2.ID);
    }


    // this is a set-up methods for tests that need a user object
    public static User enter() {
        return null;
    }

    @Test
    public void testGetShopsInfoByRatingSuccess() {
        List<Integer> expected = new LinkedList<>();
        expected.add(0);
        expected.add(1);
        expected.add(2);
        filterSearchShopSuccess(shopFilters[RATING_FILTER],expected);
    }

    @Test
    public void testGetShopsInfoByRatingFailure() {
        filterSearchShopFailure(shopFailFilters[RATINGF_FILTER]);
    }

    @Test
    public void testGetShopsInfoByNameSuccess() {
        List<Integer> expected = new LinkedList<>();
        expected.add(0);
        filterSearchShopSuccess(shopFilters[NAME_FILTER],expected);
    }

    @Test
    public void testGetShopsInfoByNameFailure() {
        filterSearchShopFailure(shopFilters[NAMEF_FILTER]);
    }

    @Test
    public void testGetShopsInfoByCategorySuccess() {
        List<Integer> expected = new LinkedList<>();
        expected.add(2);
        filterSearchShopSuccess(shopFilters[CATEGORY_FILTER],expected);
    }

    @Test
    public void testGetShopsInfoByCategoryFailure() {
        filterSearchShopFailure(shopFilters[CATEGORYF_FILTER]);
    }

    @Test
    public void testGetProductsInfoByRatingSuccess() {
        List<ProductInShop> expected = new LinkedList<>();
        expected.add(new ProductInShop(345,1,100,25,4.2,new Product("jeans","china")));
        expected.add(new ProductInShop(1,0,100,35,4.2,new Product("office chair","china")));
        expected.add(new ProductInShop(31,2,100,55,4.2,new Product("dumbbell" ,"china")));
        filterSearchProductSuccess(productFilters[PRODUCT_RATING_FILTER],expected);
    }

    @Test
    public void testGetProductsInfoByRatingFailure() {
        filterSearchProductFailure(productFailFilters[PRODUCT_RATINGF_FILTER]);
    }

    @Test
    public void testGetProductsInfoByManufacturerSuccess() {
        List<ProductInShop> expected = new LinkedList<>();
        expected.add(new ProductInShop(0,0,30,20,3,new Product("lamp","israel")));
        filterSearchProductSuccess(productFilters[MANUFACTURER_FILTER],expected);
    }

    @Test
    public void testGetProductsInfoByManufacturerFailure() {
        filterSearchProductFailure(productFailFilters[MANUFACTURERF_FILTER]);
    }

    @Test
    public void testSearchShopFilteredSuccess(){
        List<ProductInShop> products = userBridge.filterShopProducts(0,productFilters[RATING_FILTER]);
        assertEquals(1, products.size());
        assertEquals(1,products.get(0).ID);
    }

    @Test
    public void testSearchShopFilteredFailure(){
        List<ProductInShop> products = userBridge.filterShopProducts(0,productFailFilters[RATINGF_FILTER]);
        assertEquals(0, products.size());
    }

    @Test
    public void testSearchShopProducts() {
        testGetShopsInfoByNameSuccess();
        List<ProductInShop> products = userBridge.searchShopProducts(shops[ACE_ID].ID);
        assertNotNull(products);
        assertSame(products, shops[ACE_ID].products);
    }

    @Test
    public void testSearchProductsSuccess() {
        ProductFilter filter = productFilters[MANUFACTURER_FILTER];
        List<ProductInShop> products = userBridge.searchProducts(filter);
        for (ProductInShop p:
                products) {
            assertTrue(filter.filter(p));
        }
    }

    @Test
    public void testAddProductToCartSuccess() {
        User u = enter();
        userBridge.addProductToCart(u.ID,shops[castro_ID].ID,2,10);
        ShoppingCart addResult = userBridge.checkCart(u.ID);
        assertEquals(1,addResult.numOfProductsInCart());

        ShopBasket basket = addResult.getShopBasket(castro_ID);
        int product_quantity = basket.getProductQuantity(2);

        assertNotNull(basket);
        assertEquals(10,product_quantity);
        assertEquals(10,addResult.numOfProductsInCart());
        assertEquals(1,addResult.getNumberOfBaskets());

        //cancel side-effects
        userBridge.updateCart(u.ID,new int[]{2},new int[]{shops[castro_ID].ID},new int[]{0});

        boolean exitResult = userBridge.exit(u.ID);
        assertTrue(exitResult);
    }

    @Test
    public void testAddProductToCartFailureTooLargeQuantity() {
        User u = enter();
        userBridge.addProductToCart(u.ID,shops[castro_ID].ID,2,100);
        ShoppingCart addResult = userBridge.checkCart(u.ID);
        assertEquals(0,addResult.numOfProductsInCart());
        assertEquals(0,addResult.getNumberOfBaskets());
        boolean exitResult = userBridge.exit(u.ID);
        assertTrue(exitResult);
    }

    @Test
    public void testAddProductToCartFailureNegativeQuantity() {
        User u = enter();
        userBridge.addProductToCart(u.ID,shops[castro_ID].ID,2,-1);
        ShoppingCart addResult = userBridge.checkCart(u.ID);
        assertEquals(0,addResult.numOfProductsInCart());
        assertEquals(0,addResult.getNumberOfBaskets());
        boolean exitResult = userBridge.exit(u.ID);
        assertTrue(exitResult);
    }

    @Test
    public void testAddProductToCartFailureProductNotInShop() {
        User u = enter();
        userBridge.addProductToCart(u.ID,shops[castro_ID].ID,13,10);
        ShoppingCart addResult = userBridge.checkCart(u.ID);
        assertEquals(0,addResult.numOfProductsInCart());
        assertEquals(0,addResult.getNumberOfBaskets());
        boolean exitResult = userBridge.exit(u.ID);
        assertTrue(exitResult);
    }

    @Test
    public void testCheckCartEmpty() {
        User u = enter();
        ShoppingCart userCart = userBridge.checkCart(u.ID);
        assertEquals(0,userCart.numOfProductsInCart());
        boolean exitResult = userBridge.exit(u.ID);
        assertTrue(exitResult);
    }

    @Test
    public void testCheckCartNotEmpty() {
        User u = enter();
        userBridge.addProductToCart(u.ID,shops[castro_ID].ID,2,10);
        userBridge.addProductToCart(u.ID,shops[ACE_ID].ID,0,10);
        ShoppingCart userCart = userBridge.checkCart(u.ID);
        assertEquals(20,userCart.numOfProductsInCart());

        ShopBasket basket1 = userCart.getShopBasket(shops[castro_ID].ID);
        assertNotNull(basket1);

        int product1Quantity = basket1.getProductQuantity(2);

        assertNotNull(basket1);
        assertEquals(20,userCart.numOfProductsInCart());
        assertEquals(2,userCart.getNumberOfBaskets());

        assertEquals(10,product1Quantity);

        ShopBasket basket2 = userCart.getShopBasket(shops[ACE_ID].ID);
        assertNotNull(basket2);

        int product2Quantity = basket1.getProductQuantity(0);

        assertEquals(10,product2Quantity);

        //cancel side-effects
        userBridge.updateCart(u.ID,new int[]{2,0},new int[]{shops[castro_ID].ID,shops[ACE_ID].ID},new int[]{0,0});

        boolean exitResult = userBridge.exit(u.ID);
        assertTrue(exitResult);
    }

    @Test
    public void testUpdateCartSuccessIncreaseQuantity() {
        User u = enter();
        userBridge.addProductToCart(u.ID,shops[castro_ID].ID,345,10);
        userBridge.updateCart(u.ID,new int[]{345},new int[]{2},new int[]{92});
        ShoppingCart userCart = userBridge.checkCart(u.ID);

        ShopBasket basket = userCart.getShopBasket(shops[castro_ID].ID);
        assertNotNull(basket);

        int productQuantity = basket.getProductQuantity(345);

        assertEquals(92,productQuantity);
        assertEquals(92,userCart.numOfProductsInCart());
        assertEquals(1,userCart.getNumberOfBaskets());

        //cancel side-effects
        userBridge.updateCart(u.ID,new int[]{345},new int[]{shops[castro_ID].ID},new int[]{0});

        boolean exitResult = userBridge.exit(u.ID);
        assertTrue(exitResult);
    }

    @Test
    public void testUpdateCartSuccessDecreaseQuantity() {
        User u = enter();
        userBridge.addProductToCart(u.ID,shops[castro_ID].ID,345,10);
        userBridge.updateCart(u.ID,new int[]{345},new int[]{2},new int[]{2});
        ShoppingCart userCart = userBridge.checkCart(u.ID);

        assertNotNull(userCart);
        assertEquals(2,userCart.numOfProductsInCart());
        assertEquals(1,userCart.getNumberOfBaskets());

        ShopBasket basket = userCart.getShopBasket(shops[castro_ID].ID);
        assertNotNull(basket);

        int quantity = basket.getProductQuantity(345);

        assertEquals(2,quantity);

        //cancel side-effects
        userBridge.updateCart(u.ID,new int[]{345},new int[]{shops[castro_ID].ID},new int[]{0});

        boolean exitResult = userBridge.exit(u.ID);
        assertTrue(exitResult);
    }

    @Test
    public void testUpdateCartSuccessRemoveProduct() {
        User u = enter();
        userBridge.addProductToCart(u.ID,shops[castro_ID].ID,345,10);
        userBridge.updateCart(u.ID,new int[]{345},new int[]{2},new int[]{0});
        ShoppingCart userCart = userBridge.checkCart(u.ID);
        assertNotNull(userCart);

        int numOfProducts = userCart.numOfProductsInCart();
        assertEquals(0,numOfProducts);

        boolean exitResult = userBridge.exit(u.ID);
        assertTrue(exitResult);
    }

    @Test
    public void testUpdateCartFailureIncreaseQuantity() {
        User u = enter();
        userBridge.addProductToCart(u.ID,shops[castro_ID].ID,345,10);
        userBridge.updateCart(u.ID,new int[]{345},new int[]{2},new int[]{102});
        ShoppingCart userCart = userBridge.checkCart(u.ID);
        assertNotNull(userCart);

        ShopBasket basket = userCart.getShopBasket(shops[castro_ID].ID);
        assertNotNull(basket);
        int quantity = basket.getProductQuantity(345);

        assertEquals(10,quantity);

        //cancel side-effects
        userBridge.updateCart(u.ID,new int[]{345},new int[]{2},new int[]{0});

        boolean exitResult = userBridge.exit(u.ID);
        assertTrue(exitResult);
    }

    @Test
    public void testUpdateCartFailureDecreaseQuantity() {
        User u = enter();
        userBridge.addProductToCart(u.ID,shops[castro_ID].ID,345,10);
        userBridge.updateCart(u.ID,new int[]{345},new int[]{2},new int[]{-2});
        ShoppingCart userCart = userBridge.checkCart(u.ID);
        assertNotNull(userCart);

        ShopBasket basket = userCart.getShopBasket(shops[castro_ID].ID);
        assertNotNull(basket);
        int quantity = basket.getProductQuantity(345);

        assertEquals(10,quantity);

        //cancel side-effects
        userBridge.updateCart(u.ID,new int[]{345},new int[]{2},new int[]{0});

        boolean exitResult = userBridge.exit(u.ID);
        assertTrue(exitResult);
    }

    @Test
    public void testUpdateCartFailureProductNotInCart() {
        User u = enter();
        userBridge.addProductToCart(u.ID,shops[castro_ID].ID,345,10);
        userBridge.updateCart(u.ID,new int[]{1},new int[]{0},new int[]{50});
        ShoppingCart userCart = userBridge.checkCart(u.ID);

        ShopBasket basket = userCart.getShopBasket(shops[castro_ID].ID);
        assertNotNull(basket);
        int quantity = basket.getProductQuantity(345);

        assertEquals(10,quantity);

        //cancel side-effects
        userBridge.updateCart(u.ID,new int[]{345},new int[]{2},new int[]{0});

        boolean exitResult = userBridge.exit(u.ID);
        assertTrue(exitResult);
    }

    @Test
    public void testUpdateCartFailureConcurrentPurchase() throws InterruptedException {
        Thread MegaSportFounderPurchase = new MegaSportFounderPurchase();
        Thread ACEFounderPurchase = new ACEFounderPurchase();

        MegaSportFounderPurchase.start();
        ACEFounderPurchase.start();
        MegaSportFounderPurchase.join();
        ACEFounderPurchase.join();

        ShoppingCart MegaSportFounderCart = userBridge.checkCart(MegaSportFounder.ID);
        ShoppingCart ACEFounderCart = userBridge.checkCart(ACEFounder.ID);
        assertNotNull(MegaSportFounderCart);
        assertNotNull(ACEFounderCart);

        int numOfBasketsMegaSportFounder = MegaSportFounderCart.getNumberOfBaskets();
        int numOfBasketsACEFounder = ACEFounderCart.getNumberOfBaskets();
        boolean basketsCheck = numOfBasketsACEFounder == 1 && numOfBasketsMegaSportFounder == 0;
        basketsCheck = basketsCheck || ( numOfBasketsACEFounder == 0 && numOfBasketsMegaSportFounder == 1);
        assertTrue(basketsCheck);
        if(numOfBasketsACEFounder == 1){
            ShopBasket basket = ACEFounderCart.getShopBasket(shops[castro_ID].ID);
            assertNotNull(basket);

            int numOfProducts = basket.numOfProducts();
            int quantity = basket.getProductQuantity(2);
            assertEquals(1,numOfProducts);
            assertEquals(21,quantity);
            assertEquals(21,ACEFounderCart.numOfProductsInCart());
            assertEquals(1,ACEFounderCart.getNumberOfBaskets());

            ShopBasket MegaSportFounderBasket = MegaSportFounderCart.getShopBasket(shops[castro_ID].ID);
            assertNull(MegaSportFounderBasket);
            assertEquals(0,MegaSportFounderCart.getNumberOfBaskets());

            ProductInShop pis = userBridge.searchProductInShop(2, shops[castro_ID].ID);
            assertNotNull(pis);
            assertEquals(9,pis.quantity);

            //cancel side-effects
            userBridge.updateCart(ACEFounder.ID,new int[]{2},new int[]{shops[castro_ID].ID},new int[]{0});

        }
        else {
            ShopBasket basket = MegaSportFounderCart.getShopBasket(shops[castro_ID].ID);
            assertNotNull(basket);

            int numOfProducts = basket.numOfProducts();
            int quantity = basket.getProductQuantity(2);
            assertEquals(1,numOfProducts);
            assertEquals(10,quantity);
            assertEquals(10,ACEFounderCart.numOfProductsInCart());
            assertEquals(1,ACEFounderCart.getNumberOfBaskets());

            ShopBasket ACEFounderBasket = ACEFounderCart.getShopBasket(shops[castro_ID].ID);
            assertNull(ACEFounderBasket);
            assertEquals(0,ACEFounderCart.getNumberOfBaskets());

            ProductInShop pis = userBridge.searchProductInShop(2, shops[castro_ID].ID);
            assertNotNull(pis);
            assertEquals(20,pis.quantity);

            //cancel side-effects
            userBridge.updateCart(MegaSportFounder.ID,new int[]{2},new int[]{shops[castro_ID].ID},new int[]{0});
        }

        userBridge.exit(ACEFounder.ID);
        userBridge.exit(MegaSportFounder.ID);

    }

    @Test
    public void testPurchaseEmptyCart() {
        User u = enter();
        boolean purchaseResult = userBridge.purchaseCart(u.ID);
        assertFalse(purchaseResult);
        assertEquals(0,u.numOfNotifications());

        boolean exitResult = userBridge.exit(u.ID);
        assertTrue(exitResult);
    }

    @Test
    public void testPurchaseNotEmptyCart() {
        User u = enter();
        userBridge.addProductToCart(u.ID,shops[ACE_ID].ID,0,10);
        boolean purchaseResult = userBridge.purchaseCart(u.ID);
        assertTrue(purchaseResult);
        assertEquals(1,u.numOfNotifications());
        assertEquals("Purchase",u.notifications.get(0));

        ShoppingCart userCart = userBridge.checkCart(u.ID);
        int numOfProducts = userCart.numOfProductsInCart();
        assertEquals(0,numOfProducts);

        boolean exitResult = userBridge.exit(u.ID);
        assertTrue(exitResult);
    }

    private void assertShops(List<Shop> shops, ShopFilter filter,List<Integer> expected){
        for (Shop s:
             shops) {
            assertTrue(filter.filter(s));
        }
        List<Integer> shopsIDs = shops.stream().map(productInShop -> productInShop.ID).toList();
        for (int ID:
                expected) {
            assertTrue(shopsIDs.contains(ID));
        }
    }

    private void assertProducts(List<ProductInShop> products, ProductFilter filter,List<ProductInShop> expected){
        for (ProductInShop p:
                products) {
            assertTrue(filter.filter(p));
        }
        List<Integer> productIDs = products.stream().map(productInShop -> productInShop.ID).toList();
        List<Integer> shopIDs = products.stream().map(productInShop -> productInShop.shopID).toList();
        for (ProductInShop pis:
             expected) {
            int id = pis.ID;
            assertTrue(productIDs.contains(id));
            int i = productIDs.indexOf(id);
            int shopID = shopIDs.get(i);
            assertEquals(pis.shopID,shopID);
        }
    }

    private void filterSearchShopSuccess(ShopFilter filter,List<Integer> expected){
        List<Shop> shops = userBridge.getShopsInfo(filter);
        assertNotNull(shops);
        assertShops(shops,filter,expected);
    }

    private void filterSearchShopFailure(ShopFilter filter){
        List<Shop> shops = userBridge.getShopsInfo(filter);
        assertNull(shops);
    }

    private void filterSearchProductSuccess(ProductFilter filter,List<ProductInShop> expected){
        List<ProductInShop> products = userBridge.searchProducts(filter);
        assertNotNull(products);
        assertProducts(products,filter,expected);
    }

    private void filterSearchProductFailure(ProductFilter filter){
        List<ProductInShop> products = userBridge.searchProducts(filter);
        assertNull(products);
    }
}
