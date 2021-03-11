package com.back.receipt.domain.mapper;

import com.back.receipt.domain.dto.UsersAdminDto;
import com.back.receipt.security.domain.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    private UsersAdminDto mapToUserAdminDto(final User user) {
        return new UsersAdminDto(user.getId(), user.getUserName(), user.getEmail(), user.isActive(), user.getRoles().toString());
    }

    public List<UsersAdminDto> mapToUsersAdminDto (final List<User> userList) {
        return userList.stream()
                .map(this::mapToUserAdminDto)
                .collect(Collectors.toList());
    }
}