package com.gaga.auth_server.utils.mail;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class MailDTO {
    private String address;
    private String title;
    private String message;

    public MailDTO(String title, String address, String message) {
        this.address = address;
        this.title = "[Morse] " + title;
        this.message = message;
    }
}
