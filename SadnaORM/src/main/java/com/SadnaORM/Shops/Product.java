package com.SadnaORM.Shops;

import javax.persistence.*;

@Entity
@Table(name = "product")
public class Product {
    @Id
    private int id;
    private String name;
    private String description;
    private String manufacturer;
    private double price;
    private int quantity;
    @ManyToOne
    @JoinColumn(name = "shopID")
    private Shop shop;
}
