package com.gaga.auth_server.service;

import com.gaga.auth_server.dto.response.SimpleUserDTO;
import com.gaga.auth_server.model.User;
import com.gaga.auth_server.repository.UserInfoRepository;
import com.gaga.auth_server.utils.ResponseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminService {
    private final UserInfoRepository userInfoRepository;
    private ResponseMessage responseMSG;

    @PostConstruct
    protected void init() {
        responseMSG = new ResponseMessage();
    }

    public List<SimpleUserDTO> getAllUsers() {
        List<User> user = userInfoRepository.findAll();
        List<SimpleUserDTO> userSimple = new LinkedList<>();

        for (User u : user) {
            userSimple.add(new SimpleUserDTO( u.getEmail(), u.getNickname()));
        }

        return userSimple;
    }
}
