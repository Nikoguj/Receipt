package com.back.receipt.vaadin.admin;

import com.back.receipt.domain.Receipt;
import com.back.receipt.domain.Room;
import com.back.receipt.domain.dto.UsersAdminDto;
import com.back.receipt.domain.mapper.UserMapper;
import com.back.receipt.exception.MyResourceNotFoundException;
import com.back.receipt.security.domain.Role;
import com.back.receipt.security.domain.User;
import com.back.receipt.security.domain.UserDetails;
import com.back.receipt.security.repository.RoleRepository;
import com.back.receipt.service.UserService;
import com.back.receipt.vaadin.LoginUI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

@Route("admin/users")
@PageTitle("Admin Users")
public class AdminUsers extends VerticalLayout implements AfterNavigationObserver, BeforeEnterObserver {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserMapper userMapper;

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        Grid<UsersAdminDto> usersAdminDtoGrid = new Grid<>();
        usersAdminDtoGrid.setSizeFull();

        usersAdminDtoGrid.addColumn(UsersAdminDto::getId)
                .setHeader("Id")
                .setWidth("5%");

        usersAdminDtoGrid.addColumn(UsersAdminDto::getUsername)
                .setHeader("Username")
                .setWidth("15%");

        usersAdminDtoGrid.addColumn(UsersAdminDto::getEmail)
                .setHeader("Email")
                .setWidth("30%");

        usersAdminDtoGrid.addColumn(UsersAdminDto::isActive)
                .setHeader("Active")
                .setWidth("10%");

        usersAdminDtoGrid.addColumn(UsersAdminDto::getRole)
                .setHeader("Role")
                .setWidth("40%");

        usersAdminDtoGrid.setItems(userMapper.mapToUsersAdminDto(userService.getUsersList()));

        add(usersAdminDtoGrid);
        setSizeFull();

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = null;
        try {
            user = userService.getUserByUsername(userDetails.getUsername());
        } catch (MyResourceNotFoundException e) {
            e.printStackTrace();
        }
        if(user != null) {
            Role role = roleRepository.findByRole("ROLE_ADMIN").get();

            if(!user.getRoles().contains(role)) {
                event.rerouteTo(LoginUI.class);
            }

        }
    }
}
