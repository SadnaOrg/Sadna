package BusinessLayer.System;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProxySupplyTest {
    public ProxySupply p = null;

    @BeforeAll
    public void setUp(){
        p = new ProxySupply();
    }

    @Test
    public void checkSupply() {
        assertTrue(p.checkSupply(1, 3));
        assertTrue(p.checkSupply(2, 3));
    }
}