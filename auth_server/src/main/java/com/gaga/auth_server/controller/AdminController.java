package com.gaga.auth_server.controller;

import com.gaga.auth_server.dto.Message;
import com.gaga.auth_server.dto.UserDTO;
import com.gaga.auth_server.service.AdminService;
import com.gaga.auth_server.utils.ResponseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@CrossOrigin("*")
@RequestMapping("/admin")
@RestController
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    private ResponseMessage responseMessage;

    @PostConstruct
    protected void init() {
        responseMessage = new ResponseMessage();
    }

    @GetMapping("/users")
    public ResponseEntity<Message> getAllUsers(@PageableDefault Pageable pageable,
            @RequestHeader(value = "x-forward-email") String email) {

        List<UserDTO> users = adminService.getAllUsers(email, pageable);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Message(users, responseMessage.GET_ALL_USERS_SUCCESS));
    }
}
