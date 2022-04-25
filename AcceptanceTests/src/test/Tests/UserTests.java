package test.Tests;

import test.Bridge.UserProxy;
import test.Mocks.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

//TODO: check that the result include all of the products/shops
//TODO: check that the guest received a cart
//TODO: check that the other quantities didn't change (cart updates)
//TODO: concurrent purchases/updates success/failure

//TODO: fix asserts of cart tests
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

    //The following tests change the system state
    @Test
    public void testAddProductToCartSuccess() {
        User u = enter();
        ShoppingCart addResult = userBridge.addProductToCart(u.ID,shops[castro_ID].ID,2,10);
        assertEquals(1,addResult.numOfProductsInCart());

        int boughtFrom = addResult.getShop(0);
        int product = addResult.getProduct(0);
        int quantity = addResult.getQuantity(0);

        assertEquals(1,boughtFrom);
        assertEquals(2,product);
        assertEquals(10,quantity);

        boolean exitResult = userBridge.exit(u);
        assertTrue(exitResult);
    }

    @Test
    public void testAddProductToCartFailureTooLargeQuantity() {
        User u = enter();
        ShoppingCart addResult = userBridge.addProductToCart(u.ID,shops[castro_ID].ID,2,100);
        assertEquals(0,addResult.numOfProductsInCart());
        boolean exitResult = userBridge.exit(u);
        assertTrue(exitResult);
    }

    @Test
    public void testAddProductToCartFailureNegativeQuantity() {
        User u = enter();
        ShoppingCart addResult = userBridge.addProductToCart(u.ID,shops[castro_ID].ID,2,-1);
        assertEquals(0,addResult.numOfProductsInCart());
        boolean exitResult = userBridge.exit(u);
        assertTrue(exitResult);
    }

    @Test
    public void testAddProductToCartFailureProductNotInShop() {
        User u = enter();
        ShoppingCart addResult = userBridge.addProductToCart(u.ID,shops[castro_ID].ID,13,10);
        assertEquals(0,addResult.numOfProductsInCart());
        boolean exitResult = userBridge.exit(u);
        assertTrue(exitResult);
    }

    @Test
    public void testCheckCartEmpty() {
        User u = enter();
        ShoppingCart userCart = userBridge.checkCart(u.ID);
        assertEquals(0,userCart.numOfProductsInCart());
    }

    @Test
    public void testCheckCartNotEmpty() {
        User u = enter();
        userBridge.addProductToCart(u.ID,shops[castro_ID].ID,2,10);
        userBridge.addProductToCart(u.ID,shops[ACE_ID].ID,0,10);
        ShoppingCart userCart = userBridge.checkCart(u.ID);
        assertEquals(2,userCart.numOfProductsInCart());

        int boughtFrom1 = userCart.getShop(0);
        int product1 = userCart.getProduct(0);
        int quantity1 = userCart.getQuantity(0);

        assertEquals(2,boughtFrom1);
        assertEquals(2,product1);
        assertEquals(10,quantity1);

        int boughtFrom2 = userCart.getShop(0);
        int product2 = userCart.getProduct(0);
        int quantity2 = userCart.getQuantity(0);

        assertEquals(0,boughtFrom2);
        assertEquals(0,product2);
        assertEquals(10,quantity2);

        boolean exitResult = userBridge.exit(u);
        assertTrue(exitResult);
    }

    @Test
    public void testUpdateCartSuccessIncreaseQuantity() {
        User u = enter();
        userBridge.addProductToCart(u.ID,shops[castro_ID].ID,345,10);
        userBridge.updateCart(u.ID,new int[]{345},new int[]{2},new int[]{92});
        ShoppingCart userCart = userBridge.checkCart(u.ID);

        int boughtFrom1 = userCart.getShop(0);
        int product1 = userCart.getProduct(0);
        int quantity1 = userCart.getQuantity(0);

        assertEquals(2,boughtFrom1);
        assertEquals(345,product1);
        assertEquals(92,quantity1);

        boolean exitResult = userBridge.exit(u);
        assertTrue(exitResult);
    }

    @Test
    public void testUpdateCartSuccessDecreaseQuantity() {
        User u = enter();
        userBridge.addProductToCart(u.ID,shops[castro_ID].ID,345,10);
        userBridge.updateCart(u.ID,new int[]{345},new int[]{2},new int[]{2});
        ShoppingCart userCart = userBridge.checkCart(u.ID);

        int boughtFrom1 = userCart.getShop(0);
        int product1 = userCart.getProduct(0);
        int quantity1 = userCart.getQuantity(0);

        assertEquals(2,boughtFrom1);
        assertEquals(345,product1);
        assertEquals(2,quantity1);

        boolean exitResult = userBridge.exit(u);
        assertTrue(exitResult);
    }

    @Test
    public void testUpdateCartSuccessRemoveProduct() {
        User u = enter();
        userBridge.addProductToCart(u.ID,shops[castro_ID].ID,345,10);
        userBridge.updateCart(u.ID,new int[]{345},new int[]{2},new int[]{0});
        ShoppingCart userCart = userBridge.checkCart(u.ID);

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

        int boughtFrom1 = userCart.getShop(0);
        int product1 = userCart.getProduct(0);
        int quantity1 = userCart.getQuantity(0);

        assertEquals(2,boughtFrom1);
        assertEquals(345,product1);
        assertEquals(10,quantity1);

        boolean exitResult = userBridge.exit(u);
        assertTrue(exitResult);
    }

    @Test
    public void testUpdateCartFailureDecreaseQuantity() {
        User u = enter();
        userBridge.addProductToCart(u.ID,shops[castro_ID].ID,345,10);
        userBridge.updateCart(u.ID,new int[]{345},new int[]{2},new int[]{-2});
        ShoppingCart userCart = userBridge.checkCart(u.ID);

        int boughtFrom1 = userCart.getShop(0);
        int product1 = userCart.getProduct(0);
        int quantity1 = userCart.getQuantity(0);

        assertEquals(2,boughtFrom1);
        assertEquals(345,product1);
        assertEquals(10,quantity1);

        boolean exitResult = userBridge.exit(u);
        assertTrue(exitResult);
    }

    @Test
    public void testUpdateCartFailureProductNotInCart() {
        User u = enter();
        userBridge.addProductToCart(u.ID,shops[castro_ID].ID,345,10);
        userBridge.updateCart(u.ID,new int[]{1},new int[]{0},new int[]{50});
        ShoppingCart userCart = userBridge.checkCart(u.ID);

        int boughtFrom1 = userCart.getShop(0);
        int product1 = userCart.getProduct(0);
        int quantity1 = userCart.getQuantity(0);

        assertEquals(2,boughtFrom1);
        assertEquals(345,product1);
        assertEquals(10,quantity1);

        boolean exitResult = userBridge.exit(u);
        assertTrue(exitResult);
    }

    // Changes system state
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
        assertFalse(purchaseResult);
        assertEquals(1,u.numOfNotifications());

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
