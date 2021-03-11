package com.back.receipt.domain;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String address;
    private String date;
    private double fullPrice;
    private Long ownerId;

    @OneToMany(targetEntity = Product.class, mappedBy = "receipt", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Product> productList = new ArrayList<>();

    @ManyToOne()
    @JoinColumn(name = "room_id")
    private Room room;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "receipt")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<UserPartPriceReceipt> userPartPrice = new ArrayList<>();

    public Receipt() {
    }

    public Receipt(Long id, String address, String date, double fullPrice) {
        this.id = id;
        this.address = address;
        this.date = date;
        this.fullPrice = fullPrice;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public double getFullPrice() {
        return fullPrice;
    }

    public void setFullPrice(double fullPrice) {
        this.fullPrice = fullPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public List<UserPartPriceReceipt> getUserPartPrice() {
        return userPartPrice;
    }

    public void setUserPartPrice(List<UserPartPriceReceipt> userPartPriceProduct) {
        this.userPartPrice = userPartPriceProduct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Receipt receipt = (Receipt) o;

        return id != null ? id.equals(receipt.id) : receipt.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
