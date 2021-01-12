package com.gaga.auth_server.utils;

import lombok.Getter;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;


@Getter
@Component
public class Encryption {
    private String salt;

    public String encode(String rawPassword) {
        salt = BCrypt.gensalt();
        return BCrypt.hashpw(rawPassword, salt);
    }

    public String encodeWithSalt(String rawPassword, String salt) {
        return BCrypt.hashpw(rawPassword, salt);
    }
}
