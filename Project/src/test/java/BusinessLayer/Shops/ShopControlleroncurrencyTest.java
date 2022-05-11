package BusinessLayer.Shops;

import org.junit.Test;
import org.junit.experimental.ParallelComputer;
import org.junit.runner.JUnitCore;

import static org.junit.Assert.*;

public class ShopControlleroncurrencyTest {
    @Test
    public void test() {
        Class[] cls = {ShopControllerTest.class, ShopControllerTest.class, ShopControllerTest.class, ShopControllerTest.class};
        Class[] cls_unit = {ShopControllerUnitTest.class, ShopControllerUnitTest.class, ShopControllerUnitTest.class, ShopControllerUnitTest.class};

        // Parallel among classes
        JUnitCore.runClasses(ParallelComputer.classes(), cls);
        JUnitCore.runClasses(ParallelComputer.classes(), cls_unit);

        System.out.println("----------------------------");

        // Parallel among methods in a class
        JUnitCore.runClasses(ParallelComputer.methods(), cls);
        JUnitCore.runClasses(ParallelComputer.methods(), cls_unit);

        System.out.println("----------------------------");

        // Parallel all methods in all classes
        JUnitCore.runClasses(new ParallelComputer(true, true), cls);
        JUnitCore.runClasses(new ParallelComputer(true, true), cls_unit);

    }
}