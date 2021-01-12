package com.gaga.auth_server.repository;

import com.gaga.auth_server.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<User, Long> {

     Optional<User> findByEmail(String email);

     Page<User> findAll(Pageable pageable);

     Boolean existsByEmail(String email);

     Boolean existsByNickname(String nickname);

}
