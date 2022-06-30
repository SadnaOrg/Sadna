package org.sadna.tasks;

import ServiceLayer.Result;
import ServiceLayer.UserServiceImp;
import ServiceLayer.interfaces.UserService;
import com.github.myzhan.locust4j.AbstractTask;
import com.github.myzhan.locust4j.Locust;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class PurchaseCartTask extends AbstractTask {
    private int weight;
    private static AtomicInteger counter = new AtomicInteger(0);

    public PurchaseCartTask(int weight){
        this.weight = weight;
    }

    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public String getName() {
        return "Purchase Cart Task";
    }

    @Override
    public void execute() throws Exception {
        long time = System.currentTimeMillis();
        try {
            UserService service = new UserServiceImp();
            var resLoginSystem = service.loginSystem();
            assert resLoginSystem.isOk();
            int rand = new Random().nextInt(4000, 5000);
            var resLogin = service.login("maor" + rand, "maor" + rand);
            assert resLogin.isOk();
            var resOpenShop = resLogin.getElement().openShop("name", "desc");
            assert resOpenShop.isOk();
            int productID = counter.getAndIncrement();
            var resAddProduct = resLogin.getElement().addProductToShop(resOpenShop.getElement().shopId(), "name", "desc", "manufacturer", productID, 20, 20.0);
            assert resAddProduct.isOk();
            var resSaveProducts = service.saveProducts(resOpenShop.getElement().shopId(), productID, 1);
            assert resSaveProducts.isOk();
            var resPurchaseCartFromShop = service.purchaseCartFromShop("1234567890123456", 123, 5, 2048);
            assert resPurchaseCartFromShop.isOk();
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
