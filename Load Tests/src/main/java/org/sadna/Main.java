package org.sadna;

import ServiceLayer.Response;
import ServiceLayer.UserServiceImp;
import ServiceLayer.interfaces.SystemManagerService;
import ServiceLayer.interfaces.UserService;
import com.github.myzhan.locust4j.AbstractTask;
import com.github.myzhan.locust4j.Locust;
import org.sadna.tasks.*;

import javax.security.auth.callback.Callback;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        initializeUsers();
        initializeLoggedInUsers();
        SystemManagerService resManager = initializeSystemManager();
        Locust locust = Locust.getInstance();
        locust.setMasterHost("127.0.0.1");
        locust.setMaxRPS(100);
        locust.setMasterPort(5557); //some free port to run the Locust slave
        AbstractTask[] tasks = new AbstractTask[]{
                new SearchShopsTask(25),
                new LoginTask(25),
                new OpenShopTask(25),
                new HomePageTask(25),
                new AddProductsTask(25),
                new PurchaseCartTask(25),
                new AddDiscountTask(25),
                new RemoveUserTask(25, resManager),
                new RegisterTask(25),
        };
        locust.dryRun(tasks);
        locust.run(tasks);
    }

    private static void initializeLoggedInUsers() {
        UserService service = new UserServiceImp();
        service.loginSystem();
        for(int i = 8000; i < 9000; i++){
            service.login("maor" + i, "maor" + i);
        }
        service.logoutSystem();
    }

    private static SystemManagerService initializeSystemManager() {
        UserService service = new UserServiceImp();
        var resLoginSystem = service.loginSystem();
        assert resLoginSystem.isOk();
        var resLogin = service.login("Admin", "ILoveIttaiNeria");
        assert resLogin.isOk();
        var resManager = resLogin.getElement().manageSystemAsSystemManager();
        assert resManager.isOk();
        return resManager.getElement();
    }

    private static void initializeUsers() {
        for(int i = 0; i < 10000; i++){
            UserService service = new UserServiceImp();
            var resLoginSystem = service.loginSystem();
            assert resLoginSystem.isOk();
            var resRegisterSystem = service.registerToSystem("maor" + i, "maor" + i, new Date());
            assert resRegisterSystem.isOk();
            var resLogin = service.login("maor" + i, "maor" + i);
            assert resLogin.isOk();
            if(i % 10 == 0){
                var resOpenShop = resLogin.getElement().openShop("name", "name");
                assert resOpenShop.isOk();
                for(int j = 0; j < 1000; j++){
                    int productId = i*10000+j;
                    var resAddProducts = resLogin.getElement().addProductToShop(resOpenShop.getElement().shopId(), "", "", "", productId, 10, 10.0);
                    assert resAddProducts.isOk();
                    var resAddToCart = service.saveProducts(resOpenShop.getElement().shopId(), productId, 1);
                    assert resAddToCart.isOk();
                }
            }
            var resLogout = resLogin.getElement().logout();
            assert resLogout.isOk();
        }
    }
}