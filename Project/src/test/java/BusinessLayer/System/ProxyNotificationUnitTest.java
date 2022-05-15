package BusinessLayer.System;

import BusinessLayer.Users.SubscribedUser;
import BusinessLayer.Users.User;
import BusinessLayer.Users.UserController;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class ProxyNotificationUnitTest {
    Notification notif;
    User user;
    UserController u2;
    @Before
    public void setUp(){
        notif = new ProxyNotification();
        user = mock(SubscribedUser.class);
        u2 = mock(UserController.class);
    }

    @Test
    public void testProxyNotificationSuccess(){
        try(MockedStatic<UserController> mockedStatic = mockStatic(UserController.class)){
            mockedStatic.when(UserController::getInstance).thenReturn(u2);
            when(u2.getUser("u1")).thenReturn(user);
            when(user.isLoggedIn()).thenReturn(false);
            Assert.assertFalse(notif.notifyUser("u1"));
        }
    }

    @Test
    public void testProxyNotificationFail(){
        try(MockedStatic<UserController> mockedStatic = mockStatic(UserController.class)){
            mockedStatic.when(UserController::getInstance).thenReturn(u2);
            when(u2.getUser("u1")).thenReturn(user);
            when(user.isLoggedIn()).thenReturn(true);
            Assert.assertTrue(notif.notifyUser("u1"));
        }
    }
}
