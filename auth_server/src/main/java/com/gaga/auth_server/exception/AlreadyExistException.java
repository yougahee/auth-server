package com.gaga.auth_server.exception;

public class AlreadyExistException extends RuntimeException {
    static final long serialVersionUID = 1L;

    public AlreadyExistException() { super(); }

    public AlreadyExistException(Throwable e) { super(e); }

    public AlreadyExistException(String errorMessage) {
        super(errorMessage);
    }

    public AlreadyExistException(String message, Throwable e) { super(message, e); }
}
