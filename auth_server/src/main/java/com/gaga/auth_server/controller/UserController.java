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
    private ResponseMessage ResponseMSG;

    @PostConstruct
    protected void init() {
        ResponseMSG = new ResponseMessage();
    }

    @GetMapping("/")
    public String getState() {
        return "SEVER IS RUNNING";
    }

    @GetMapping("/test/remove/{email}")
    public ResponseEntity<Message> testDeleteEmail(@PathVariable("email") String email) {
        userService.removeEmailRecord(email);
        return ResponseEntity.ok().body(new Message(ResponseMSG.TEST_DELETE_EMAIL_RECORD));
    }

    @PostMapping("/login")
    public ResponseEntity<Message> login(@RequestBody LoginDTO loginDTO) {
        TokenDTO tokenDTO = userService.getUserToken(loginDTO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Message(tokenDTO, ResponseMSG.LOG_IN_SUCCESS));
    }

    @PostMapping("/signup")
    public ResponseEntity<Message> signUp(@Valid @RequestBody UserInfoRequestDTO userInfo) {
        userService.insertUser(userInfo);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Message(ResponseMSG.SIGN_UP_SUCCESS));
    }

    @PostMapping("/check/nickname")
    public ResponseEntity<Message> checkNickName(@Valid @RequestBody UserNicknameDTO nicknameDTO) {
        userService.checkNickname(nicknameDTO.getNickname());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Message(ResponseMSG.CAN_USE_NICKNAME));
    }

    @PostMapping("/check/email")
    public ResponseEntity<Message> sendEmail(@Valid @RequestBody UserEmailDTO userEmailDTO) {
        userService.sendEmail(userEmailDTO.getEmail().toLowerCase());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Message(ResponseMSG.SEND_EMAIL));
    }

    @PostMapping("/check/email-code")
    public ResponseEntity<Message> checkEmailCode(@Valid @RequestBody EmailAuthorizationDTO emailDTO) {
        userService.checkEmailCode(emailDTO.getEmail().toLowerCase(), emailDTO.getCode());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Message(ResponseMSG.CERTIFICATE_EMAIL));
    }

    @GetMapping("/refresh")
    public ResponseEntity<Message> reissueToken(@RequestHeader(value = "refresh_token") String refreshToken) {
        TokenDTO tokenDTO = userService.getReissueToken(refreshToken);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Message(tokenDTO, ResponseMSG.REISSUE_REFRESH_TOKEN));
    }

    @PostMapping("/find-pw")
    public ResponseEntity<Message> findPassword(@Valid @RequestBody UserEmailDTO emailInfo) {
        //## token 보내면 email 보낼 필요 없음
        userService.findPassword(emailInfo.getEmail().toLowerCase());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new Message(ResponseMSG.SEND_TEMP_PW_CODE));
    }
}
