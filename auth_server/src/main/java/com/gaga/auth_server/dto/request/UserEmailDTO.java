package com.gaga.auth_server.dto.request;

import com.gaga.auth_server.utils.ResponseMessage;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
public class UserEmailDTO {
    @NotBlank(message = ResponseMessage.REQUIRED_EMAIL)
    @Email(message = ResponseMessage.NOT_EMAIL_FORM)
    private String email;
}
