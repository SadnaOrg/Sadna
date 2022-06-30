package com.SadnaORM.SadnaORM.UserTests;

import com.SadnaORM.UserRepositories.GuestRepository;
import com.SadnaORM.Users.Guest;
import com.SadnaORM.Users.PaymentMethod;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class GuestTests {

    @Autowired
    GuestRepository guestRepository;

    @Test
    public void testSaveGuestNoPaymentMethod(){
        Guest guest = new Guest("Gusto", null);
        guestRepository.save(guest);
        Optional<Guest> guestFound = guestRepository.findById("Gusto");
        assertTrue(guestFound.isPresent());
        guest = guestFound.get();
        assertEquals("Gusto", guest.getUsername());
        assertNull(guest.getPaymentMethod());
    }

    @Test
    public void testRemoveGuestNoPaymentMethod(){
        testSaveGuestNoPaymentMethod();
        guestRepository.deleteById("Gusto");
        Optional<Guest> guestNotFound = guestRepository.findById("Gusto");
        assertFalse(guestNotFound.isPresent());
    }

    @Test
    public void testAddGuestWithPaymentMethod(){
        Guest guest = new Guest("Gusto");
        PaymentMethod method = new PaymentMethod("4580111122223333", 655, 2025, 13, guest);
        guest.setPaymentMethod(method);
        guestRepository.save(guest);
        Optional<Guest> guestFound = guestRepository.findById("Gusto");
        assertTrue(guestFound.isPresent());
        guest = guestFound.get();
        assertEquals("Gusto", guest.getUsername());
        assertNotNull(guest.getPaymentMethod());
        method = guest.getPaymentMethod();
        assertEquals("4580111122223333", method.getCreditCard());
        assertEquals(655, method.getCVV());
        assertEquals(2025, method.getExpirationYear());
        assertEquals(13, method.getExpirationDay());
    }

    @Test
    public void testRemoveGuestWithPaymentMethod(){
        testAddGuestWithPaymentMethod();
        guestRepository.deleteById("Gusto");
        Optional<Guest> guest = guestRepository.findById("Gusto");
        assertFalse(guest.isPresent());
    }
}
