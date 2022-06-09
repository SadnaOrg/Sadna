package BusinessLayer.Notifications;

import BusinessLayer.Users.SubscribedUser;
import BusinessLayer.Users.User;
import BusinessLayer.Users.UserController;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;

import java.util.ArrayList;
import java.util.Collection;

import static org.mockito.Mockito.*;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class NotifierUnitTest {
    Notifier notifier;
    Collection<String> usernames;
    UserController controller;
    User loggedInUser;
    User loggedOutUser;
    String content = "content";
    @Before
    public void setUp() {
        notifier = new Notifier();
        usernames = generateUsers(5);
        controller = mock(UserController.class);
        loggedInUser = mock(User.class);
        loggedOutUser = mock(User.class);
        when(loggedInUser.isLoggedIn()).thenReturn(true);
        when(loggedOutUser.isLoggedIn()).thenReturn(false);
    }

//    @Test
//    public void addNotificationSuccess_AllLoggedIn() {
//        try(MockedStatic<UserController> mockedStatic = mockStatic(UserController.class)){
//            mockedStatic.when(UserController::getInstance).thenReturn(controller);
//            when(controller.getUser(anyString())).thenReturn(loggedInUser);
//            notifier.addNotification(usernames, content);
//            Assert.assertEquals(notifier.getNotifications().size(), 1);
//            Assert.assertEquals(notifier.getNotifications().stream().toList().get(0).getClass(), RealTimeNotification.class);
//        }
//    }
//
//    @Test
//    public void addNotificationSuccess_AllLoggedOut() {
//        try(MockedStatic<UserController> mockedStatic = mockStatic(UserController.class)){
//            mockedStatic.when(UserController::getInstance).thenReturn(controller);
//            when(controller.getUser(anyString())).thenReturn(loggedOutUser);
//            notifier.addNotification(usernames, content);
//            Assert.assertEquals(notifier.getNotifications().size(), 1);
//            Assert.assertEquals(notifier.getNotifications().stream().toList().get(0).getClass(), PausedNotification.class);
//        }
//    }
//
//    @Test
//    public void addNotificationSuccess_LoggedInAndOut() {
//        try(MockedStatic<UserController> mockedStatic = mockStatic(UserController.class)){
//            mockedStatic.when(UserController::getInstance).thenReturn(controller);
//            when(controller.getUser(anyString())).thenReturn(loggedOutUser);
//            when(controller.getUser("u1")).thenReturn(loggedInUser);
//            notifier.addNotification(usernames, content);
//            Assert.assertEquals(notifier.getNotifications().size(), 2);
//            Assert.assertEquals(notifier.getNotifications().stream().toList().get(0).getClass(), RealTimeNotification.class);
//            Assert.assertEquals(notifier.getNotifications().stream().toList().get(1).getClass(), PausedNotification.class);
//        }
//    }

    public Collection<String> generateUsers(int size){
        usernames = new ArrayList<>();
        for(int i = 0; i < size; i++){
            usernames.add("u" + (i+1));
        }
        return usernames;
    }
}
