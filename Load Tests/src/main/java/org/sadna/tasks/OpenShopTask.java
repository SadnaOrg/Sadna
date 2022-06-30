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

public class OpenShopTask extends AbstractTask {
    private int weight;
    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public String getName() {
        return "Open Shop Task";
    }

    public OpenShopTask(int weight){
        this.weight = weight;
    }

    @Override
    public void execute() throws Exception {
        long time = System.currentTimeMillis();
        try {
            UserService service = new UserServiceImp();
            var resLoginSystem = service.loginSystem();
            assert resLoginSystem.isOk();
            int rand = new Random().nextInt(3000, 4000);
            var resLogin = service.login("maor" + rand, "maor" + rand);
            assert resLogin.isOk();
            var resOpenShop = resLogin.getElement().openShop("name", "desc");
            assert resOpenShop.isOk();
            var resLogout = resLogin.getElement().logout();
            assert resLogout.isOk();
            assert (System.currentTimeMillis() - time) < 1000;
            Locust.getInstance().recordSuccess("http", getName(), (System.currentTimeMillis() - time), 1);
        } catch (Exception ex) {
            ex.printStackTrace();
            Locust.getInstance().recordFailure("http", getName(), (System.currentTimeMillis() - time), ex.getMessage());
        }
    }
}
