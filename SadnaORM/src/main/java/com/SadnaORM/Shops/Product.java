package com.SadnaORM.Shops;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
}
