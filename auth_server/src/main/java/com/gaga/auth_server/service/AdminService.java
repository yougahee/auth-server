package com.gaga.auth_server.service;

import com.gaga.auth_server.dto.UserDTO;
import com.gaga.auth_server.dto.response.SimpleUserDTO;
import com.gaga.auth_server.model.User;
import com.gaga.auth_server.repository.UserInfoRepository;
import com.gaga.auth_server.utils.ResponseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<UserDTO> getAllUsers(Pageable pageable) {
        Page<User> user = userInfoRepository.findAll(pageable);
        return user.stream().map(UserDTO::fromEntity).collect(Collectors.toList());
    }
}
