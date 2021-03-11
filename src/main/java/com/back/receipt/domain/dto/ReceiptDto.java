package com.back.receipt.domain.dto;

public class ReceiptDto {

    private Long id;
    private String address;
    private String date;
    private double fullPrice;
    private int productCount;

    public ReceiptDto() {
    }

    public ReceiptDto(Long id, String address, String date, double fullPrice, int productCount) {
        this.id = id;
        this.address = address;
        this.date = date;
        this.fullPrice = fullPrice;
        this.productCount = productCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public double getFullPrice() {
        return fullPrice;
    }

    public void setFullPrice(double fullPrice) {
        this.fullPrice = fullPrice;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReceiptDto that = (ReceiptDto) o;

        if (Double.compare(that.fullPrice, fullPrice) != 0) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (address != null ? !address.equals(that.address) : that.address != null) return false;
        return date != null ? date.equals(that.date) : that.date == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id != null ? id.hashCode() : 0;
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        temp = Double.doubleToLongBits(fullPrice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
