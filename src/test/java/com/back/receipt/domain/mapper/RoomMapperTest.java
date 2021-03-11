package com.back.receipt.domain.mapper;

import com.back.receipt.domain.Receipt;
import com.back.receipt.domain.Room;
import com.back.receipt.domain.dto.RoomDto;
import com.back.receipt.security.domain.User;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
class RoomMapperTest {

    @Autowired
    private RoomMapper roomMapper;

    @Test
    void mapToRoomDtoList() {
        //Given
        List<User> userList1 = new ArrayList<>(Arrays.asList(
                new User(1L, "user1", "user1", true),
                new User(2L, "user2", "2", true)
        ));
        List<Receipt> receiptList1 = new ArrayList<>(Arrays.asList(
                new Receipt(1L, "adress1", "date1", 10.0),
                new Receipt(2L, "adress2", "date2", 20.0)
        ));
        Room room1 = new Room(1L, "password1", userList1, receiptList1);

        List<User> userList2 = new ArrayList<>(Arrays.asList(
                new User(1L, "user3", "user1", true),
                new User(2L, "user4", "2", true)
        ));
        List<Receipt> receiptList2 = new ArrayList<>(Arrays.asList(
                new Receipt(1L, "adress3", "date3", 30.0),
                new Receipt(2L, "adress4", "date4", 40.0)
        ));
        Room room2 = new Room(1L, "password2", userList2, receiptList2);

        String exceptedUserList1 = "user1, user2";

        String exceptedUserList2 = "user3, user4";

        List<Room> roomList = new ArrayList<>(Arrays.asList(
                room1, room2
        ));

        //When
        List<RoomDto> returnRoomDtoList = roomMapper.mapToRoomDtoList(roomList);

        //Then
        Assert.assertEquals(2, returnRoomDtoList.size());
        Assert.assertEquals(room1.getPassword(), returnRoomDtoList.get(0).getPassword());
        Assert.assertEquals(room1.getReceiptList().size(), returnRoomDtoList.get(0).getNumberOfReceipts());
        Assert.assertEquals(exceptedUserList1, returnRoomDtoList.get(0).getUserList());

        Assert.assertEquals(room2.getPassword(), returnRoomDtoList.get(1).getPassword());
        Assert.assertEquals(room2.getReceiptList().size(), returnRoomDtoList.get(1).getNumberOfReceipts());
        Assert.assertEquals(exceptedUserList2, returnRoomDtoList.get(1).getUserList());
    }

    @Test
    void mapToRoomDto() {
        //Given
        List<User> userList = new ArrayList<>(Arrays.asList(
                new User(1L, "user1", "user1", true),
                new User(2L, "user2", "2", true)
        ));
        List<Receipt> receiptList = new ArrayList<>(Arrays.asList(
                new Receipt(1L, "adress1", "date1", 10.0),
                new Receipt(2L, "adress2", "date2", 20.0)
        ));
        Room room = new Room(1L, "password", userList, receiptList);

        String exceptedUserList = "user1, user2";

        //When
        RoomDto returnRoomDto = roomMapper.mapToRoomDto(room);

        //Then
        Assert.assertEquals(room.getId(), returnRoomDto.getId());
        Assert.assertEquals(room.getPassword(), returnRoomDto.getPassword());
        Assert.assertEquals(room.getReceiptList().size(), returnRoomDto.getNumberOfReceipts());
        Assert.assertEquals(exceptedUserList, returnRoomDto.getUserList());
    }
}