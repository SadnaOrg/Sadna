package BusinessLayer.System;

import BusinessLayer.Notifications.ProxyNotificationUnitTest;
import org.junit.Test;
import org.junit.experimental.ParallelComputer;
import org.junit.runner.JUnitCore;

@SuppressWarnings("rawtypes")
public class SystemParallelTest {
    @Test
    public void testParallelUnitTests(){
        testParallelClasses(new Class[]{PaymentMethodUnitTest.class, ProxyPaymentUnitTest.class, ProxySupplyUnitTest.class,
                ExternalServicesSystemUnitTest.class, ProxyNotificationUnitTest.class, SystemUnitTest.class});
    }
    @Test
    public void testParallelIntegrationTests(){
        testParallelClasses(new Class[]{ProxyPaymentTest.class, ExternalServicesSystemTest.class, SystemTest.class});
    }

    public void testParallelClasses(Class[] cls){
        JUnitCore.runClasses(ParallelComputer.classes(), cls);
        JUnitCore.runClasses(ParallelComputer.methods(), cls);
        JUnitCore.runClasses(new ParallelComputer(true, true), cls);
    }
}
