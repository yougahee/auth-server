package com.gaga.auth_server.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginTokenResponseDTO {
    private int status;
    private boolean success;
    private String message;
    private String accessToken;
    private String refreshToken;

    public LoginTokenResponseDTO() {
        this.status = 400;
        this.success = false;
        this.message = "로그인 실패";
    }

    public LoginTokenResponseDTO(String message) {
        this.status = 400;
        this.success = false;
        this.message = message;
    }

    public LoginTokenResponseDTO(int status, boolean success, String message) {
        this.status = status;
        this.success = success;
        this.message = message;
    }

    public LoginTokenResponseDTO(String token, String refreshToken) {
        this.status = 200;
        this.success = true;
        this.message = "로그인 성공";
        this.accessToken = token;
        this.refreshToken = refreshToken;
    }
}
