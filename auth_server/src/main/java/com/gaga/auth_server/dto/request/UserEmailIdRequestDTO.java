package com.gaga.auth_server.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;

@NoArgsConstructor
@Getter
@Setter
public class UserEmailIdRequestDTO {
    @Email(message = "이메일 형식에 맞지 않습니다.")
    private String email;
}
