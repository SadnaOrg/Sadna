package AcceptanceTests.Tests;

import AcceptanceTests.Bridge.UserProxy;
import org.junit.BeforeClass;
import org.junit.Test;


import AcceptanceTests.DataObjects.*;
import AcceptanceTests.Threads.ACEFounderPurchase;
import AcceptanceTests.Threads.MegaSportFounderPurchase;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;


public abstract class UserTests extends ProjectTests {

    @BeforeClass
    public static void setUp(){
        setUserBridge(new UserProxy());
        setUpTests();
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
    }

    @Test
    public void testVisitTwoGuests() {
        Guest guest1 = userBridge.visit();
        String guest1Name = guest1.getName();
        assertNotNull(guest1Name);

        Guest guest2 = userBridge.visit();
        String guest2Name = guest1.getName();
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
        expected.add(0);
        filterSearchShopSuccess(shopFilters[NAME_FILTER],expected);
    }

    @Test
    public void testGetShopsInfoByNameFailure() {
        filterSearchShopFailure(shopFilters[NAMEF_FILTER]);
    }

    @Test
    public void testGetShopsInfoByDescriptionSuccess() {
        List<Integer> expected = new LinkedList<>();
        expected.add(2);
        filterSearchShopSuccess(shopFilters[DESC_FILTER],expected);
    }

    @Test
    public void testGetShopsInfoByDescriptionFailure() {
        filterSearchShopFailure(shopFilters[DESC_FILTER]);
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
        expected.add(new ProductInShop(0,0,30,20,3,new Product("lamp","good","israel")));
        filterSearchProductSuccess(productFilters[MANUFACTURER_FILTER],expected);
    }

    @Test
    public void testGetProductsInfoByManufacturerFailure() {
        filterSearchProductFailure(productFailFilters[MANUFACTURERF_FILTER]);
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
        List<ProductInShop> products = userBridge.searchShopProducts(shops[ACE_ID].ID);
        assertNotNull(products);
        assertSame(products, shops[ACE_ID].products);
    }

    @Test
    public void testSearchShopProductsFailureNoSuchShop() {
        List<ProductInShop> products = userBridge.searchShopProducts(10000);
        assertNull(products);
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
         boolean added = userBridge.addProductToCart(u.name,shops[castro_ID].ID,2,10);
         assertTrue(added);

        ShoppingCart addResult = userBridge.checkCart(u.name);
        assertNotNull(addResult);

        ShopBasket basket = addResult.getShopBasket(castro_ID);
        assertNotNull(basket);

        int product_quantity = basket.getProductQuantity(2);

        assertEquals(10,product_quantity);
        assertEquals(10,addResult.numOfProductsInCart());
        assertEquals(1,addResult.getNumberOfBaskets());

        //cancel side-effects
        userBridge.updateCart(u.name,2,shops[castro_ID].ID,0);

        boolean exitResult = userBridge.exit(u.name);
        assertTrue(exitResult);
    }

    @Test
    public void testAddProductToCartFailureTooLargeQuantity() {
        User u = enter();
        boolean added = userBridge.addProductToCart(u.name,shops[castro_ID].ID,2,100);
        assertFalse(added);

        ShoppingCart addResult = userBridge.checkCart(u.name);
        assertNotNull(addResult);

        assertEquals(0,addResult.numOfProductsInCart());
        assertEquals(0,addResult.getNumberOfBaskets());
        boolean exitResult = userBridge.exit(u.name);
        assertTrue(exitResult);
    }

    @Test
    public void testAddProductToCartFailureNegativeQuantity() {
        User u = enter();
        boolean added = userBridge.addProductToCart(u.name,shops[castro_ID].ID,2,-1);
        assertFalse(added);

        ShoppingCart addResult = userBridge.checkCart(u.name);
        assertNotNull(addResult);
        assertEquals(0,addResult.numOfProductsInCart());
        assertEquals(0,addResult.getNumberOfBaskets());
        boolean exitResult = userBridge.exit(u.name);
        assertTrue(exitResult);
    }

    @Test
    public void testAddProductToCartFailureProductNotInShop() {
        User u = enter();
        boolean added = userBridge.addProductToCart(u.name,shops[castro_ID].ID,13,10);
        assertFalse(added);

        ShoppingCart addResult = userBridge.checkCart(u.name);
        assertNotNull(addResult);
        assertEquals(0,addResult.numOfProductsInCart());
        assertEquals(0,addResult.getNumberOfBaskets());
        boolean exitResult = userBridge.exit(u.name);
        assertTrue(exitResult);
    }

    @Test
    public void testCheckCartEmpty() {
        User u = enter();
        ShoppingCart userCart = userBridge.checkCart(u.name);
        assertNotNull(userCart);
        assertEquals(0,userCart.numOfProductsInCart());
        assertEquals(0,userCart.getNumberOfBaskets());

        boolean exitResult = userBridge.exit(u.name);
        assertTrue(exitResult);
    }

    @Test
    public void testCheckCartNotEmpty() {
        User u = enter();
        boolean added1 = userBridge.addProductToCart(u.name,shops[castro_ID].ID,2,10);
        boolean added2 = userBridge.addProductToCart(u.name,shops[ACE_ID].ID,0,10);
        assertTrue(added1 && added2);

        ShoppingCart userCart = userBridge.checkCart(u.name);
        assertNotNull(userCart);
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
        userBridge.updateCart(u.name,2,shops[castro_ID].ID,0);
        userBridge.updateCart(u.name,0,shops[ACE_ID].ID,0);

        boolean exitResult = userBridge.exit(u.name);
        assertTrue(exitResult);
    }

    @Test
    public void testUpdateCartSuccessIncreaseQuantity() {
        User u = enter();
        boolean added = userBridge.addProductToCart(u.name,shops[castro_ID].ID,345,10);
        boolean updated = userBridge.updateCart(u.name,345,2,92);
        assertTrue(added && updated);

        ShoppingCart userCart = userBridge.checkCart(u.name);
        assertNotNull(userCart);

        ShopBasket basket = userCart.getShopBasket(shops[castro_ID].ID);
        assertNotNull(basket);

        int productQuantity = basket.getProductQuantity(345);

        assertEquals(92,productQuantity);
        assertEquals(92,userCart.numOfProductsInCart());
        assertEquals(1,userCart.getNumberOfBaskets());

        //cancel side-effects
        userBridge.updateCart(u.name,345,shops[castro_ID].ID,0);

        boolean exitResult = userBridge.exit(u.name);
        assertTrue(exitResult);
    }

    @Test
    public void testUpdateCartSuccessDecreaseQuantity() {
        User u = enter();
        boolean added = userBridge.addProductToCart(u.name,shops[castro_ID].ID,345,10);
        assertTrue(added);

        boolean decreased = userBridge.updateCart(u.name,345,2,2);
        assertTrue(decreased);

        ShoppingCart userCart = userBridge.checkCart(u.name);
        assertNotNull(userCart);
        assertEquals(2,userCart.numOfProductsInCart());
        assertEquals(1,userCart.getNumberOfBaskets());

        ShopBasket basket = userCart.getShopBasket(shops[castro_ID].ID);
        assertNotNull(basket);

        int quantity = basket.getProductQuantity(345);

        assertEquals(2,quantity);

        //cancel side-effects
        userBridge.updateCart(u.name,345,shops[castro_ID].ID,0);

        boolean exitResult = userBridge.exit(u.name);
        assertTrue(exitResult);
    }

    @Test
    public void testUpdateCartSuccessRemoveProduct() {
        User u = enter();
        boolean added =userBridge.addProductToCart(u.name,shops[castro_ID].ID,345,10);
        assertTrue(added);

        boolean removed = userBridge.updateCart(u.name,345,2,0);
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
        User u = enter();
        boolean added = userBridge.addProductToCart(u.name,shops[castro_ID].ID,345,10);
        assertTrue(added);

        boolean updated = userBridge.updateCart(u.name,345,2,102);
        assertFalse(updated);

        ShoppingCart userCart = userBridge.checkCart(u.name);
        assertNotNull(userCart);
        assertEquals(1,userCart.getNumberOfBaskets());
        assertEquals(10,userCart.numOfProductsInCart());

        ShopBasket basket = userCart.getShopBasket(shops[castro_ID].ID);
        assertNotNull(basket);
        int quantity = basket.getProductQuantity(345);

        assertEquals(10,quantity);

        //cancel side-effects
        userBridge.updateCart(u.name,345,2,0);

        boolean exitResult = userBridge.exit(u.name);
        assertTrue(exitResult);
    }

    @Test
    public void testUpdateCartFailureDecreaseQuantity() {
        User u = enter();
        boolean added = userBridge.addProductToCart(u.name,shops[castro_ID].ID,345,10);
        assertTrue(added);

        boolean updated = userBridge.updateCart(u.name,345,2,-2);
        assertFalse(updated);

        ShoppingCart userCart = userBridge.checkCart(u.name);
        assertNotNull(userCart);
        assertEquals(1,userCart.getNumberOfBaskets());
        assertEquals(10,userCart.numOfProductsInCart());

        ShopBasket basket = userCart.getShopBasket(shops[castro_ID].ID);
        assertNotNull(basket);
        int quantity = basket.getProductQuantity(345);

        assertEquals(10,quantity);

        //cancel side-effects
        userBridge.updateCart(u.name,345,2,0);

        boolean exitResult = userBridge.exit(u.name);
        assertTrue(exitResult);
    }

    @Test
    public void testUpdateCartFailureProductNotInCart() {
        User u = enter();
        boolean added = userBridge.addProductToCart(u.name,shops[castro_ID].ID,345,10);
        assertTrue(added);

        boolean updated = userBridge.updateCart(u.name,1,0,50);
        assertFalse(updated);

        ShoppingCart userCart = userBridge.checkCart(u.name);
        assertNotNull(userCart);
        assertEquals(1,userCart.getNumberOfBaskets());
        assertEquals(10,userCart.numOfProductsInCart());

        ShopBasket basket = userCart.getShopBasket(shops[castro_ID].ID);
        assertNotNull(basket);
        int quantity = basket.getProductQuantity(345);

        assertEquals(10,quantity);

        //cancel side-effects
        userBridge.updateCart(u.name,345,2,0);

        boolean exitResult = userBridge.exit(u.name);
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

        ShoppingCart MegaSportFounderCart = userBridge.checkCart(MegaSportFounder.name);
        ShoppingCart ACEFounderCart = userBridge.checkCart(ACEFounder.name);
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
            userBridge.updateCart(ACEFounder.name,2,shops[castro_ID].ID,0);

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
            userBridge.updateCart(MegaSportFounder.name,2,shops[castro_ID].ID,0);
        }

        boolean exitACE = userBridge.exit(ACEFounder.name);
        boolean exitMegaSport = userBridge.exit(MegaSportFounder.name);
        assertTrue(exitACE);
        assertTrue(exitMegaSport);
    }

    @Test
    public void testPurchaseEmptyCart() {
        User u = enter();
        boolean purchaseResult = userBridge.purchaseCart(u.name,"4580476511112222", 694, 9, 22);
        assertFalse(purchaseResult);
        assertEquals(0,u.numOfNotifications());

        boolean exitResult = userBridge.exit(u.name);
        assertTrue(exitResult);
    }

    @Test
    public void testPurchaseNotEmptyCart() {
        User u = enter();
        boolean added = userBridge.addProductToCart(u.name,shops[ACE_ID].ID,0,10);
        assertTrue(added);

        boolean purchaseResult = userBridge.purchaseCart(u.name, "480470023456848", 674, 7, 11);
        assertTrue(purchaseResult);
        assertEquals(1,u.numOfNotifications());
        assertEquals("Purchase",u.notifications.get(0));

        ShoppingCart userCart = userBridge.checkCart(u.name);
        assertNotNull(userCart);

        int numOfProducts = userCart.numOfProductsInCart();
        assertEquals(0,numOfProducts);

        boolean exitResult = userBridge.exit(u.name);
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