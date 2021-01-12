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
    private ResponseMessage responseMessage;

    @PostConstruct
    protected void init() {
        responseMessage = new ResponseMessage();
    }

    @GetMapping("/")
    public String getState() {
        return "SEVER IS RUNNING";
    }

    @GetMapping("/test/remove/{email}")
    public ResponseEntity<Message> testDeleteEmail(@PathVariable("email") String email) {
        userService.removeEmailRecord(email);
        return ResponseEntity.ok().body(new Message(responseMessage.TEST_DELETE_EMAIL_RECORD));
    }

    @PostMapping("/login")
    public ResponseEntity<Message> login(@RequestBody LoginDTO loginDTO) {
        TokenDTO tokenDTO = userService.getUserToken(loginDTO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Message(tokenDTO, responseMessage.LOG_IN_SUCCESS));
    }

    @PostMapping("/signup")
    public ResponseEntity<Message> signUp(@Valid @RequestBody UserInfoRequestDTO userInfo) {
        userService.insertUser(userInfo);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Message(responseMessage.SIGN_UP_SUCCESS));
    }

    @PostMapping("/check/nickname")
    public ResponseEntity<Message> checkNickName(@Valid @RequestBody UserNicknameDTO nicknameDTO) {
        userService.checkNickname(nicknameDTO.getNickname());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Message(responseMessage.CAN_USE_NICKNAME));
    }

    @PostMapping("/check/email")
    public ResponseEntity<Message> sendEmail(@Valid @RequestBody UserEmailDTO userEmailDTO) {
        int code = userService.sendEmail(userEmailDTO.getEmail().toLowerCase());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Message("빠른테스트를 위해 : " + code , responseMessage.SEND_EMAIL));
    }

    @PostMapping("/check/email-code")
    public ResponseEntity<Message> checkEmailCode(@Valid @RequestBody EmailAuthorizationDTO emailDTO) {
        userService.checkEmailCode(emailDTO.getEmail().toLowerCase(), emailDTO.getCode());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Message(responseMessage.CERTIFICATE_EMAIL));
    }

    @GetMapping("/refresh")
    public ResponseEntity<Message> reissueToken(@RequestHeader(value = "refresh_token") String refreshToken) {
        TokenDTO tokenDTO = userService.getReissueToken(refreshToken);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Message(tokenDTO, responseMessage.REISSUE_REFRESH_TOKEN));
    }

    @GetMapping("/find-pw")
    public ResponseEntity<Message> findPassword(@RequestHeader(value = "x-forward-email") String email) {
        userService.findPassword(email);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Message(responseMessage.SEND_TEMP_PW_CODE));
    }

    @PostMapping("/change/pw")
    public ResponseEntity<Message> changePassword(@RequestHeader(value = "x-forward-email") String email,
                                                  @Valid @RequestBody UserPasswordDTO passwordDTO) {
        userService.changePassword(email, passwordDTO.getPassword());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Message(responseMessage.CHANGE_PW_SUCCESS));
    }

    @PostMapping("/change/nickname")
    public ResponseEntity<Message> changeNickName(@Valid @RequestHeader(value = "x-forward-email") String email,
                                                  @RequestBody UserNicknameDTO nicknameDTO) {
        userService.changeNickname(email, nicknameDTO.getNickname());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Message(responseMessage.CHANGE_NICKNAME_SUCCESS));
    }

    @GetMapping("/withdraw")
    public ResponseEntity<Message> leaveUser(@RequestHeader(value = "x-forward-email") String email) {
        userService.leaveUser(email);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Message(responseMessage.WITHDRAW_MORSE_SUCCESS));
    }
}
