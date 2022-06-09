package BusinessLayer.Notifications;

import BusinessLayer.Users.User;
import BusinessLayer.Users.UserController;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.LinkedList;
import java.util.function.Function;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NotifierUnitTest {

    @Mock
    private Notification not1,not2;

    private String user1="user1",user2="user2";
    @Mock
    private User u1;
    private Notifier notifier;
    private Function<Notification, Boolean> con1,con2;
    private LinkedList<Notification> notList1,notList2;


    @Before
    public void setUp() throws Exception {
        notList1 = new LinkedList<>();
        notList2 = new LinkedList<>();
        notifier = new Notifier();
        not1 =  Mockito.mock(Notification.class);
        not2 =  Mockito.mock(Notification.class);
        u1 = mock(User.class);
        when(u1.getName()).thenReturn(user1);
        when(u1.getUserName()).thenReturn(user1);
        con1 = (n)->notList1.add(n);
        notifier.register(con1,user1);
        UserController.getInstance().addUserForTest(u1);
        var x = new LinkedList<String>();
        x.add(user1);
        when(not1.getUserNames()).thenReturn(x);
        x = new LinkedList<String>();
        x.add(user2);
        when(not2.getUserNames()).thenReturn(x);
    }

    @Test (expected = Exception.class)
    public void notification_on_real_time_fail_nonExistUser() {
        notifier.addNotification(not2);
        fail("send massege to a non existing user");
    }
    @Test
    public void notification_on_real_time_Sucsses() {
        when(u1.isLoggedIn()).thenReturn(true);
        notifier.addNotification(not1);
        assertEquals(1, notList1.size());
        assertEquals(notList1.get(0),not1);
    }

    @Test
    public void notification_delay_succsess() {
        when(u1.isLoggedIn()).thenReturn(false);
        notifier.addNotification(not1);
        assertEquals(0, notList1.size());
        when(u1.isLoggedIn()).thenReturn(true);
        try {
            notifier.getDelayedNotifications(user1);
        }catch (Exception ignored){
        }
        assertEquals(1, notList1.size());
        assertEquals(notList1.get(0),not1);
    }

    @Test
    public void unregister() {
        notifier.unregister(user1);
        when(u1.isLoggedIn()).thenReturn(true);
        notifier.addNotification(not1);
        try {
            notifier.getDelayedNotifications(user1);
        }catch (Exception ignored){
        }
        assertEquals(0, notList1.size());
    }
}