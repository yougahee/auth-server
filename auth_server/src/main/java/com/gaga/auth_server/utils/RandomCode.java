package com.gaga.auth_server.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomCode {
    public String randomString() {
        StringBuilder sb = new StringBuilder();
        Random rnd = new Random();

        for (int i = 0; i < 8; i++) {
            switch (i % 3) {
                case 0:
                    sb.append((char) ((rnd.nextInt(26)) + 97));
                    break;
                case 1:
                    sb.append((char) ((rnd.nextInt(26)) + 65));
                    break;
                case 2:
                    sb.append((rnd.nextInt(10)));
                    break;
            }
        }
        return sb.toString();
    }
}
