package com.gaga.auth_server.exception;

public class UnauthorizedException extends RuntimeException {
    static final long serialVersionUID = 1000L;

    public UnauthorizedException() { super(); }

    public UnauthorizedException(String message) { super(message); }

    public UnauthorizedException(Throwable e) { super(e); }

    public UnauthorizedException(String message, Throwable e) { super(message, e); }
}
