package org.sadna.tasks;

import ServiceLayer.UserServiceImp;
import ServiceLayer.interfaces.SystemManagerService;
import ServiceLayer.interfaces.UserService;
import com.github.myzhan.locust4j.AbstractTask;
import com.github.myzhan.locust4j.Locust;
import io.restassured.response.Response;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;

public class RemoveUserTask extends AbstractTask {
    private int weight;
    private SystemManagerService managerService;

    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public String getName() {
        return "Remove User Task";
    }

    public RemoveUserTask(int weight, SystemManagerService service){
        this.weight = weight;
        this.managerService = service;
    }

    @Override
    public void execute() {
        long time = System.currentTimeMillis();
        try {
            Response response = given().get("http://localhost:8080/Login");
            assert response.getStatusCode() == 200;
            UserService service = new UserServiceImp();
            assert response.getTimeIn(TimeUnit.MILLISECONDS) < 1000;
            var resLoginSystem = service.loginSystem();
            assert resLoginSystem.isOk();
            var resRegisterUser = service.registerToSystem("temp", "temp", new Date());
            assert resRegisterUser.isOk();
            var resRemoveUser = managerService.removeSubscribedUserFromSystem("temp");
            assert resRemoveUser.isOk();
            assert (System.currentTimeMillis() - time) < 1000;
            Locust.getInstance().recordSuccess("http", getName(), response.getTime(), 1);
        } catch (Exception ex) {
            ex.printStackTrace();
            Locust.getInstance().recordFailure("http", getName(), (System.currentTimeMillis() - time), ex.getMessage());
        }
    }
}
