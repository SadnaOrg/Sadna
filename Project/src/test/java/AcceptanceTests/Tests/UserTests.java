package AcceptanceTests.Tests;

import AcceptanceTests.Bridge.SubscribedUserBridge;
import AcceptanceTests.Bridge.SubscribedUserProxy;
import AcceptanceTests.Bridge.UserProxy;
import AcceptanceTests.DataObjects.*;
import org.junit.*;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;


public abstract class UserTests extends ProjectTests {
    protected User u;

    @BeforeClass
    public static void setUp(){
        setUserBridge(new UserProxy());
        ProjectTests.setUpTests();
    }

    @AfterClass
    public static void restore(){
        SubscribedUserBridge b = new SubscribedUserProxy((UserProxy) getUserBridge());
        Guest g = b.visit();
        castroFounder = b.login(g.name,new RegistrationInfo("castroFounder","castro_rocks"));
        b.updateProductQuantity(castroFounder.name,shops[castro_ID].ID,2,30);
        b.updateProductQuantity(castroFounder.name,shops[castro_ID].ID,45,40);
        b.exit(castroFounder.name);
    }

    @Before
    public void setUpTest(){
        u = enter();
    }

    @After
    public void tearDown(){
        userBridge.updateCart(u.name,2,shops[castro_ID].ID,0);
        userBridge.updateCart(u.name,0,shops[ACE_ID].ID,0);
        userBridge.exit(u.name);
    }

    @Test
    public void testVisitSingleGuest() {
        Guest newGuest = userBridge.visit();
        String guestName = newGuest.getName();
        assertNotNull(guestName);

        ShoppingCart cart = userBridge.checkCart(guestName);
        assertNotNull(cart);
        assertEquals(0,cart.numOfProductsInCart());
        assertEquals(0,cart.getNumberOfBaskets());

        boolean exit = userBridge.exit(newGuest.name);
        assertTrue(exit);
    }

    @Test
    public void testVisitTwoGuests() {
        Guest guest1 = userBridge.visit();
        String guest1Name = guest1.getName();
        assertNotNull(guest1Name);

        Guest guest2 = userBridge.visit();
        String guest2Name = guest2.getName();
        assertNotNull(guest2Name);

        assertNotSame(guest1Name, guest2Name);

        boolean exit1 = userBridge.exit(guest1Name);
        boolean exit2 = userBridge.exit(guest2Name);
        assertTrue(exit1);
        assertTrue(exit2);
    }


    // this is a set-up methods for tests that need a user object
    public abstract User enter();

    @Test
    public void testGetShopsInfoByNameSuccess() {
        List<Integer> expected = new LinkedList<>();
        expected.add(shops[ACE_ID].ID);
        filterSearchShopSuccess(u.name,shopFilters[NAME_FILTER],expected);
    }

    @Test
    public void testGetShopsInfoByNameFailure() {
        filterSearchShopFailure(u.name,shopFailFilters[NAMEF_FILTER]);
    }

    @Test
    public void testGetShopsInfoByDescriptionSuccess() {
        List<Integer> expected = new LinkedList<>();
        expected.add(shops[castro_ID].ID);
        filterSearchShopSuccess(u.name,shopFilters[DESC_FILTER],expected);
    }

    @Test
    public void testGetShopsInfoByDescriptionFailure() {
        filterSearchShopFailure(u.name,shopFailFilters[DESCF_FILTER]);
    }

//    @Test
//    public void testGetProductsInfoByRatingSuccess() {
//        List<ProductInShop> expected = new LinkedList<>();
//        expected.add(new ProductInShop(345,1,100,25,4.2,new Product("jeans","china")));
//        expected.add(new ProductInShop(1,0,100,35,4.2,new Product("office chair","china")));
//        expected.add(new ProductInShop(31,2,100,55,4.2,new Product("dumbbell" ,"china")));
//        filterSearchProductSuccess(productFilters[PRODUCT_RATING_FILTER],expected);
//    }
//
//    @Test
//    public void testGetProductsInfoByRatingFailure() {
//        filterSearchProductFailure(productFailFilters[PRODUCT_RATINGF_FILTER]);
//    }

    @Test
    public void testGetProductsInfoByManufacturerSuccess() {
        List<ProductInShop> expected = new LinkedList<>();
        expected.add(new ProductInShop(0,shops[ACE_ID].ID,30,20,new Product("lamp","good","israel")));
        filterSearchProductSuccess(u.name,productFilters[MANUFACTURER_FILTER],expected);
    }

    @Test
    public void testGetProductsInfoByManufacturerFailure() {
        filterSearchProductFailure(u.name,productFailFilters[MANUFACTURERF_FILTER]);
    }

//    @Test
//    public void testSearchShopFilteredSuccess(){
//        List<ProductInShop> products = userBridge.filterShopProducts(0,productFilters[RATING_FILTER]);
//        assertEquals(1, products.size());
//        assertEquals(1,products.get(0).ID);
//    }
//
//    @Test
//    public void testSearchShopFilteredFailure(){
//        List<ProductInShop> products = userBridge.filterShopProducts(0,productFailFilters[RATINGF_FILTER]);
//        assertEquals(0, products.size());
//    }

    @Test
    public void testSearchShopProductsSuccess() {
        List<ProductInShop> products = userBridge.searchShopProducts(u.name,shops[ACE_ID].ID);
        assertNotNull(products);
        List<Integer> ids = products.stream().map(productInShop -> productInShop.ID).toList();
        List<Shop> shopsSearch = userBridge.getShopsInfo(u.name, new ShopFilter() {
            @Override
            public boolean filter(Shop shop) {
                return shop.ID == UserTests.shops[ACE_ID].ID;
            }
        });
        assertEquals(1,shopsSearch.size());
        shops[ACE_ID] = shopsSearch.get(0);
        List<Integer> realIDs = shops[ACE_ID].products.keySet().stream().toList();
        assertEquals(shops[ACE_ID].products.size(), products.size());
        assertTrue(ids.containsAll(realIDs));
    }

    @Test
    public void testSearchShopProductsFailureNoSuchShop() {
        List<ProductInShop> products = userBridge.searchShopProducts(u.name,10000);
        assertNull(products);
    }

    @Test
    public void testSearchProductsSuccess() {
        ProductFilter filter = productFilters[MANUFACTURER_FILTER];
        List<ProductInShop> products = userBridge.searchProducts(u.name,filter);
        for (ProductInShop p:
                products) {
            assertTrue(filter.filter(p));
        }
    }

    @Test
    public void testSearchProductByDescriptionSuccess(){
        ProductFilter filter = productFilters[PRODUCT_DESC_FILTER];
        List<ProductInShop> expected = new LinkedList<>();
        Product p = new Product("desk","newest edition","china");
        ProductInShop productInShop = new ProductInShop(2,shops[ACE_ID].ID,30,20,p);
        expected.add(productInShop);
        List<ProductInShop> products = userBridge.searchProducts(u.name,filter);
        assertProducts(products,filter,expected);
    }

    @Test
    public void testSearchProductByDescriptionFailure(){
        ProductFilter filter = productFailFilters[PRODUCT_DESCF_FILTER];
        List<ProductInShop> products = userBridge.searchProducts(u.name,filter);
        assertNull(products);
    }

    @Test
    public void testAddProductToCartSuccess() {
        boolean added = userBridge.addProductToCart(u.name,shops[castro_ID].ID,2,10);
        assertTrue(added);

        ShoppingCart addResult = userBridge.checkCart(u.name);
        assertNotNull(addResult);

        ShopBasket basket = addResult.getShopBasket(shops[castro_ID].ID);
        assertNotNull(basket);

        int product_quantity = basket.getProductQuantity(2);

        assertEquals(10,product_quantity);
        assertEquals(10,addResult.numOfProductsInCart());
        assertEquals(1,addResult.getNumberOfBaskets());
    }

    @Test
    public void testAddProductToCartFailureTooLargeQuantity() {
        boolean added = userBridge.addProductToCart(u.name,shops[castro_ID].ID,2,100);
        assertTrue(added);

        ShoppingCart addResult = userBridge.checkCart(u.name);
        assertNotNull(addResult);

        assertEquals(100,addResult.numOfProductsInCart());
        assertEquals(1,addResult.getNumberOfBaskets());
    }

    @Test
    public void testAddProductToCartFailureNegativeQuantity() {
        boolean added = userBridge.addProductToCart(u.name,shops[castro_ID].ID,2,-1);
        assertFalse(added);

        ShoppingCart addResult = userBridge.checkCart(u.name);
        assertNotNull(addResult);
        assertEquals(0,addResult.numOfProductsInCart());
        assertEquals(0,addResult.getNumberOfBaskets());
    }

    @Test
    public void testAddProductToCartFailureProductNotInShop() {
        boolean added = userBridge.addProductToCart(u.name,shops[castro_ID].ID,13,10);
        assertFalse(added);

        ShoppingCart addResult = userBridge.checkCart(u.name);
        assertNotNull(addResult);
        assertEquals(0,addResult.numOfProductsInCart());
        assertEquals(0,addResult.getNumberOfBaskets());
    }

    @Test
    public void testCheckCartEmpty() {
        ShoppingCart userCart = userBridge.checkCart(u.name);
        assertNotNull(userCart);
        assertEquals(0,userCart.numOfProductsInCart());
        assertEquals(0,userCart.getNumberOfBaskets());
    }

    @Test
    public void testCheckCartNotEmpty() {
        boolean added1 = userBridge.addProductToCart(u.name,shops[castro_ID].ID,2,10);
        boolean added2 = userBridge.addProductToCart(u.name,shops[ACE_ID].ID,0,10);
        assertTrue(added1 && added2);

        ShoppingCart userCart = userBridge.checkCart(u.name);
        assertNotNull(userCart);

        ShopBasket basket1 = userCart.getShopBasket(shops[castro_ID].ID);
        assertNotNull(basket1);

        int product1Quantity = basket1.getProductQuantity(2);

        assertNotNull(basket1);
        assertEquals(20,userCart.numOfProductsInCart());
        assertEquals(2,userCart.getNumberOfBaskets());

        assertEquals(10,product1Quantity);

        ShopBasket basket2 = userCart.getShopBasket(shops[ACE_ID].ID);
        assertNotNull(basket2);

        int product2Quantity = basket2.getProductQuantity(0);

        assertEquals(10,product2Quantity);

        userBridge.updateCart(u.name,2,shops[castro_ID].ID,0);
        userBridge.updateCart(u.name,0,shops[ACE_ID].ID,0);
        userCart = userBridge.checkCart(u.name);
        assertEquals(0,userCart.getNumberOfBaskets());
    }

    @Test
    public void testUpdateCartSuccessIncreaseQuantity() {
        testAddProductToCartSuccess();
        boolean updated = userBridge.updateCart(u.name,2,shops[castro_ID].ID,20);
        assertTrue(updated);

        ShoppingCart userCart = userBridge.checkCart(u.name);
        assertNotNull(userCart);

        ShopBasket basket = userCart.getShopBasket(shops[castro_ID].ID);
        assertNotNull(basket);

        int productQuantity = basket.getProductQuantity(2);

        assertEquals(20,productQuantity);
        assertEquals(20,userCart.numOfProductsInCart());
        assertEquals(1,userCart.getNumberOfBaskets());
    }

    @Test
    public void testUpdateCartSuccessDecreaseQuantity() {
        testAddProductToCartSuccess();

        boolean decreased = userBridge.updateCart(u.name,2,shops[castro_ID].ID,2);
        assertTrue(decreased);

        ShoppingCart userCart = userBridge.checkCart(u.name);
        assertNotNull(userCart);
        assertEquals(2,userCart.numOfProductsInCart());
        assertEquals(1,userCart.getNumberOfBaskets());

        ShopBasket basket = userCart.getShopBasket(shops[castro_ID].ID);
        assertNotNull(basket);

        int quantity = basket.getProductQuantity(2);

        assertEquals(2,quantity);
    }

    @Test
    public void testUpdateCartSuccessRemoveProduct() {
        testAddProductToCartSuccess();
        boolean removed = userBridge.updateCart(u.name,2,shops[castro_ID].ID,0);
        assertTrue(removed);

        ShoppingCart userCart = userBridge.checkCart(u.name);
        assertNotNull(userCart);

        int numOfProducts = userCart.numOfProductsInCart();
        assertEquals(0,numOfProducts);

        boolean exitResult = userBridge.exit(u.name);
        assertTrue(exitResult);
    }

    @Test
    public void testUpdateCartFailureIncreaseQuantity() {
        testAddProductToCartSuccess();

        boolean updated = userBridge.updateCart(u.name,2,shops[castro_ID].ID,100);
        assertTrue(updated);

        ShoppingCart userCart = userBridge.checkCart(u.name);
        assertNotNull(userCart);
        assertEquals(1,userCart.getNumberOfBaskets());
        assertEquals(100,userCart.numOfProductsInCart());

        ShopBasket basket = userCart.getShopBasket(shops[castro_ID].ID);
        assertNotNull(basket);
        int quantity = basket.getProductQuantity(2);

        assertEquals(100,quantity);

        double bought = userBridge.purchaseCart(u.name,"4800470023456848", 674, 7, 2025);
        assertEquals(0.0,bought,0);
    }

    @Test
    public void testUpdateCartFailureDecreaseQuantity() {
        testAddProductToCartSuccess();

        boolean updated = userBridge.updateCart(u.name,2,shops[castro_ID].ID,-2);
        assertFalse(updated);

        ShoppingCart userCart = userBridge.checkCart(u.name);
        assertNotNull(userCart);
        assertEquals(1,userCart.getNumberOfBaskets());
        assertEquals(10,userCart.numOfProductsInCart());

        ShopBasket basket = userCart.getShopBasket(shops[castro_ID].ID);
        assertNotNull(basket);
        int quantity = basket.getProductQuantity(2);

        assertEquals(10,quantity);
    }

    @Test
    public void testUpdateCartFailureProductNotInCart() {
        boolean updated = userBridge.updateCart(u.name,1,shops[ACE_ID].ID,50);
        assertFalse(updated);

        ShoppingCart userCart = userBridge.checkCart(u.name);
        assertNotNull(userCart);
        assertEquals(0,userCart.getNumberOfBaskets());
        assertEquals(0,userCart.numOfProductsInCart());
    }

    @Test
    public void testPurchaseEmptyCart() {
        double purchaseResult = userBridge.purchaseCart(u.name,"4580476511112222", 694, 9, 22);
        assertEquals(0.0,purchaseResult,0);
        //assertEquals(0,u.numOfNotifications());
    }

    @Test
    public void testPurchaseNotEmptyCart() {
        testAddProductToCartSuccess();

        double purchaseResult = userBridge.purchaseCart(u.name, "4800470023456848", 674, 7, 2025);
        assertNotEquals(0.0,purchaseResult,0);

        //assertEquals(1,u.numOfNotifications());
        //assertEquals("Purchase",u.notifications.get(0));

        ShoppingCart userCart = userBridge.checkCart(u.name);
        assertNotNull(userCart);

        int numOfProducts = userCart.numOfProductsInCart();
        assertEquals(0,numOfProducts);
        assertEquals(0,userCart.getNumberOfBaskets());
    }

    @Test
    public void testPurchaseCartFailurePaymentFailedBadCreditCard(){
        testAddProductToCartSuccess();

        double purchased = userBridge.purchaseCart(u.name,"11110000av",965,12,2025);
        assertEquals(0.0,purchased,0);
    }

    @Test
    public void testPurchaseCartFailurePaymentFailedBadCVV(){
        testAddProductToCartSuccess();

        double purchased = userBridge.purchaseCart(u.name,"4800470023456848",-15,12,2025);
        assertEquals(0.0,purchased,0);
    }

    @Test
    public void testPurchaseCartFailurePaymentFailedBadExpirationDate(){
        testAddProductToCartSuccess();

        double purchased = userBridge.purchaseCart(u.name,"4800470023456848",15,13,3);
        assertEquals(0.0,purchased,0);
    }

    private void assertShops(List<Shop> shops, ShopFilter filter,List<Integer> expected){
        for (Shop s:
             shops) {
            assertTrue(filter.filter(s));
        }
        List<Integer> shopsIDs = shops.stream().map(shop -> shop.ID).toList();
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

    private void filterSearchShopSuccess(String name,ShopFilter filter,List<Integer> expected){
        List<Shop> shops = userBridge.getShopsInfo(name,filter);
        assertNotNull(shops);
        assertShops(shops,filter,expected);
    }

    private void filterSearchShopFailure(String name,ShopFilter filter){
        List<Shop> shops = userBridge.getShopsInfo(name,filter);
        assertNull(shops);
    }

    private void filterSearchProductSuccess(String name,ProductFilter filter,List<ProductInShop> expected){
        List<ProductInShop> products = userBridge.searchProducts(name,filter);
        assertNotNull(products);
        assertProducts(products,filter,expected);
    }

    private void filterSearchProductFailure(String name,ProductFilter filter){
        List<ProductInShop> products = userBridge.searchProducts(name,filter);
        assertNull(products);
    }
}