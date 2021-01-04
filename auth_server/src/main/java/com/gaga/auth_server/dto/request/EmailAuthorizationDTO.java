package com.gaga.auth_server.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;

@Getter
@Setter
public class EmailAuthorizationDTO {
    @Email(message = "이메일 형식에 맞지 않습니다.")
    private String email;
    private String code;
}
