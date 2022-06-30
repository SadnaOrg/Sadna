package org.sadna.tasks;

import ServiceLayer.UserServiceImp;
import ServiceLayer.interfaces.UserService;
import com.github.myzhan.locust4j.AbstractTask;
import com.github.myzhan.locust4j.Locust;
import io.restassured.response.Response;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static io.restassured.RestAssured.given;

public class RegisterTask extends AbstractTask {
    private int weight;
    private AtomicInteger counter = new AtomicInteger(0);

    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public String getName() {
        return "Register Task";
    }

    public RegisterTask(int weight){
        this.weight = weight;
    }

    @Override
    public void execute() {
        long time = System.currentTimeMillis();
        try {
            Response response = given().get("http://localhost:8080/Register");
            assert response.getStatusCode() == 200;
            UserService service = new UserServiceImp();
            assert response.getTimeIn(TimeUnit.MILLISECONDS) < 1000;
            var resLoginSystem = service.loginSystem();
            assert resLoginSystem.isOk();
            int count = counter.getAndIncrement();
            var resRegister = service.registerToSystem("newUser" + count, "newUser" + count, new Date());
            assert resRegister.isOk();
            assert (System.currentTimeMillis() - time) < 1000;
            Locust.getInstance().recordSuccess("http", getName(), response.getTime(), 1);
        } catch (Exception ex) {
            ex.printStackTrace();
            Locust.getInstance().recordFailure("http", getName(), (System.currentTimeMillis() - time), ex.getMessage());
        }
    }
}
