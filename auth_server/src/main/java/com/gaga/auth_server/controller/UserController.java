package com.gaga.auth_server.controller;

import com.gaga.auth_server.dto.request.*;
import com.gaga.auth_server.dto.response.*;
import com.gaga.auth_server.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.annotation.PostConstruct;
import javax.validation.Valid;

@Slf4j
@CrossOrigin("*")
@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;
    private DefaultResponseDTO defaultResponseDTO;

    @PostConstruct
    protected void init() {
        defaultResponseDTO = new DefaultResponseDTO();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginTokenResponseDTO> login(@RequestBody UserLogInRequestDTO loginInfo) {
        LoginTokenResponseDTO loginTokenResponseDTO = userService.getUserToken(loginInfo);
        return ResponseEntity.ok().body(loginTokenResponseDTO);
    }

    @PostMapping("/signup")
    public ResponseEntity<DefaultResponseDTO> signUp(@Valid @RequestBody UserInfoRequestDTO userInfo) {
        defaultResponseDTO = userService.insertUser(userInfo);
        return ResponseEntity.ok().body(defaultResponseDTO);
    }

    @PostMapping("/check/nickname")
    public ResponseEntity<DefaultResponseDTO> checkNickName(@Valid @RequestBody UserNicknameRequestDTO userInfo) {
        defaultResponseDTO = userService.checkNickname(userInfo.getNickname());
        return ResponseEntity.ok().body(defaultResponseDTO);
    }

    @PostMapping("/check/email")
    public ResponseEntity<DefaultResponseDTO> sendEmail(@Valid @RequestBody UserEmailIdRequestDTO emailInfo) {
        defaultResponseDTO = userService.sendEmail(emailInfo.getEmail().toLowerCase());
        return ResponseEntity.ok().body(defaultResponseDTO);
    }

    @PostMapping("/check/email-code")
    public ResponseEntity<DefaultResponseDTO> checkEmailCode(@Valid @RequestBody EmailAuthorizationDTO userInfo) {
        defaultResponseDTO = userService.checkEmailCode(userInfo.getEmail().toLowerCase(), userInfo.getCode());
        return ResponseEntity.ok().body(defaultResponseDTO);
    }

    @GetMapping("/refresh")
    public ResponseEntity<TokenResponseDTO> reissueToken(@RequestHeader(value = "refresh_token") String refreshToken) {
        TokenResponseDTO tokenResponseDTO = userService.getReissueToken(refreshToken);
        return ResponseEntity.ok().body(tokenResponseDTO);
    }

    @PostMapping("/find-pw")
    public ResponseEntity<DefaultResponseDTO> findPassword(@Valid @RequestBody UserEmailIdRequestDTO emailInfo) {
        defaultResponseDTO = userService.findPassword(emailInfo.getEmail().toLowerCase());
        return ResponseEntity.ok().body(defaultResponseDTO);
    }
}
