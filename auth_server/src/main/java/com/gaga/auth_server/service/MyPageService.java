package com.gaga.auth_server.service;

import com.gaga.auth_server.dto.response.DataResponseDTO;
import com.gaga.auth_server.dto.response.DefaultResponseDTO;
import com.gaga.auth_server.exception.NoExistEmailException;
import com.gaga.auth_server.model.User;
import com.gaga.auth_server.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class MyPageService {
    private final UserService userService;
    private final UserInfoRepository userInfoRepository;

    public DataResponseDTO getMyProfile() {
        return new DataResponseDTO("");
    }

    @Transactional
    public DefaultResponseDTO updatePoint(String email, int coin) {

        User user = userService.findByEmail(email);
        int totalPoint = user.getPoint() + coin;

        //coin 충전은 양수, coin 사용은 마이너스로
        if(totalPoint < 0) return new DefaultResponseDTO();
        user.setPoint(totalPoint);
        userInfoRepository.save(user);

        return new DefaultResponseDTO("포인트 update");
    }
}
