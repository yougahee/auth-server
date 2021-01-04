package com.gaga.auth_server.repository;

import com.gaga.auth_server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<User, Long> {

     Optional<User> findByEmail(String email);

     List<User> findAll();

     Boolean existsByEmail(String email);
     Boolean existsByNickname(String nickname);
}
