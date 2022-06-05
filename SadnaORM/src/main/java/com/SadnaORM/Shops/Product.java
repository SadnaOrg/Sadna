package com.SadnaORM.Shops;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "product")
public class Product {
    public Product(int id, String name, String description, String manufacturer, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.manufacturer = manufacturer;
        this.price = price;
        this.quantity = quantity;
    }

    @Id
    private int id;
    private String name;
    private String description;
    private String manufacturer;
    private double price;
    private int quantity;

    public Product() {

    }
}
