package com.gaga.auth_server.controller;

import com.gaga.auth_server.dto.response.DataResponseDTO;
import com.gaga.auth_server.enums.TokenEnum;
import com.gaga.auth_server.service.AdminService;
import com.gaga.auth_server.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin("*")
@RequestMapping("/admin")
@RestController
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    private final JwtUtils jwtUtils;

    @GetMapping("/users")
    public ResponseEntity<DataResponseDTO> getAllUsers(@RequestHeader(value = "token") String token) {
        jwtUtils.isValidateToken(token, TokenEnum.ACCESS);

        DataResponseDTO dataResponseDTO = adminService.getAllUsers();
        return ResponseEntity.ok().body(dataResponseDTO);
    }
}
