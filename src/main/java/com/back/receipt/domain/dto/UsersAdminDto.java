package com.back.receipt.domain.dto;

public class UsersAdminDto {

    private Long id;
    private String username;
    private String email;
    private boolean isActive;
    private String role;

    public UsersAdminDto(Long id, String username, String email, boolean isActive, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.isActive = isActive;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
