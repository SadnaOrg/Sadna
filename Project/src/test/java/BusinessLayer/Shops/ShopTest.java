package BusinessLayer.Shops;

import BusinessLayer.Products.Product;
import BusinessLayer.Products.ProductFilters;
import BusinessLayer.Products.Users.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ShopTest {

    private final SubscribedUser founder = new SubscribedUser("Founder Guy","Guy123456");
    private Shop s1;
    private Product p1;
    private Product p2;

    @Before
    public void setUp() {
        s1 = new Shop(100, "shop","testing shop", founder);
        p1 = new Product(1, "a", 5, 100);
        p2 = new Product(2, "c", 15, 500);
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
        ProductFilters productFilters = null;
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
        User u1 = new Guest("Guy");
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
        User u1 = new Guest("Guy");
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
        User u1 = new Guest("Guy");
        Basket b1 = new Basket(s1.getId());
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
        User u1 = new Guest("Guy");
        Basket b1 = new Basket(s1.getId());
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
        SubscribedUser subscribedUser =new SubscribedUser("Guy","meirMaor");
        ShopAdministrator sa1 = new ShopAdministrator(s1,subscribedUser,s1.getFounder().getUserName());
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
        Product p3 = new Product(1, "fake", 50, 1000);
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

    @Test
    public void changeProductAndChangeItAgainAndAnotherOne() {
        s1.addProduct(p1);
        Assert.assertEquals(1, s1.getProducts().size());
        s1.addProduct(p2);
        Assert.assertEquals(2, s1.getProducts().size());
        Assert.assertEquals(p1.getName(), s1.getProducts().get(p1.getID()).getName());
        Assert.assertNotEquals(p1.getName(), s1.getProducts().get(p2.getID()).getName());
        Assert.assertEquals(p2.getName(), s1.getProducts().get(p2.getID()).getName());

        Product new_product= new Product(1 , "Meir", 222,2223);
        s1.changeProduct(new_product);
        Assert.assertEquals(2, s1.getProducts().size());
        Assert.assertEquals(new_product.getName(), s1.getProducts().get(p1.getID()).getName());
        Assert.assertEquals(p1.getName(), s1.getProducts().get(new_product.getID()).getName());
        Assert.assertEquals(p2.getName(), s1.getProducts().get(p2.getID()).getName());

        Product new_product2= new Product(1 , "maouer", 4222,22523);
        s1.changeProduct(new_product2);
        Assert.assertEquals(2, s1.getProducts().size());
        Assert.assertEquals(new_product2.getName(), s1.getProducts().get(new_product.getID()).getName());
        Assert.assertNotEquals(new_product.getName(), s1.getProducts().get(new_product2.getID()).getName());
        Assert.assertEquals(p2.getName(), s1.getProducts().get(p2.getID()).getName());

        Product new_product3= new Product(2 , "wello", 32,32);
        s1.changeProduct(new_product3);
        Assert.assertEquals(2, s1.getProducts().size());
        Assert.assertEquals(new_product2.getName(), s1.getProducts().get(new_product.getID()).getName());
        Assert.assertNotEquals(new_product3.getName(), s1.getProducts().get(new_product2.getID()).getName());
        Assert.assertEquals(new_product3.getName(), s1.getProducts().get(p2.getID()).getName());

    }

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

        Product new_product= new Product(1 , "Meir", 222,2223);
        s1.changeProduct(new_product);
        Assert.assertEquals(1, s1.getProducts().size());
        Assert.assertEquals(new_product.getName(), s1.getProducts().get(p1.getID()).getName());
        Assert.assertEquals(p1.getName(), s1.getProducts().get(new_product.getID()).getName());
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
        Product p3 = new Product(32, "fake", 50, 1000);
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
        User u1 = new Guest("Yuval");
        Basket b = new Basket(s1.getId());
        b.saveProducts(p1.getID(),10,p1.getPrice());
        b.saveProducts(p2.getID(),50,p1.getPrice());
        s1.addBasket(u1.getName(), b);
        double price =s1.purchaseBasket(u1.getName());
        Assert.assertEquals(price, 5.0*10+15.0*50, 0.0);
    }


    @Test
    public void purchaseBasketOneOfTheProductsNotInShop() {
        s1.addProduct(p1);
        s1.addProduct(p2);
        Product p3 = new Product(32, "fake", 50, 1000);
        User u1 = new Guest("Yuval");
        Basket b = new Basket(s1.getId());
        b.saveProducts(p1.getID(),10,p1.getPrice());
        b.saveProducts(p2.getID(),50,p2.getPrice());
        b.saveProducts(p3.getID(),51,p3.getPrice());
        s1.addBasket(u1.getName(), b);
        try {
            double price = s1.purchaseBasket(u1.getName());
            fail("shouldn't end here p3 not in s1");
        }
        catch (IllegalStateException e) {
            Assert.assertEquals(e.getMessage(), "The product is not in the shop");
            Assert.assertEquals(s1.getUsersBaskets().get(u1.getName()), b);
            Assert.assertEquals(3,b.getProducts().size());
            Assert.assertEquals(s1.getUsersBaskets().get(u1.getName()).getProducts().size(),b.getProducts().size());

        }
    }

    @Test
    public void purchaseBasketOneOfTheProductsOutOfStock() {
        s1.addProduct(p1);
        s1.addProduct(p2);
        Product p3 = new Product(32, "fake", 50, 10);
        s1.addProduct(p3);
        User u1 = new Guest("Yuval");
        Basket b = new Basket(s1.getId());
        b.saveProducts(p1.getID(),10, p1.getPrice());
        b.saveProducts(p2.getID(),50, p2.getPrice());
        b.saveProducts(p3.getID(),51, p3.getPrice());
        s1.addBasket(u1.getName(), b);
        try {
            double price = s1.purchaseBasket(u1.getName());
            fail("shouldn't end here p3 not in s1");
        }
        catch (IllegalStateException e) {
            Assert.assertEquals(e.getMessage(), "Try to buy out of stock product from the shop");
            Assert.assertEquals(s1.getUsersBaskets().get(u1.getName()), b);
            Assert.assertEquals(3,b.getProducts().size());
            Assert.assertEquals(s1.getUsersBaskets().get(u1.getName()).getProducts().size(),b.getProducts().size());

        }
    }

    @Test
    public void checkIfUserHasBasketTrueAndFalseCases() {
        s1.addProduct(p1);
        s1.addProduct(p2);
        User u1 = new Guest("Yuval");
        User u2 = new Guest("Meir");
        Basket b = new Basket(s1.getId());
        b.saveProducts(p1.getID(),10, p1.getPrice());
        b.saveProducts(p2.getID(),50, p2.getPrice());
        s1.addBasket(u1.getName(), b);
        Assert.assertTrue(s1.checkIfUserHasBasket(u1.getName()));
        Assert.assertFalse(s1.checkIfUserHasBasket(u2.getName()));
    }

    @Test
    public void addBasketTrueAndFalseCases() {
        s1.addProduct(p1);
        s1.addProduct(p2);
        User u1 = new Guest("Yuval");
        User u2 = new Guest("Meir");
        Basket b1 = new Basket(s1.getId());
        Basket b2 = new Basket(s1.getId());
        b1.saveProducts(p1.getID(),10, p1.getPrice());
        b1.saveProducts(p2.getID(),50, p2.getPrice());
        s1.addBasket(u1.getName(), b1);
        b2.saveProducts(p1.getID(),10, p1.getPrice());
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
        SubscribedUser u1 = new SubscribedUser("Meir","Maor");
        ShopAdministrator sa1 = new ShopAdministrator(s1,u1,s1.getFounder().getUserName());
        Assert.assertEquals(1, s1.getShopAdministrators().size());
        Assert.assertTrue(s1.addAdministrator(sa1.getUserName(),sa1));
        Assert.assertEquals(2, s1.getShopAdministrators().size());
        Assert.assertFalse(s1.addAdministrator(sa1.getUserName(),sa1));
        Assert.assertEquals(2, s1.getShopAdministrators().size());
    }

}
