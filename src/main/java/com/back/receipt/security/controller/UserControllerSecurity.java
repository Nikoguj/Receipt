package com.back.receipt.security.controller;

import com.back.receipt.security.service.EmailOrUsernameExistsException;
import com.back.receipt.security.service.UserServiceSecurity;
import com.back.receipt.domain.dto.UserRegisterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserControllerSecurity {

    @Autowired
    private UserServiceSecurity userServiceSecurity;

    public void registerUser(UserRegisterDto userDto) throws EmailOrUsernameExistsException {
        userServiceSecurity.registerNewUser(userDto);
    }
}