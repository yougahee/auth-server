package com.gaga.auth_server.service;

import com.gaga.auth_server.dto.UserDTO;
import com.gaga.auth_server.exception.NoExistEmailException;
import com.gaga.auth_server.exception.UnauthorizedException;
import com.gaga.auth_server.model.User;
import com.gaga.auth_server.repository.UserInfoRepository;
import com.gaga.auth_server.utils.ResponseMessage;
import com.gaga.auth_server.utils.UserGrade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminService {
    private final UserInfoRepository userInfoRepository;
    private ResponseMessage responseMSG;
    private UserGrade userGrade;

    @PostConstruct
    protected void init() {
        responseMSG = new ResponseMessage();
        userGrade = new UserGrade();
    }

    public List<UserDTO> getAllUsers(String email, Pageable pageable) {
        User user = userInfoRepository.findByEmail(email)
                .orElseThrow(() -> new NoExistEmailException(responseMSG.NOT_FOUND_EMAIL));

        if(user.getGrade() != userGrade.MANAGER)
            throw new UnauthorizedException(responseMSG.NO_AUTHORIZATION);

        Page<User> users = userInfoRepository.findAll(pageable);
        return users.stream().map(UserDTO::fromEntity).collect(Collectors.toList());
    }
}
