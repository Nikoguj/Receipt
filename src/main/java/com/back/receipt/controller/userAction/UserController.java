package com.back.receipt.controller.userAction;

import com.back.receipt.domain.dto.RoomDto;
import com.back.receipt.domain.mapper.RoomMapper;
import com.back.receipt.exception.MyResourceNotFoundException;
import com.back.receipt.security.service.UserServiceSecurity;
import com.back.receipt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoomMapper roomMapper;

    @PostMapping("/createRoom/{username}")
    @PreAuthorize("#username.equals(authentication.principal.username) || hasRole('ROLE_ADMIN')")
    public void createRoom(@PathVariable String username) {
        userService.createRoom(username);
    }

    @GetMapping("/getRoomList/{username}")
    @PreAuthorize("#username.equals(authentication.principal.username) || hasRole('ROLE_ADMIN')")
    public List<RoomDto> getRoomList(@PathVariable String username) throws MyResourceNotFoundException {
        return roomMapper.mapToRoomDtoList(userService.getRoomList(username));
    }

    @PutMapping("/joinRoom/{username}/{roomId}/{roomPassword}")
    @PreAuthorize("#username.equals(authentication.principal.username) || hasRole('ROLE_ADMIN')")
    public void joinRoom(@PathVariable String username, @PathVariable Long roomId, @PathVariable String roomPassword) throws MyResourceNotFoundException {
        userService.joinRoom(username, roomId, roomPassword);
    }

}