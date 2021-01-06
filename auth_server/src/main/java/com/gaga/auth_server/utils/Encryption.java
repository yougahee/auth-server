package com.gaga.auth_server.utils;

import lombok.Getter;
import org.springframework.security.crypto.bcrypt.BCrypt;


@Getter
public class Encryption {
    private String salt;

    public String encode(String rawPassword) {
        salt = BCrypt.gensalt();
        return BCrypt.hashpw(rawPassword, salt);
    }

    public String encodeWithSalt(String rawPassword, String salt) {
        return BCrypt.hashpw(rawPassword, salt);
    }

    public static void main(String[] args) {
        /*String pw = "gagaga";
        System.out.println("암호화된 PW : " + encode(pw));
        System.out.println("salt의 값 : " + salt);

        if(BCrypt.checkpw(pw, encode(pw)))
            System.out.println("It matches");
        else
            System.out.println("It does not match");

        System.out.println("============");
        System.out.println("암호화된 PW : " + encode("test!"));
        System.out.println("salt의 값 : " + salt);*/
    }
}
