package com.gaga.auth_server.controller;

import com.gaga.auth_server.dto.Message;
import com.gaga.auth_server.service.MyPageService;
import com.gaga.auth_server.utils.ResponseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private ResponseMessage responseMessage;

    @PostConstruct
    protected void init() {
        responseMessage = new ResponseMessage();
    }

    @GetMapping("")
    public ResponseEntity<Message> getMyProfileInfo(@RequestHeader String token) {
        //##토큰으로 확인을 해야함
        myPageService.getMyProfile();
        return ResponseEntity.ok().body(new Message(responseMessage.GET_MY_PAGE));
    }

    @GetMapping("/point/{coin}")
    public ResponseEntity<Message> updatePoint(@RequestHeader String token, @PathVariable("coin") int coin) {
        //token유효성검사는 APIgateway에서 한다면, email이 와야함.
        int point = myPageService.updatePoint(token, coin);
        return ResponseEntity.ok().body(new Message(point, responseMessage.POINT_UPDATE_SUCCESS));
    }
}
