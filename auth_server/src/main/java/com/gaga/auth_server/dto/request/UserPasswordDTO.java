package com.gaga.auth_server.dto.request;

import com.gaga.auth_server.utils.ResponseMessage;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@Getter
public class UserPasswordDTO {
    private String old_password;

    @NotBlank(message = ResponseMessage.REQUIRED_PASSWORD)
    @Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}",
            message = ResponseMessage.NOT_PASSWORD_FORM)
    private String password;
}
