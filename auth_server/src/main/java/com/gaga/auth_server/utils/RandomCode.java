package com.gaga.auth_server.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomCode {
    Random rnd = new Random();

    public final int RANDOM_MIN_NUMBER = 10000;
    public final int RANDOM_MAX_NUMBER = 90000;

    public String randomString() {
        StringBuilder sb = new StringBuilder();

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
                default:
                    break;
            }
        }
        return sb.toString();
    }

    public int randomEmailCode() {
        return rnd.nextInt(RANDOM_MAX_NUMBER) + RANDOM_MIN_NUMBER;
    }
}
