package com.back.receipt.domain.mapper;

import com.back.receipt.domain.Room;
import com.back.receipt.domain.dto.RoomDto;
import com.back.receipt.security.domain.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoomMapper {

    public List<RoomDto> mapToRoomDtoList(final List<Room> roomList) {
        return roomList.stream().map(this::mapToRoomDto).collect(Collectors.toList());
    }

    public RoomDto mapToRoomDto(final Room room) {
        RoomDto roomDto = new RoomDto();
        roomDto.setId(room.getId());
        roomDto.setPassword(room.getPassword());
        roomDto.setNumberOfReceipts(room.getReceiptList().size());
        List<String> userListString = room.getUserList().stream().map(User::getUserName).collect(Collectors.toList());
        String s = listToString(userListString);
        roomDto.setUserList(s);
        return roomDto;
    }

    private String listToString(List<String> stringList) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < stringList.size(); i++) {
            if(i == stringList.size()-1) {
                str.append(stringList.get(i));
            } else {
                str.append(stringList.get(i) + ", ");
            }
        }

        return str.toString();
    }
}
