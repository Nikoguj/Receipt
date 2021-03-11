package com.back.receipt.domain.dto;

public class UserPartPriceDto {

    private String username;
    private double partPrice;

    public UserPartPriceDto(String username, double partPrice) {
        this.username = username;
        this.partPrice = partPrice;
    }

    public UserPartPriceDto() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getPartPrice() {
        return partPrice;
    }

    public void setPartPrice(double partPrice) {
        this.partPrice = partPrice;
    }

    public String getPartPriceString() {
        return String.valueOf(partPrice);
    }

    public void setPartPriceString(String partPrice) {
        this.partPrice = Double.parseDouble(partPrice);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserPartPriceDto that = (UserPartPriceDto) o;

        return username != null ? username.equals(that.username) : that.username == null;
    }

    @Override
    public int hashCode() {
        return username != null ? username.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "UserPartPriceDto{" +
                "username='" + username + '\'' +
                ", partPrice=" + partPrice +
                '}';
    }
}
