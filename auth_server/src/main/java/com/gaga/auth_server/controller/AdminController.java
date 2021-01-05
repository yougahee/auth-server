package com.gaga.auth_server.controller;

import com.gaga.auth_server.dto.Message;
import com.gaga.auth_server.dto.response.SimpleUserDTO;
import com.gaga.auth_server.enums.TokenEnum;
import com.gaga.auth_server.service.AdminService;
import com.gaga.auth_server.utils.JwtUtils;
import com.gaga.auth_server.utils.ResponseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final JwtUtils jwtUtils;
    private ResponseMessage responseMessage;

    @PostConstruct
    protected void init() {
        responseMessage = new ResponseMessage();
    }

    @GetMapping("/users")
    public ResponseEntity<Message> getAllUsers(@RequestHeader(value = "token") String token) {
        jwtUtils.isValidateToken(token, TokenEnum.ACCESS);

        List<SimpleUserDTO> users = adminService.getAllUsers();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Message(users, responseMessage.GET_ALL_USERS_SUCCESS));
    }
}
