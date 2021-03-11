package com.back.receipt.domain;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "receipt_id")
    private Receipt receipt;

    private String name;
    private double quantity;
    private double priceForOne;
    private double priceWithoutDiscount;
    private double discount;
    private double price;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<UserPartPriceProduct> userPartPrice = new ArrayList<>();

    public Product(String name) {
        this.name = name;
    }

    public Product() {
    }

    public Product(Long id, Receipt receipt, String name, double quantity, double priceForOne, double priceWithoutDiscount, double discount, double price) {
        this.id = id;
        this.receipt = receipt;
        this.name = name;
        this.quantity = quantity;
        this.priceForOne = priceForOne;
        this.priceWithoutDiscount = priceWithoutDiscount;
        this.discount = discount;
        this.price = price;
    }

    public Product(String name, double quantity, double priceForOne, double priceWithoutDiscount, double discount, double price) {
        this.name = name;
        this.quantity = quantity;
        this.priceForOne = priceForOne;
        this.priceWithoutDiscount = priceWithoutDiscount;
        this.discount = discount;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Receipt getReceipt() {
        return receipt;
    }

    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getPriceForOne() {
        return priceForOne;
    }

    public void setPriceForOne(double priceForOne) {
        this.priceForOne = priceForOne;
    }

    public double getPriceWithoutDiscount() {
        return priceWithoutDiscount;
    }

    public void setPriceWithoutDiscount(double priceWithoutDiscount) {
        this.priceWithoutDiscount = priceWithoutDiscount;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<UserPartPriceProduct> getUserPartPrice() {
        return userPartPrice;
    }

    public void setUserPartPrice(List<UserPartPriceProduct> userPartPriceProduct) {
        this.userPartPrice = userPartPriceProduct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        return id != null ? id.equals(product.id) : product.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
