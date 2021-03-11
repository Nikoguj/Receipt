package com.back.receipt.domain.dto;

public class RoomDto {
    private Long id;
    private String password;
    private String userList;
    private int numberOfReceipts = 0;

    public RoomDto() {
    }

    public RoomDto(Long id, String password, String userList, int receiptCount) {
        this.id = id;
        this.password = password;
        this.userList = userList;
        this.numberOfReceipts = receiptCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserList() {
        return userList;
    }

    public void setUserList(String userList) {
        this.userList = userList;
    }

    public int getNumberOfReceipts() {
        return numberOfReceipts;
    }

    public void setNumberOfReceipts(int numberOfReceipts) {
        this.numberOfReceipts = numberOfReceipts;
    }
}
