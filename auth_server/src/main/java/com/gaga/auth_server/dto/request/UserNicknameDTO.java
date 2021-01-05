package com.gaga.auth_server.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UserNicknameDTO {
    @NotBlank(message = "닉네임은 필수 입력입니다.")
    private String nickname;
}
