package com.gaga.auth_server.controller;

import com.gaga.auth_server.dto.response.DataResponseDTO;
import com.gaga.auth_server.dto.response.DefaultResponseDTO;
import com.gaga.auth_server.service.MyPageService;
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
public class MypageController {
    private final MyPageService myPageService;
    private DefaultResponseDTO defaultResponseDTO;

    @PostConstruct
    protected void init() {
        defaultResponseDTO = new DefaultResponseDTO();
    }

    @GetMapping("")
    public ResponseEntity<DataResponseDTO> getMyProfileInfo() {
        DataResponseDTO dataResponseDTO = myPageService.getMyProfile();
        return ResponseEntity.ok().body(dataResponseDTO);
    }

    @GetMapping("/point/{coin}")
    public ResponseEntity<DefaultResponseDTO> updatePoint(@RequestHeader String token, @PathVariable("coin") int coin) {
        //token유효성검사는 APIgateway에서 한다면, email이 와야함.
        defaultResponseDTO = myPageService.updatePoint(token, coin);
        return ResponseEntity.ok().body(defaultResponseDTO);
    }
}
