package BusinessLayer.Users;

import BusinessLayer.Users.UserControllerTest;
import BusinessLayer.Users.UserControllerUnitTest;
import org.junit.Test;
import org.junit.experimental.ParallelComputer;
import org.junit.runner.JUnitCore;

import static org.junit.Assert.*;

public class UserControllerConcurrencyTest {
    @Test
    public void test() {
        Class[] cls = {UserControllerTest.class, UserControllerTest.class, UserControllerTest.class, UserControllerTest.class};
        Class[] cls_unit = {UserControllerUnitTest.class, UserControllerUnitTest.class, UserControllerUnitTest.class, UserControllerUnitTest.class};

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