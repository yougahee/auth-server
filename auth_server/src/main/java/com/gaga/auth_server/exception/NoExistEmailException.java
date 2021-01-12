package com.gaga.auth_server.exception;


public class NoExistEmailException extends RuntimeException {
    static final long serialVersionUID = 1L;

    public NoExistEmailException() { super(); }

    public NoExistEmailException(Throwable e) { super(e); }

    public NoExistEmailException(String errorMessage) {
        super(errorMessage);
    }

    public NoExistEmailException(String message, Throwable e) { super(message, e); }

}