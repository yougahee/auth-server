package com.gaga.auth_server.service;

import com.gaga.auth_server.dto.response.MyPageDTO;
import com.gaga.auth_server.exception.NoNegativeNumberException;
import com.gaga.auth_server.exception.NotFoundException;
import com.gaga.auth_server.model.User;
import com.gaga.auth_server.repository.UserInfoRepository;
import com.gaga.auth_server.utils.ResponseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class MyPageService {
    private final UserService userService;
    private final UserInfoRepository userInfoRepository;
    private ResponseMessage ResponseMessage;

    @PostConstruct
    protected void init() {
        ResponseMessage = new ResponseMessage();
    }

    public MyPageDTO getMyProfile(String email) {
        log.info("my profile get");
        User user = userInfoRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(ResponseMessage.NOT_FOUND_EMAIL));
        return MyPageDTO.builder()
                .nickname(user.getNickname())
                .point(user.getPoint())
                .build();
    }

    @Transactional
    public long updatePoint(String email, int coin) {
        User user = userService.findByEmailOrThrow(email);
        long totalPoint = user.getPoint() + coin;

        if(totalPoint < 0) throw new NoNegativeNumberException(ResponseMessage.POINT_UPDATE_FAIL);
        user.setPoint(totalPoint);
        userInfoRepository.save(user);

        return totalPoint;
    }
}
