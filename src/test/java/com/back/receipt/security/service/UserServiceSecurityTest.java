package com.back.receipt.security.service;

import com.back.receipt.security.domain.User;
import com.back.receipt.security.repository.UserRepository;
import com.back.receipt.domain.dto.UserRegisterDto;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
class UserServiceSecurityTest {

    @Autowired
    private UserServiceSecurity userServiceSecurity;

    @Autowired
    private UserRepository userRepository;

    @Test
    void registerNewUser() throws EmailOrUsernameExistsException {
        //Given
        UserRegisterDto userDto = new UserRegisterDto("test1", "test1", "test1@gmail.com");

        //When
        User user = userServiceSecurity.registerNewUser(userDto);

        //Then
        Assert.assertNotNull(user.getId());
        Assert.assertEquals("test1", user.getUserName());
        Assert.assertNotNull(user.getPassword());
        Assert.assertEquals("test1@gmail.com", user.getEmail());
        Assert.assertEquals(true, user.isActive());

        //Clean up
        userRepository.delete(user);
    }
}