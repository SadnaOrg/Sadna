package org.sadna.tasks;

import com.github.myzhan.locust4j.AbstractTask;
import com.github.myzhan.locust4j.Locust;

import io.restassured.response.Response;

import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
public class HomePageTask extends AbstractTask{
    private int weight;

    @Override
    public int getWeight() {
        return weight;
    }


    @Override
    public String getName() {
        return "Home Page Task";
    }

    public HomePageTask(int weight){
        this.weight = weight;
    }

    @Override
    public void execute() {
        int responseTime = 0;
        try {
            Response response = given().get("http://localhost:8080");

            assert response.getStatusCode() == 200;
            assert response.getTimeIn(TimeUnit.SECONDS) < 1;
            responseTime = (int) response.getTimeIn(TimeUnit.MILLISECONDS);

            Locust.getInstance().recordSuccess("http", getName(), response.getTime(), 1);
        } catch (Exception ex) {
            ex.printStackTrace();
            Locust.getInstance().recordFailure("http", getName(), responseTime, ex.getMessage());
        }
    }
}
