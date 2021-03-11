package com.back.receipt.security.service;

import com.back.receipt.security.config.PasswordConfig;
import com.back.receipt.security.domain.Role;
import com.back.receipt.security.domain.User;
import com.back.receipt.security.repository.RoleRepository;
import com.back.receipt.security.repository.UserRepository;
import com.back.receipt.domain.dto.UserRegisterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserServiceSecurity implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordConfig passwordConfig;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUserName(username);

        userOptional.orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        return userOptional.map(com.back.receipt.security.domain.UserDetails::new).get();
    }

    public User registerNewUser(final UserRegisterDto userDto) throws EmailOrUsernameExistsException {

        if(usernameExist(userDto.getUserName())) {
            throw new EmailOrUsernameExistsException("Username " + userDto.getUserName() + " is currently in use");
        }

        if(emailExist(userDto.getEmail())) {
            throw new EmailOrUsernameExistsException("Email " + userDto.getEmail() + "is currently in use");
        }

        User user = new User();
        user.setUserName(userDto.getUserName());
        user.setPassword(passwordConfig.passwordEncoder().encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        Role role = roleRepository.findByRole("ROLE_USER").get();
        user.setRoles(Collections.singletonList(role));
        user.setActive(true);
        return userRepository.save(user);
    }

    private boolean usernameExist(String userName) {
        Optional<User> userOptional = userRepository.findByUserName(userName);
        if(userOptional.isPresent()) {
            return true;
        } else {
            return false;
        }
    }

    private boolean emailExist(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isPresent()) {
            return true;
        } else {
            return false;
        }
    }
}
