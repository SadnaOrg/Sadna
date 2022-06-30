package com.SadnaORM.SadnaORM.UserTests;

import com.SadnaORM.ShopRepositories.ShopRepository;
import com.SadnaORM.Shops.Product;
import com.SadnaORM.Shops.Shop;
import com.SadnaORM.UserRepositories.BasketRepository;
import com.SadnaORM.UserRepositories.SubscribedUserRepository;
import com.SadnaORM.Users.Basket;
import com.SadnaORM.Users.PaymentMethod;
import com.SadnaORM.Users.SubscribedUser;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class BasketTests {

    @Autowired
    private BasketRepository basketRepository;

    @Autowired
    private SubscribedUserRepository subscribedUserRepository;

    @Autowired
    private ShopRepository shopRepository;

    private static Shop shop;
    private static SubscribedUser user;
    private static Collection<Product> products;
    private static Map<Integer, Integer> basketProducts;

    @BeforeClass
    public static void setUp(){
        user = new SubscribedUser("Michael1","123321password",true,true,null);
        PaymentMethod method = new PaymentMethod("4580111122223333", 655, 2025, 13, user);
        user.setPaymentMethod(method);

        String name = "name";
        String description = "desc";
        shop = new Shop(0, name, description);

        Product p1 = new Product(1, "my first one", "oh really?", "yes, really", 11, 100);
        Product p2 = new Product(2, "nice product", "tcudorp ecin","dwada",11, 13);
        products = List.of(new Product[]{p1, p2});

        basketProducts = new HashMap<>();
        basketProducts.put(1, 12);
        basketProducts.put(2, 2);
    }

    @AfterEach
    public void tearDown(){
        basketRepository.deleteAll();
        subscribedUserRepository.deleteAll();
        shopRepository.deleteAll();
    }

    @Test
    public void testAddBasketFailureNoShopNull(){
        subscribedUserRepository.save(user);
        Basket basket = new Basket(null, user);
        try {
            basketRepository.save(basket);
            fail("foreign key constraint violated!");
        }
        catch (Exception ignored){

        }
    }

    @Test
    public void testAddBasketFailureNoSuchShop(){
        subscribedUserRepository.save(user);
        Basket basket = new Basket(shop, user);
        try {
            basketRepository.save(basket);
            fail("foreign key constraint violated!");
        }
        catch (Exception ignored){

        }
    }

    @Test
    public void testAddBasketFailNoUserNull(){
        shopRepository.save(shop);
        Basket basket = new Basket(shop, null);
        try {
            basketRepository.save(basket);
            fail("foreign key constraint violated!");
        }
        catch (Exception ignored){

        }
    }

    @Test
    public void testAddBasketFailureNoSuchUser(){
        shopRepository.save(shop);
        Basket basket = new Basket(shop,user);
        try {
            basketRepository.save(basket);
            fail("foreign key constraint violated!");
        }
        catch (Exception ignored){

        }
    }

    @Test
    public void testAddBasketFailureNoUserNoShopNull(){
        Basket basket = new Basket(null, null);
        try {
            basketRepository.save(basket);
            fail("foreign key constraint violated!");
        }
        catch (Exception ignored){

        }
    }

    @Test
    public void testAddBasketFailureNoSuchUserNoSuchShop(){
        shopRepository.save(shop);
        subscribedUserRepository.save(user);
        Basket basket = new Basket(shop, user);
        try {
            basketRepository.save(basket);
            fail("foreign key constraint violated!");
        }
        catch (Exception ignored){

        }
    }

    @Test
    public void testAddEmptyBasketSuccess(){
        shopRepository.save(shop);
        subscribedUserRepository.save(user);
        Basket basket = new Basket(shop, user);
        basketRepository.save(basket);
        Optional<Basket> basketFound = basketRepository.findById(new Basket.BasketPKID(basket.getShop(), basket.getUser()));
        assertTrue(basketFound.isPresent());
        Basket myBasket= basketFound.get();
        assertEquals(basket.getUser().getUsername(), myBasket.getUser().getUsername());
        assertEquals(basket.getShop().getId(), myBasket.getShop().getId());
        assertEquals(0, myBasket.getProducts().size());
    }

    @Test
    public void testRemoveBasket(){
        testAddEmptyBasketSuccess();
        Optional<Basket> basketFound = basketRepository.findById(new Basket.BasketPKID(shop, user));
        assertTrue(basketFound.isPresent());
        basketRepository.delete(basketFound.get());
        Optional<Basket> basketNotFound = basketRepository.findById(new Basket.BasketPKID(shop, user));
        assertFalse(basketNotFound.isPresent());
    }

    @Test
    public void testCascadeRemoveShop(){
        testAddEmptyBasketSuccess();
        Optional<Basket> basketFound = basketRepository.findById(new Basket.BasketPKID(shop, user));
        assertTrue(basketFound.isPresent());
        shopRepository.delete(shop);
        Optional<Basket> basketNotFound = basketRepository.findById(new Basket.BasketPKID(shop, user));
        assertFalse(basketNotFound.isPresent());
    }

    @Test
    public void testCascadeRemoveUser(){
        testAddEmptyBasketSuccess();
        Optional<Basket> basketFound = basketRepository.findById(new Basket.BasketPKID(shop, user));
        assertTrue(basketFound.isPresent());
        subscribedUserRepository.delete(user);
        Optional<Basket> basketNotFound = basketRepository.findById(new Basket.BasketPKID(shop, user));
        assertFalse(basketNotFound.isPresent());
    }

    @Test
    public void testAddBasketWithProducts() {
        testAddEmptyBasketSuccess();
        Optional<Basket> basketFound = basketRepository.findById(new Basket.BasketPKID(shop, user));
        assertTrue(basketFound.isPresent());
        Basket myBasket = basketFound.get();
        myBasket.setProducts(basketProducts);
        basketRepository.save(myBasket);
        basketFound = basketRepository.findById(new Basket.BasketPKID(shop, user));
        assertTrue(basketFound.isPresent());
        myBasket = basketFound.get();
        Map<Integer,Integer> myBasketProducts = myBasket.getProducts();
        assertEquals(2, myBasketProducts.size());
        assertEquals(12, myBasketProducts.get(1));
        assertEquals(2, myBasketProducts.get(2));
    }
}
