package com.gaga.auth_server.exception;

import lombok.Getter;

@Getter
public class UnauthorizedException extends RuntimeException {
    static final long serialVersionUID = 1000L;
    public final String errorMessage = "유효하지 않습니다.";

    public UnauthorizedException() {};

    public UnauthorizedException(String message, Throwable e) { super(message, e); }

    public UnauthorizedException(String errorMessage) {
        super(errorMessage);
    }
}
