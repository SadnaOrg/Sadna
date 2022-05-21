package BusinessLayer.Notifications;

import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class PausedNotificationTest {
    ConcreteNotification notification;
    String successUser = "u1";
    String failUser = "userToRemove";
    @Before
    public void setUp() throws Exception {
        notification = new PausedNotification("content");
    }

    @Test
    public void containsSuccess() {
        notification.add(successUser);
        assertTrue(notification.contains(successUser));
    }

    @Test
    public void containsFail_Empty() {
        notification.add(successUser);
        assertTrue(notification.contains(successUser));
    }

    @Test
    public void containsFail_wrongUser() {
        assertFalse(notification.contains(failUser));
    }

    @Test
    public void removeSuccess() {
        notification.add(successUser);
        assertTrue(notification.remove(successUser));
    }

    @Test
    public void removeFail_Empty() {
        assertFalse(notification.remove(failUser));
    }

    @Test
    public void removeFail_WrongUser() {
        assertTrue(notification.add(successUser));
        assertFalse(notification.remove(failUser));
    }

    @Test
    public void addSuccess() {
        assertTrue(notification.add(successUser));
    }

    @Test
    public void addFail_userAlreadyExists() {
        assertTrue(notification.add(successUser));
        assertFalse(notification.add(successUser));
        assertFalse(notification.add(successUser));
    }

    @Test
    public void isEmptySuccess() {
        assertTrue(notification.isEmpty());
    }

    @Test
    public void isEmptyFail() {
        notification.add(successUser);
        assertFalse(notification.isEmpty());
    }
}