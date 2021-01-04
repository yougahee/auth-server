package com.gaga.auth_server.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DefaultResponseDTO {
    private int status;
    private boolean success;
    private String message;

    public DefaultResponseDTO() {
        this.status = 400;
        this.success = false;
        this.message = "실패";
    }

    public DefaultResponseDTO(String message) {
        this.status = 200;
        this.success = true;
        this.message = message;
    }

    public DefaultResponseDTO(int status, Boolean success, String message) {
        this.status = status;
        this.success = success;
        this.message = message;
    }

}
