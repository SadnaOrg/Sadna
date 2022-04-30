package BusinessLayer.System;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ProxySupplyTest {
    public ProxySupply p = null;

    @Before
    public void setUp(){
        p = new ProxySupply();
    }

    @Test
    public void checkSupply() {
        Assert.assertTrue(p.checkSupply(1, 3));
        Assert.assertTrue(p.checkSupply(2, 3));
    }
}