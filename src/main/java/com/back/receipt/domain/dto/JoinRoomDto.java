package com.back.receipt.domain.dto;

public class JoinRoomDto {
    private String id;
    private String password;

    public JoinRoomDto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}