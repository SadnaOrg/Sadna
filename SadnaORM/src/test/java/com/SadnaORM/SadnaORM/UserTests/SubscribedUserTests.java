package com.SadnaORM.SadnaORM.UserTests;

import com.SadnaORM.UserRepositories.SubscribedUserRepository;
import com.SadnaORM.Users.PaymentMethod;
import com.SadnaORM.Users.SubscribedUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class SubscribedUserTests {

    @Autowired
    SubscribedUserRepository subscribedUserRepository;

    @Test
    public void testSaveUserNotAdministrator(){
        PaymentMethod method = new PaymentMethod();
        SubscribedUser user = new SubscribedUser("Michael1","123321password",true,true,method);
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

    }
}
