package com.gaga.auth_server.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class SimpleUserDTO {
    private String email;
    private String name;

    public SimpleUserDTO(String email, String name) {
        this.email = email;
        this.name = name;
    }
}
