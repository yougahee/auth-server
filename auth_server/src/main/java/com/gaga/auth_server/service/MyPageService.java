package com.gaga.auth_server.service;

import com.gaga.auth_server.exception.NoNegativeNumberException;
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
    private ResponseMessage responseMsg;

    @PostConstruct
    protected void init() {
        responseMsg = new ResponseMessage();
    }

    //## mypage에서 어떤 것들이 필요한지 찾아보자
    public User getMyProfile() {
        User user = new User();

        return user;
    }

    @Transactional
    public int updatePoint(String email, int coin) {

        User user = userService.findByEmailOrThrow(email);
        int totalPoint = user.getPoint() + coin;

        if(totalPoint < 0) throw new NoNegativeNumberException(responseMsg.POINT_UPDATE_FAIL);
        user.setPoint(totalPoint);
        userInfoRepository.save(user);

        return totalPoint;
    }
}
