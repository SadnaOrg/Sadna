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

import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class SubscribedUserTests {

    @Autowired
    SubscribedUserRepository subscribedUserRepository;

    @Test
    public void testSimpleUser(){
        PaymentMethod method = new PaymentMethod();
        SubscribedUser user = new SubscribedUser("Michael1","123321password",true,true,method);
        subscribedUserRepository.save(user);
        Optional<SubscribedUser> userFound = subscribedUserRepository.findById("Michael1");
        assertTrue(userFound.isPresent());
        assertTrue(userFound.get().getUsername().equals("Michael1"));
    }
}
