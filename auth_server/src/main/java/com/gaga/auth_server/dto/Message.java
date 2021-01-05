package com.gaga.auth_server.dto;

import lombok.Getter;

@Getter
public class Message {
    private String message = "";
    private Object data;

    private static final String DEFAULT_KEY = "result";

    public Message(String message) {
        this.data = "";
        this.message = message;
    }

    public Message(Object result) {
        this.data = result;
        this.message = "성공";
    }

    public Message(Object result, String message) {
        this.data = result;
        this.message = message;
    }
}
