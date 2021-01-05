package com.gaga.auth_server.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UserNicknameDTO {
    @NotBlank
    private String nickname;
}
