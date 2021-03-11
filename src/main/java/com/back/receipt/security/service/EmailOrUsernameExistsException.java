package com.back.receipt.security.service;

public class EmailOrUsernameExistsException extends Exception {

    public EmailOrUsernameExistsException(String message) {
        super(message);
    }
}