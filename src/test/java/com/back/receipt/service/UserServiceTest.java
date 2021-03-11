package com.back.receipt.service;

import com.back.receipt.config.RoomPasswordGenerator;
import com.back.receipt.domain.Room;
import com.back.receipt.exception.MyResourceNotFoundException;
import com.back.receipt.repository.RoomRepository;
import com.back.receipt.security.config.PasswordConfig;
import com.back.receipt.security.domain.Role;
import com.back.receipt.security.domain.User;
import com.back.receipt.security.repository.RoleRepository;
import com.back.receipt.security.repository.UserRepository;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordConfig passwordConfig;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoomPasswordGenerator roomPasswordGenerator;

    @Autowired
    private RoomRepository roomRepository;

    private User user = new User();

    @BeforeEach
    public void initz() {
        user.setUserName("testUser");
        user.setPassword(passwordConfig.passwordEncoder().encode("test"));
        user.setEmail("user3@user.com");

        createRoleIfNotFound("ROLE_ADMIN");
        Optional<Role> adminRole = roleRepository.findByRole("ROLE_ADMIN");

        user.setRoles(Collections.singletonList(adminRole.get()));
        user.setActive(true);
        userRepository.save(user);
    }

    @Test
    public void createRoom() {
        //Given

        //When
        userService.createRoom(user.getUserName());

        Room returnRoom = roomRepository.findFirstBy().get();
        User returnUser = userRepository.findByUserName(user.getUserName()).get();

        //Then
        Assert.assertNotEquals(Optional.of(0), returnRoom.getId());
        Assert.assertNotEquals(Optional.of(0), returnRoom.getPassword());
        Assert.assertEquals(user.getUserName(), returnRoom.getUserList().get(0).getUserName());

        //Clean up
        returnUser.getRoomList().remove(returnRoom);
        returnRoom.getUserList().remove(returnUser);
        roomRepository.save(returnRoom);
        userRepository.save(returnUser);
        roomRepository.delete(returnRoom);
        userRepository.delete(user);
    }

    @Transactional
    Role createRoleIfNotFound(String roleName) {

        Optional<Role> findRole = roleRepository.findByRole(roleName);
        if (!findRole.isPresent()) {
            Role newRole = new Role(roleName);
            roleRepository.save(newRole);
            return newRole;
        }
        return findRole.get();
    }

    @Test
    public void getRoomListTest() throws MyResourceNotFoundException {
        //Given
        Room room1 = userService.createRoom(user.getUserName());
        Room room2 = userService.createRoom(user.getUserName());
        Room room3 = userService.createRoom(user.getUserName());
        Room room4 = userService.createRoom(user.getUserName());

        //When
        List<Room> returnRoomList = userService.getRoomList(user.getUserName());
        User returnUser = userRepository.findByUserName(user.getUserName()).get();

        //Then
        Assert.assertEquals(4, returnRoomList.size());
        Assert.assertTrue(returnRoomList.contains(room1));
        Assert.assertTrue(returnRoomList.contains(room2));
        Assert.assertTrue(returnRoomList.contains(room3));
        Assert.assertTrue(returnRoomList.contains(room4));

        //Clean up
        returnUser.getRoomList().clear();
        returnRoomList.get(0).getUserList().remove(returnUser);
        returnRoomList.get(1).getUserList().remove(returnUser);
        returnRoomList.get(2).getUserList().remove(returnUser);
        returnRoomList.get(3).getUserList().remove(returnUser);
        roomRepository.save(returnRoomList.get(0));
        roomRepository.save(returnRoomList.get(1));
        roomRepository.save(returnRoomList.get(2));
        roomRepository.save(returnRoomList.get(3));
        userRepository.save(returnUser);
        roomRepository.delete(returnRoomList.get(0));
        roomRepository.delete(returnRoomList.get(1));
        roomRepository.delete(returnRoomList.get(2));
        roomRepository.delete(returnRoomList.get(3));
        userRepository.delete(user);
    }

    @Test
    void joinRoom() throws MyResourceNotFoundException {
        //Given
        Room room = userService.createRoom(user.getUserName());

        User user2 = new User();
        user2.setUserName("testUser2");
        user2.setPassword(passwordConfig.passwordEncoder().encode("test2"));
        user2.setEmail("userTest2@user.com");

        createRoleIfNotFound("ROLE_ADMIN");
        Optional<Role> adminRole = roleRepository.findByRole("ROLE_ADMIN");

        user2.setRoles(Collections.singletonList(adminRole.get()));
        user2.setActive(true);
        userRepository.save(user2);

        //When
        userService.joinRoom(user2.getUserName(), room.getId(), room.getPassword());

        //Then
        User returnUser1 = userRepository.findByUserName(user2.getUserName()).get();
        Assert.assertEquals(1, returnUser1.getRoomList().size());
        Assert.assertEquals(room.getPassword(), returnUser1.getRoomList().get(0).getPassword());

        //Clean up
        User returnUser2 = userRepository.findByUserName(user.getUserName()).get();

        returnUser1.getRoomList().clear();
        userRepository.save(returnUser1);
        userRepository.delete(returnUser1);

        returnUser2.getRoomList().clear();
        userRepository.save(returnUser2);
        userRepository.delete(returnUser2);

        Room returnRoom = roomRepository.findFirstBy().get();
        returnRoom.getUserList().clear();
        roomRepository.save(returnRoom);
        roomRepository.delete(returnRoom);

    }
}