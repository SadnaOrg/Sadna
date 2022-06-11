package BusinessLayer.System;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ExternalPayment {

    private final String url = "https://cs-bgu-wsep.herokuapp.com/";
    private final int minTransactionID = 10000;
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

    public int pay(PaymentInfo info) {
        HttpRequest request;
        HttpResponse<String> response;
        PaymentMethod method = info.getMethod();
        String ID = info.getID();
        String holder = info.getHolder();
        try {
            request = HttpRequest.newBuilder().uri(new URI(url)).
                    header("action_type", "pay").
                    header("card_number", method.getCreditCardNumber()).
                    header("month", String.valueOf(method.getExpiryMonth())).
                    header("year", String.valueOf(method.getExpiryYear())).
                    header("holder", holder).
                    header("ccv", String.valueOf(method.getCVV())).
                    header("id", ID).
                    POST(HttpRequest.BodyPublishers.noBody()).
                    build();
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception exception) {
            throw new RuntimeException("failed to send a payment request to the payment system");
        }

        int transaction = Integer.parseInt(response.body());
        if (isLegalID(transaction)) {
            return transaction;
        } else if (transaction == -1) {
            throw new RuntimeException("failed to process the payment");
        } else throw new IllegalStateException("received illegal transaction number from payment system");
    }

    public boolean cancelPayment(int transactionID) {
        HttpRequest request;
        HttpResponse<String> response;
        if (!(isLegalID(transactionID))) {
            throw new IllegalArgumentException("the transaction number is between 10,000 and 100,000");
        }
        try {
            request = HttpRequest.newBuilder().
                    uri(new URI(url)).
                    header("action_type", "cancel_pay").
                    header("transaction_id", String.valueOf(transactionID)).
                    POST(HttpRequest.BodyPublishers.noBody()).
                    build();
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception exception) {
            throw new RuntimeException("failed to get a request from the payment system");
        }

        int success = Integer.parseInt(response.body());
        if (success == 1)
            return true;
        else if (success == -1)
            return false;
        else throw new IllegalStateException("received illegal answer from the payment system");
    }

    private boolean isLegalID(int transactionID){
        return transactionID >= minTransactionID & transactionID <= maxTransactionID;
    }
}
