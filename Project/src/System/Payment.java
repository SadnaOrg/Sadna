package System;

public abstract class Payment {
    int id;

    public abstract boolean pay(int totalPrice);
}
