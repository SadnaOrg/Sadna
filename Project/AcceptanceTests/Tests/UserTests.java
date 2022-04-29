package Tests;

import Bridge.UserProxy;
import org.junit.BeforeClass;
import org.junit.Test;
import Mocks.*;
import Threads.ACEFounderPurchase;
import Threads.MegaSportFounderPurchase;

import java.util.List;

import static org.junit.Assert.*;

//TODO: check that the result include all of the products/shops
//TODO: check that the other quantities didn't change (cart updates)

//TODO: search tests: return products in shop instead

public abstract class UserTests extends ProjectTests {

    @BeforeClass
    public static void setUp(){
        setUpTests();
        setUserBridge(new UserProxy());
    }

    // Changes system state
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

    // Changes system state
    @Test
    public void testVisitTwoGuests() {
        Guest guest1 = userBridge.visit();
        int guest1ID = guest1.getID();
        assertTrue(guest1ID >= 0);

        Guest guest2 = userBridge.visit();
        int guest2ID = guest2.getID();
        assertTrue(guest2ID >= 0);

        assertTrue(guest1ID != guest2ID);

        userBridge.exit(guest1);
        userBridge.exit(guest2);
    }


    // this is a set-up methods for tests that need a user object
    public abstract User enter();

    @Test
    public void testGetShopsInfoByRatingSuccess() {
        filterSearchShopSuccess(shopFilters[RATING_FILTER]);
    }

    @Test
    public void testGetShopsInfoByRatingFailure() {
        filterSearchShopFailure(shopFailFilters[RATINGF_FILTER]);
    }

    @Test
    public void testGetShopsInfoByNameSuccess() {
        filterSearchShopSuccess(shopFilters[NAME_FILTER]);
    }

    @Test
    public void testGetShopsInfoByNameFailure() {
        filterSearchShopFailure(shopFilters[NAMEF_FILTER]);
    }

    @Test
    public void testGetShopsInfoByCategorySuccess() {
        filterSearchShopSuccess(shopFilters[CATEGORY_FILTER]);
    }

    @Test
    public void testGetShopsInfoByCategoryFailure() {
        filterSearchShopFailure(shopFilters[CATEGORYF_FILTER]);
    }

    @Test
    public void testGetProductsInfoByRatingSuccess() {
        filterSearchProductSuccess(productFilters[PRODUCT_RATING_FILTER]);
    }

    @Test
    public void testGetProductsInfoByRatingFailure() {
        filterSearchProductFailure(productFailFilters[PRODUCT_RATINGF_FILTER]);
    }

    @Test
    public void testGetProductsInfoByManufacturerSuccess() {
        filterSearchProductSuccess(productFilters[MANUFACTURER_FILTER]);
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
        List<Product> products = userBridge.searchShopProducts(shops[ACE_ID]);
        assertNotNull(products);
        assertSame(products, shops[ACE_ID].products);
    }

    @Test
    public void testSearchProductsSuccess() {
        ProductFilter filter = productFilters[MANUFACTURER_FILTER];
        List<Product> products = userBridge.searchProducts(filter);
        for (Product p:
                products) {
            assertTrue(filter.filter(p) && isSold(p));
        }
    }

    @Test
    public void testAddProductToCartSuccess() {
        User u = enter();
        ShoppingCart addResult = userBridge.addProductToCart(u.ID,shops[castro_ID].ID,2,10);
        assertEquals(1,addResult.numOfProductsInCart());

        ShopBasket basket = addResult.getShopBasket(castro_ID);
        int product_quantity = basket.getProductQuantity(2);

        assertNotNull(basket);
        assertEquals(10,product_quantity);
        assertEquals(10,addResult.numOfProductsInCart());
        assertEquals(1,addResult.getNumberOfBaskets());

        boolean exitResult = userBridge.exit(u);
        assertTrue(exitResult);
    }

    @Test
    public void testAddProductToCartFailureTooLargeQuantity() {
        User u = enter();
        ShoppingCart addResult = userBridge.addProductToCart(u.ID,shops[castro_ID].ID,2,100);
        assertEquals(0,addResult.numOfProductsInCart());
        assertEquals(0,addResult.getNumberOfBaskets());
        boolean exitResult = userBridge.exit(u);
        assertTrue(exitResult);
    }

    @Test
    public void testAddProductToCartFailureNegativeQuantity() {
        User u = enter();
        ShoppingCart addResult = userBridge.addProductToCart(u.ID,shops[castro_ID].ID,2,-1);
        assertEquals(0,addResult.numOfProductsInCart());
        assertEquals(0,addResult.getNumberOfBaskets());
        boolean exitResult = userBridge.exit(u);
        assertTrue(exitResult);
    }

    @Test
    public void testAddProductToCartFailureProductNotInShop() {
        User u = enter();
        ShoppingCart addResult = userBridge.addProductToCart(u.ID,shops[castro_ID].ID,13,10);
        assertEquals(0,addResult.numOfProductsInCart());
        assertEquals(0,addResult.getNumberOfBaskets());
        boolean exitResult = userBridge.exit(u);
        assertTrue(exitResult);
    }

    @Test
    public void testCheckCartEmpty() {
        User u = enter();
        ShoppingCart userCart = userBridge.checkCart(u.ID);
        assertEquals(0,userCart.numOfProductsInCart());
        boolean exitResult = userBridge.exit(u);
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
        assertEquals(1,userCart.getNumberOfBaskets());

        assertEquals(10,product1Quantity);

        ShopBasket basket2 = userCart.getShopBasket(shops[ACE_ID].ID);
        assertNotNull(basket2);

        int product2Quantity = basket1.getProductQuantity(0);

        assertEquals(10,product2Quantity);

        boolean exitResult = userBridge.exit(u);
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

        boolean exitResult = userBridge.exit(u);
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

        boolean exitResult = userBridge.exit(u);
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

        boolean exitResult = userBridge.exit(u);
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

        boolean exitResult = userBridge.exit(u);
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

        boolean exitResult = userBridge.exit(u);
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

        boolean exitResult = userBridge.exit(u);
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
        }

        userBridge.exit(ACEFounder);
        userBridge.exit(MegaSportFounder);

    }

    @Test
    public void testPurchaseEmptyCart() {
        User u = enter();
        boolean purchaseResult = userBridge.purchaseCart(u.ID);
        assertFalse(purchaseResult);
        assertEquals(0,u.numOfNotifications());

        boolean exitResult = userBridge.exit(u);
        assertTrue(exitResult);
    }

    @Test
    public void testPurchaseNotEmptyCart() {
        User u = enter();
        userBridge.addProductToCart(u.ID,shops[castro_ID].ID,345,10);
        boolean purchaseResult = userBridge.purchaseCart(u.ID);
        assertTrue(purchaseResult);
        assertEquals(1,u.numOfNotifications());
        assertEquals("Purchase",u.notifications.get(0));

        ShoppingCart userCart = userBridge.checkCart(u.ID);
        int numOfProducts = userCart.numOfProductsInCart();
        assertEquals(0,numOfProducts);

        boolean exitResult = userBridge.exit(u);
        assertTrue(exitResult);
    }

    private void assertShops(List<Shop> shops, ShopFilter filter){
        for (Shop s:
             shops) {
            assertTrue(filter.filter(s));
        }
    }

    private void assertProducts(List<Product> products, ProductFilter filter){
        for (Product p:
                products) {
            assertTrue(filter.filter(p));
        }
    }

    private void filterSearchShopSuccess(ShopFilter filter){
        List<Shop> shops = userBridge.getShopsInfo(filter);
        assertNotNull(shops);
        assertShops(shops,filter);
    }

    private void filterSearchShopFailure(ShopFilter filter){
        List<Shop> shops = userBridge.getShopsInfo(filter);
        assertNull(shops);
    }

    private void filterSearchProductSuccess(ProductFilter filter){
        List<Product> products = userBridge.searchProducts(filter);
        assertNotNull(products);
        assertProducts(products,filter);
    }

    private void filterSearchProductFailure(ProductFilter filter){
        List<Product> products = userBridge.searchProducts(filter);
        assertNull(products);
    }
}
