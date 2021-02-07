package com.gaga.auth_server.dto.request;

import com.gaga.auth_server.utils.ResponseMessage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@Getter @Setter
public class UserInfoRequestDTO {

    @NotBlank(message = ResponseMessage.REQUIRED_EMAIL)
    @Email(message = ResponseMessage.NOT_EMAIL_FORM)
    private String email;

    @NotBlank(message = ResponseMessage.REQUIRED_PASSWORD)
    @Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}",
            message = ResponseMessage.NOT_PASSWORD_FORM)
    private String password;

    @NotBlank(message = ResponseMessage.REQUIRED_NICKNAME)
    private String nickname;
}
