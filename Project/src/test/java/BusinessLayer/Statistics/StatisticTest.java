package BusinessLayer.Statistics;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.Assert.*;

public class StatisticTest {

    Statistic s;
    @Before
    public void setUp() throws Exception {
        s = new Statistic(LocalDate.now());
    }

    @Test
    public void start() {
        var t0 = LocalTime.now().minusSeconds(3);
        s.start(1);

        assertEquals(1, s.getTick());
        assertTrue(t0.isBefore(s.getLastTick()));
    }

    @Test
    public void register() {
        var r = s.getRegisteredUser().lastValue;
        s.register();
        assertEquals((int) s.getRegisteredUser().lastValue, r + 1);
    }

    @Test
    public void login() {
        var r = s.getLoginUser().lastValue;
        s.login();
        assertEquals((int) s.getLoginUser().lastValue, r + 1);
    }

    @Test
    public void logout() {
        var r = s.getLoginUser().lastValue;
        s.logout();
        assertEquals((int) s.getLoginUser().lastValue, r -1);
    }

    @Test
    public void purchase() {
        var r = s.getPurchase().lastValue;
        s.purchase();
        assertEquals((int) s.getPurchase().lastValue, r + 1);
    }
}