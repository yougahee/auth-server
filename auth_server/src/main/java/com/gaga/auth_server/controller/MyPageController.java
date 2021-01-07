package com.gaga.auth_server.controller;

import com.gaga.auth_server.dto.Message;
import com.gaga.auth_server.model.User;
import com.gaga.auth_server.service.MyPageService;
import com.gaga.auth_server.utils.JwtUtils;
import com.gaga.auth_server.utils.ResponseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@Slf4j
@CrossOrigin("*")
@RequestMapping("/mypage")
@RequiredArgsConstructor
@RestController
public class MyPageController {
    private final MyPageService myPageService;
    private final JwtUtils jwtUtils;
    private ResponseMessage ResponseMessage;

    @PostConstruct
    protected void init() { ResponseMessage = new ResponseMessage(); }

    @GetMapping("")
    public ResponseEntity<Message> getMyProfileInfo(@RequestHeader(value = "x-forward-email") String email) {
        User user = myPageService.getMyProfile(email);
        return ResponseEntity.status(HttpStatus.OK).body(new Message(ResponseMessage.GET_MY_PAGE));
    }

    @GetMapping("/point/{coin}")
    public ResponseEntity<Message> updatePoint(@RequestHeader(value = "token") String token,
                                               @RequestHeader(value = "x-forward-email") String email,
                                               @PathVariable("coin") int coin) {
        int point = myPageService.updatePoint(email, coin);
        return ResponseEntity.ok().body(new Message(point, ResponseMessage.POINT_UPDATE_SUCCESS));
    }
}
