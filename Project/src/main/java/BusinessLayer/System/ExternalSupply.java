package BusinessLayer.System;

import BusinessLayer.Products.ProductInfo;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static BusinessLayer.System.ExternalPayment.getSupplyInfo;

public class ExternalSupply implements Supply{

    private final String url = "https://cs-bgu-wsep.herokuapp.com/";
    private final int minTransactionID = 0;
    private final int maxTransactionID = 100000;

    public boolean available() {
        HttpRequest request;
        HttpResponse<String> response;
        try {
            request = HttpRequest.newBuilder().
                    uri(new URI(url)).
                    header("action_type", "handshake").
                    POST(HttpRequest.BodyPublishers.noBody()).
                    build();
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception exception) {
            throw new RuntimeException("failed to check availability");
        }

        return response.body().equals("OK");
    }

    public int supply(SupplyInfo info){
        HttpRequest request;
        HttpResponse<String> response;
        try {
            request = HttpRequest.newBuilder().uri(new URI(url)).
                    header("action_type","supply").
                    header("name",info.getName()).
                    header("address",info.getAddress()).
                    header("city",info.getCity()).
                    header("country",info.getCountry()).
                    header("zip",info.getZip()).
                    build();
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        }
        catch (Exception exception){
            throw new RuntimeException("failed to receive a response from the supply system");
        }

        int transaction = response.statusCode();
        if(isLegalID(transaction))
            return transaction;
        else if(transaction == -1)
            throw new RuntimeException("the transaction has failed");
        else throw new IllegalStateException("received an illegal response from the supply system");
    }

    public boolean cancelSupply(int transactionID){
        if (!(isLegalID(transactionID))) {
            throw new IllegalArgumentException("the transaction number is between 10,000 and 100,000");
        }
        HttpRequest request;
        HttpResponse<String> response;
        try {
            request = HttpRequest.newBuilder().
                    uri(new URI(url)).
                    header("action_type","cancel_supply").
                    header("transaction_id",String.valueOf(transactionID)).
                    POST(HttpRequest.BodyPublishers.noBody()).
                    build();
            response = HttpClient.newHttpClient().send(request,HttpResponse.BodyHandlers.ofString());
        }
        catch (Exception exception){
            throw new RuntimeException("failed to get a response from the supply system");
        }

        int success = Integer.parseInt(response.body());
        if(success == 1)
            return true;
        else if (success == -1)
            return false;
        else throw new IllegalStateException("received an illegal answer from the supply system");
    }

    private boolean isLegalID(int transactionID){
        return transactionID >= minTransactionID & transactionID <= maxTransactionID;
    }

    @Override
    public boolean checkSupply(ProductInfo pack) {
        return supply(getSupplyInfo(pack))>=0;
    }
}
