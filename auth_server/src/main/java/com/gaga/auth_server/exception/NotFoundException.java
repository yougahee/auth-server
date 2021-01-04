package com.gaga.auth_server.exception;

public class NotFoundException extends RuntimeException {

    static final long serialVersionUID = 1L;

    public NotFoundException() { super(); }

    public NotFoundException(Throwable e) { super(e); }

    public NotFoundException(String errorMessage) {
        super(errorMessage);
    }

    public NotFoundException(String message, Throwable e) { super(message, e); }

}
