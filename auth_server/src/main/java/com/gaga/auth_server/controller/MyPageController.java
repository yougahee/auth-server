package com.gaga.auth_server.controller;

import com.gaga.auth_server.dto.message.Message;
import com.gaga.auth_server.service.MyPageService;
import com.gaga.auth_server.utils.ResponseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin("*")
@RequestMapping("/mypage")
@RequiredArgsConstructor
@RestController
public class MyPageController {
    private final MyPageService myPageService;

    @GetMapping("")
    public ResponseEntity<Message> getMyProfileInfo(@RequestHeader(value = "x-forward-email") String email) {
        return ResponseEntity.status(HttpStatus.OK).body(new Message(myPageService.getMyProfile(email), ResponseMessage.GET_MY_PAGE));
    }

    @GetMapping("/point/{coin}")
    public ResponseEntity<Message> updatePoint(@RequestHeader(value = "x-forward-email") String email,
                                               @PathVariable("coin") int coin) {
        long point = myPageService.updatePoint(email, coin);
        return ResponseEntity.ok().body(new Message(point, ResponseMessage.POINT_UPDATE_SUCCESS));
    }
}
