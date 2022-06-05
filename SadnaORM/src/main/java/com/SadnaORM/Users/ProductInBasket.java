package com.SadnaORM.Users;

import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class ProductInBasket {
    private int ID;
    private int quantity;
}
