package com.gaga.auth_server.dto.request;

import com.gaga.auth_server.utils.ResponseMessage;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class UserNicknameDTO {
    @NotBlank(message = ResponseMessage.REQUIRED_NICKNAME)
    private String nickname;
}
