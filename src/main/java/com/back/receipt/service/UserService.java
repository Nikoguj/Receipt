package com.back.receipt.service;

import com.back.receipt.config.RoomPasswordGenerator;
import com.back.receipt.domain.Room;
import com.back.receipt.exception.MyResourceNotFoundException;
import com.back.receipt.repository.RoomRepository;
import com.back.receipt.security.domain.User;
import com.back.receipt.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomPasswordGenerator roomPasswordGenerator;

    public User getUser(final Long userId) throws MyResourceNotFoundException {
        Optional<User> userOptional = userRepository.findById(userId);
        userOptional.orElseThrow( () -> new MyResourceNotFoundException("User not found"));
        User user = userOptional.get();

        return user;
    }

    public User getUserByUsername(final String username) throws MyResourceNotFoundException {
        Optional<User> userOptional = userRepository.findByUserName(username);
        userOptional.orElseThrow( () -> new MyResourceNotFoundException("User not found"));
        User user = userOptional.get();

        return user;
    }

    public Room createRoom(final String username) {
        Optional<User> userOptional = userRepository.findByUserName(username);
        userOptional.orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        User user = userOptional.get();

        Room room = new Room();
        room.setPassword(roomPasswordGenerator.generate());
        room.getUserList().add(user);
        user.getRoomList().add(room);

        roomRepository.save(room);
        userRepository.save(user);

        return room;
    }

    public List<Room> getRoomList(final String username) throws MyResourceNotFoundException {
        Optional<User> userOptional = userRepository.findByUserName(username);
        userOptional.orElseThrow(() -> new MyResourceNotFoundException("Username not found"));
        User user = userOptional.get();

        return user.getRoomList();
    }

    public void joinRoom(final String username, final Long roomId, final String roomPassword) throws MyResourceNotFoundException {
        Optional<User> userOptional = userRepository.findByUserName(username);
        userOptional.orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        User user = userOptional.get();

        Optional<Room> roomOptional = roomRepository.findById(roomId);
        roomOptional.orElseThrow(() -> new MyResourceNotFoundException("Username not found"));
        Room room = roomOptional.get();

        if(roomPassword.equals(room.getPassword())) {
            user.getRoomList().add(room);
            room.getUserList().add(user);

            roomRepository.save(room);
            userRepository.save(user);

            roomService.setPartPriceForNewUser(roomId, user.getId());
        } else {
            throw new MyResourceNotFoundException("Wrong Password");
        }

    }

    public List<User> getUsersList() {
        return (List<User>) userRepository.findAll();
    }


}
