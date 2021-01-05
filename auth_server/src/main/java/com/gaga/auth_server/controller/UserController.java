package com.gaga.auth_server.controller;

import com.gaga.auth_server.dto.Message;
import com.gaga.auth_server.dto.request.*;
import com.gaga.auth_server.dto.response.*;
import com.gaga.auth_server.service.UserService;
import com.gaga.auth_server.utils.ResponseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    private ResponseMessage responseMSG;

    @PostConstruct
    protected void init() {
        defaultResponseDTO = new DefaultResponseDTO();
        responseMSG = new ResponseMessage();
    }

    @GetMapping("/")
    public String getState() {
        return "SEVER IS RUNNING";
    }

    @PostMapping("/login")
    public ResponseEntity<Message> login(@RequestBody LoginDTO loginDTO) {
        TokenDTO tokenDTO = userService.getUserToken(loginDTO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Message(tokenDTO, responseMSG.LOG_IN_SUCCESS));
    }

    @PostMapping("/signup")
    public ResponseEntity<Message> signUp(@Valid @RequestBody UserInfoRequestDTO userInfo) {
        userService.insertUser(userInfo);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Message(responseMSG.SIGN_UP_SUCCESS));
    }

    @PostMapping("/check/nickname")
    public ResponseEntity<Message> checkNickName(@Valid @RequestBody UserNicknameDTO userInfo) {
        userService.checkNickname(userInfo.getNickname());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Message(responseMSG.CAN_USE_NICKNAME));
    }

    @PostMapping("/check/email")
    public ResponseEntity<Message> sendEmail(@Valid @RequestBody UserEmailDTO emailInfo) {
        userService.sendEmail(emailInfo.getEmail().toLowerCase());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Message(responseMSG.SEND_EMAIL));
    }

    @PostMapping("/check/email-code")
    public ResponseEntity<DefaultResponseDTO> checkEmailCode(@Valid @RequestBody EmailAuthorizationDTO userInfo) {
        defaultResponseDTO = userService.checkEmailCode(userInfo.getEmail().toLowerCase(), userInfo.getCode());
        return ResponseEntity.ok().body(defaultResponseDTO);
    }

    @GetMapping("/refresh")
    public ResponseEntity<TokenDTO> reissueToken(@RequestHeader(value = "refresh_token") String refreshToken) {
        TokenDTO tokenResponseDTO = userService.getReissueToken(refreshToken);
        return ResponseEntity.status(HttpStatus.OK).body(tokenResponseDTO);
    }

    @PostMapping("/find-pw")
    public ResponseEntity<DefaultResponseDTO> findPassword(@Valid @RequestBody UserEmailDTO emailInfo) {
        defaultResponseDTO = userService.findPassword(emailInfo.getEmail().toLowerCase());
        return ResponseEntity.ok().body(defaultResponseDTO);
    }
}
