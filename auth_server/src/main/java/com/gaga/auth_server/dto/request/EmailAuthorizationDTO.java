package com.gaga.auth_server.dto.request;

import com.gaga.auth_server.utils.ResponseMessage;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class EmailAuthorizationDTO {
    @NotBlank(message = ResponseMessage.REQUIRED_EMAIL)
    @Email(message = ResponseMessage.NOT_EMAIL_FORM)
    private String email;
    private String code;
}
