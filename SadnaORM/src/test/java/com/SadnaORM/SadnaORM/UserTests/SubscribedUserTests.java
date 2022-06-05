package com.SadnaORM.SadnaORM.UserTests;

import com.SadnaORM.ShopRepositories.ShopRepository;
import com.SadnaORM.Shops.Shop;
import com.SadnaORM.UserRepositories.SubscribedUserRepository;
import com.SadnaORM.Users.Basket;
import com.SadnaORM.Users.PaymentMethod;
import com.SadnaORM.Users.SubscribedUser;
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
public class SubscribedUserTests {
    private boolean deleteMichael1 = false;
    private boolean deleteShop = false;
    private Shop s;


    @Autowired
    SubscribedUserRepository subscribedUserRepository;

    @Autowired
    ShopRepository shopRepository;

    @AfterEach
    public void tearDown(){
        if(deleteMichael1){
            subscribedUserRepository.deleteById("Michael1");
            deleteMichael1 = false;
        }

        if(deleteShop){
            shopRepository.deleteById(0);
            deleteShop = false;
        }
    }

    @Test
    public void testSaveUserNotAdministrator(){
        SubscribedUser user = new SubscribedUser("Michael1","123321password",true,true,null);
        PaymentMethod method = new PaymentMethod("4580111122223333", 655, 2025, 13, user);
        user.setPaymentMethod(method);
        subscribedUserRepository.save(user);
        Optional<SubscribedUser> userFound = subscribedUserRepository.findById("Michael1");
        assertTrue(userFound.isPresent());
        user = userFound.get();
        assertEquals("Michael1", user.getUsername());
        assertEquals("123321password", user.getPassword());
        deleteMichael1 = true;
    }

    @Test
    public void testRemoveUser(){
        testSaveUserNotAdministrator();
        subscribedUserRepository.deleteById("Michael1");
        Optional<SubscribedUser> userNotFound = subscribedUserRepository.findById("Michael1");
        assertFalse(userNotFound.isPresent());
        deleteMichael1 = false;
    }

    @Test
    public void testRemoveUserFailure(){
        try {
            subscribedUserRepository.deleteById("Michael2");
            fail("removed a non existing user");
        }
        catch (Exception ignored){

        }
    }

    @Test
    public void testSaveUserWithBasket(){
        testSaveUserNotAdministrator();
        Optional<SubscribedUser> userFound = subscribedUserRepository.findById("Michael1");
        assertTrue(userFound.isPresent());
        SubscribedUser user = userFound.get();
        makeShop();
        Optional<Shop> shopFound = shopRepository.findById(0);
        assertTrue(shopFound.isPresent());
        Shop shop = shopFound.get();
        Basket userBasket = new Basket(shop, user, makeProducts());
        user.addBasket(userBasket);
        subscribedUserRepository.save(user);
        Map<Shop,Basket> userBaskets = user.getUserBaskets();
        assertNotNull(userBaskets);
        assertEquals(1, userBaskets.size());
        Basket b1 = userBaskets.getOrDefault(shop, null);
        assertNotNull(b1);
        Map<Integer, Integer> products = b1.getProducts();
        int quantity0 = products.getOrDefault(0, -1);
        int quantity1 = products.getOrDefault(1, -1);
        assertEquals(4, quantity0);
        assertEquals(5, quantity1);
    }

    private void makeShop(){
        s = new Shop(0, "Adidas", "Sports");
        shopRepository.save(s);
        deleteShop = true;
    }

    private Map<Integer, Integer> makeProducts(){
        Map<Integer, Integer> productsInBasket = new HashMap<>();
        productsInBasket.put(0, 4);
        productsInBasket.put(1, 5);
        return productsInBasket;
    }

}
