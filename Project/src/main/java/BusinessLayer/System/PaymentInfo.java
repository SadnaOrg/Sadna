package BusinessLayer.System;

public class PaymentInfo {
    private PaymentMethod method;
    private String ID;
    private String holder;

    public PaymentInfo(PaymentMethod method, String ID, String holder) {
        this.method = method;
        this.ID = ID;
        this.holder = holder;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public String getID() {
        return ID;
    }

    public String getHolder() {
        return holder;
    }
}
