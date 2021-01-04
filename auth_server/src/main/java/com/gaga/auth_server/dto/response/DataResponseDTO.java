package com.gaga.auth_server.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DataResponseDTO {
    private int status;
    private boolean success;
    private String message;
    private Object data;

    public DataResponseDTO(String message) {
        this.status = 400;
        this.success = false;
        this.message = message;
    }

    public DataResponseDTO(int status, String message) {
        this.status = status;
        this.success = false;
        this.message = message;
    }

    public DataResponseDTO(String message, Object data) {
        this.status = 200;
        this.success = true;
        this.message = message;
        this.data = data;
    }
}
