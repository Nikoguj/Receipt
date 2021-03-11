package com.back.receipt.vaadin;

import com.back.receipt.domain.Room;
import com.back.receipt.domain.dto.RoomDto;
import com.back.receipt.domain.mapper.RoomMapper;
import com.back.receipt.exception.MyResourceNotFoundException;
import com.back.receipt.security.domain.UserDetails;
import com.back.receipt.service.UserService;
import com.back.receipt.domain.dto.JoinRoomDto;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.annotation.PostConstruct;

@Route("rooms")
@Secured({"ROLE_USER", "ROLE_ADMIN"})
@PageTitle("Rooms")
public class RoomsUI extends VerticalLayout {

    @Autowired
    private UserService userService;

    @Autowired
    private RoomMapper roomMapper;

    public RoomsUI() {
    }

    @PostConstruct
    public void init() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        SplitLayout splitLayout = new SplitLayout();

        Grid<RoomDto> roomGrid = new Grid<>();

        setupGrid(roomGrid);

        VerticalLayout firstColumnLayout = setupFirstColumn();

        Button addCreateRoomButton = addCreateRoomButton(userDetails, roomGrid);
        Details addJoinRoomDetails = addJoinRoomDetails(userDetails, roomGrid);
        ////////Details testerDetails = createTesterDetails();

        firstColumnLayout.add(addCreateRoomButton, addJoinRoomDetails);

        splitLayout.addToPrimary(firstColumnLayout);

        refreshRoomList(userDetails, roomGrid);

        splitLayout.addToSecondary(roomGrid);
        splitLayout.setSplitterPosition(20);

        add(splitLayout);

        roomGrid.setSizeFull();
        splitLayout.setSizeFull();
        setSizeFull();
    }

    private void setupGrid(Grid<RoomDto> roomGrid) {
        roomGrid.addColumn(RoomDto::getId)
                .setHeader("Id")
                .setWidth("25%");

        roomGrid.addColumn(RoomDto::getPassword)
                .setHeader("Password")
                .setWidth("25%");

        roomGrid.addColumn(RoomDto::getUserList)
                .setHeader("User List")
                .setWidth("25%");

        roomGrid.addColumn(RoomDto::getNumberOfReceipts)
                .setHeader("Number Of Receipts")
                .setWidth("25%");

        roomGrid.getStyle().set("cursor", "pointer");
        roomGrid.addItemClickListener(event -> {
            UI.getCurrent().navigate(RoomUI.class, event.getItem().getId());
        });
    }

    private VerticalLayout setupFirstColumn() {
        VerticalLayout firstLayout = new VerticalLayout();
        firstLayout.setSizeFull();
        firstLayout.setAlignItems(Alignment.CENTER);
        return firstLayout;
    }

    private Details addJoinRoomDetails(UserDetails userDetails, Grid<RoomDto> roomGrid) {

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setWidth("85%");

        Binder<JoinRoomDto> joinRoomDtoBinder = new com.vaadin.flow.data.binder.Binder<>(JoinRoomDto.class);
        JoinRoomDto joinRoomDto = new JoinRoomDto();
        joinRoomDtoBinder.setBean(joinRoomDto);

        TextField idTextField = new TextField();
        idTextField.setWidth("100%");
        idTextField.setLabel("Room ID");

        joinRoomDtoBinder.forField(idTextField)
                .withValidator(value -> value != null, "ID can't be empty")
                .withValidator(value -> value.matches("[0-9]+"), "ID is only numbers")
                .withValidator((value, content) -> {
                    ValidationResult validationResult = ValidationResult.ok();
                    try {
                        for (Room room : userService.getRoomList(userDetails.getUsername())) {
                            if (room.getId() == Long.valueOf(value)) {
                                validationResult = ValidationResult.error("You are already in this room");
                            }
                        }
                    } catch (MyResourceNotFoundException e) {
                        e.printStackTrace();
                    }
                    return validationResult;
                })
                .bind(JoinRoomDto::getId, JoinRoomDto::setId);


        TextField passwordTextField = new TextField();
        passwordTextField.setWidth("100%");
        passwordTextField.setLabel("Room password");

        joinRoomDtoBinder.forField(passwordTextField)
                .withValidator(value -> value != null, "Password can't be empty")
                .bind(JoinRoomDto::getPassword, JoinRoomDto::setPassword);

        Button joinRoomButton = new Button("Join");
        joinRoomButton.setWidth("100%");

        Label incorrectLabel = new Label("Incorrect id or password");
        incorrectLabel.setVisible(false);

        joinRoomButton.addClickListener(event -> {
            if (joinRoomDtoBinder.writeBeanIfValid(joinRoomDto)) {
                try {
                    userService.joinRoom(userDetails.getUsername(), Long.valueOf(joinRoomDto.getId()), joinRoomDto.getPassword());
                    refreshRoomList(userDetails, roomGrid);
                    incorrectLabel.setVisible(false);
                } catch (MyResourceNotFoundException e) {
                    incorrectLabel.setVisible(true);
                }
            } else {
                incorrectLabel.setVisible(true);
            }
        });

        verticalLayout.add(idTextField, passwordTextField, joinRoomButton, incorrectLabel);

        Details joinRoomDetails = new Details("Join a room", verticalLayout);

        return joinRoomDetails;
    }

    private void refreshRoomList(UserDetails userDetails, Grid<RoomDto> roomGrid) {
        try {
            roomGrid.setItems(roomMapper.mapToRoomDtoList(userService.getRoomList(userDetails.getUsername())));
        } catch (MyResourceNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Button addCreateRoomButton(UserDetails userDetails, Grid<RoomDto> roomGrid) {
        Button createRoomButton = new Button("Create new room");
        createRoomButton.setWidth("85%");

        createRoomButton.addClickListener(clickEvent -> {
            userService.createRoom(userDetails.getUsername());
            refreshRoomList(userDetails, roomGrid);
        });

        return createRoomButton;
    }


}
