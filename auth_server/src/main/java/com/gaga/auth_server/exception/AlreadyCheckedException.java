package com.gaga.auth_server.exception;

public class AlreadyCheckedException extends RuntimeException {
    static final long serialVersionUID = 1L;

    public AlreadyCheckedException() { super(); }

    public AlreadyCheckedException(Throwable e) { super(e); }

    public AlreadyCheckedException(String errorMessage) { super(errorMessage); }

    public AlreadyCheckedException(String message, Throwable e) { super(message, e); }
}
