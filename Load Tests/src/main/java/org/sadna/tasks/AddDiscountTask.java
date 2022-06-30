package org.sadna.tasks;

import ServiceLayer.UserServiceImp;
import ServiceLayer.interfaces.UserService;
import com.github.myzhan.locust4j.AbstractTask;
import com.github.myzhan.locust4j.Locust;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class AddDiscountTask extends AbstractTask {
    private int weight;
    private static AtomicInteger counter = new AtomicInteger(0);

    public AddDiscountTask(int weight){
        this.weight = weight;
    }

    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public String getName() {
        return "Add Discount Task";
    }

    @Override
    public void execute() throws Exception {
        long time = System.currentTimeMillis();
        try {
            UserService service = new UserServiceImp();
            var resLoginSystem = service.loginSystem();
            assert resLoginSystem.isOk();
            int rand = new Random().nextInt(1000);
            var resLogin = service.login("maor" + rand, "maor" + rand);
            assert resLogin.isOk();
            var resOpenShop = resLogin.getElement().openShop("name", "desc");
            assert resOpenShop.isOk();
            var resAddDiscount = resLogin.getElement().createDiscountPlusPolicy(1, resOpenShop.getElement().shopId());
            assert resAddDiscount.isOk();
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
