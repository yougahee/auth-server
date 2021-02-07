package com.gaga.auth_server.dto.message;

import com.gaga.auth_server.utils.TimestampUtils;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Message {
    String timestamp;
    private String message;
    private Object data;

    private static final String DEFAULT_KEY = "result";

    public Message() {
        this.timestamp = TimestampUtils.getNow();
    }

    public Message(String message) {
        this.timestamp = TimestampUtils.getNow();
        this.message = message;
    }

    public Message(Object result) {
        this.timestamp = TimestampUtils.getNow();
        this.message = "success";
        this.data = result;
    }

    public Message(Object result, String message) {
        this.timestamp = TimestampUtils.getNow();
        this.message = message;
        this.data = result;
    }
}
