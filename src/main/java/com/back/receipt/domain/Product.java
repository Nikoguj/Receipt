package com.back.receipt.domain;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode
public class Product {
    private String name;
    private double quantity;
    private double priceForOne;
    private double priceWithoutDiscount;
    private double discount;
    private double price;

    public Product(String name) {
        this.name = name;
    }
}
