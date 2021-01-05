package com.gaga.auth_server.exception;

public class ExistNickNameException extends RuntimeException {
    static final long serialVersionUID = 1L;

    public ExistNickNameException() { super(); }

    public ExistNickNameException(Throwable e) { super(e); }

    public ExistNickNameException(String errorMessage) {
        super(errorMessage);
    }

    public ExistNickNameException(String message, Throwable e) { super(message, e); }
}
