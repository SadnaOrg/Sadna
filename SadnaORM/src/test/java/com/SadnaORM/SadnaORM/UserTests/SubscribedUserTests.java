package com.SadnaORM.SadnaORM.UserTests;

import com.SadnaORM.Shops.Shop;
import com.SadnaORM.UserRepositories.SubscribedUserRepository;
import com.SadnaORM.Users.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class SubscribedUserTests {

    @Autowired
    SubscribedUserRepository subscribedUserRepository;

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
    }

    @Test
    public void testRemoveUser(){
        testSaveUserNotAdministrator();
        subscribedUserRepository.deleteById("Michael1");
        Optional<SubscribedUser> userNotFound = subscribedUserRepository.findById("Michael1");
        assertFalse(userNotFound.isPresent());
    }

    @Test
    public void testUserWithAdmin(){
        testSaveUserNotAdministrator();
        ShopAdministrator.BaseActionType action = ShopAdministrator.BaseActionType.HISTORY_INFO;
        List<ShopAdministrator.BaseActionType> actions = new LinkedList<>();
        actions.add(action);
        Shop s = new Shop(0, "Adidas", "Sports");
        Optional<SubscribedUser> userFound = subscribedUserRepository.findById("Michael1");
        assertTrue(userFound.isPresent());
        SubscribedUser user = userFound.get();
        ShopAdministrator administrator = new ShopManager(actions, user, s, new LinkedList<>());
        user.addAdministrator(administrator);
        subscribedUserRepository.save(user);
        userFound = subscribedUserRepository.findById("Michael1");
        assertTrue((userFound.isPresent()));
        user = userFound.get();
        List<ShopAdministrator> administrators = user.getAdministrators();
        assertEquals(1,administrators.size());
        ShopAdministrator myAdmin = administrators.get(0);
        List<ShopAdministrator.BaseActionType> myActions = myAdmin.getAction();
        assertNotNull(myAdmin.getUser());
        assertNotNull(myAdmin.getAppoints());
        assertNotNull(myAdmin.getShop());
        assertNotNull(myAdmin.getAction());
        assertEquals(1, myActions.size());
    }
}
