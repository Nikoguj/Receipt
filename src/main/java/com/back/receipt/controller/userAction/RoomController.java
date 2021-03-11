package com.back.receipt.controller.userAction;

import com.back.receipt.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/room")
public class RoomController {

    @Autowired
    private RoomService roomService;

/*    @PostMapping("/addReceipt/{username}/{roomId}/{shop}")
    @PreAuthorize("#username.equals(authentication.principal.username) || hasRole('ROLE_ADMIN')")
    public void addReceipt(@RequestParam("file") MultipartFile file, @PathVariable String username, @PathVariable Long roomId, @PathVariable String shop) throws Exception {
        roomService.addReceipt(file, username, roomId, shop);

    }*/
}
