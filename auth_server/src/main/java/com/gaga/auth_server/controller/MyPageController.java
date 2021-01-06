package com.gaga.auth_server.controller;

import com.gaga.auth_server.dto.Message;
import com.gaga.auth_server.model.User;
import com.gaga.auth_server.service.MyPageService;
import com.gaga.auth_server.utils.ResponseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.annotation.PostConstruct;

@Slf4j
@CrossOrigin("*")
@RequestMapping("/mypage")
@RequiredArgsConstructor
@RestController
public class MyPageController {
    private final MyPageService myPageService;
    private ResponseMessage ResponseMessage;

    @PostConstruct
    protected void init() {
        ResponseMessage = new ResponseMessage();
    }

    @GetMapping("")
    public ResponseEntity<Message> getMyProfileInfo(@RequestHeader(value = "authorization") String token) {
        //##토큰으로 확인을 해야함
        log.info("myprofile start");
        log.info(token);
        User user = myPageService.getMyProfile();
        return ResponseEntity.status(HttpStatus.OK).body(new Message(ResponseMessage.GET_MY_PAGE));
    }

    @GetMapping("/point/{coin}")
    public ResponseEntity<Message> updatePoint(@RequestHeader(value = "authorization") String token,
                                               @PathVariable("coin") int coin) {
        //## token에서 email 꺼내주기
        int point = myPageService.updatePoint("ahrfus34@gmail.com", coin);
        return ResponseEntity.ok().body(new Message(point, ResponseMessage.POINT_UPDATE_SUCCESS));
    }
}
