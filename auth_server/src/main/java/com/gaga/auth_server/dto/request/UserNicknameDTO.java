package com.gaga.auth_server.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UserNicknameDTO {
    @NotBlank(message = "아이디(이메일)은 필수 입력입니다.")
    @Email(message = "이메일 형식에 맞지 않습니다.")
    private String email;

    @NotBlank(message = "닉네임은 필수 입력입니다.")
    private String nickname;
}
