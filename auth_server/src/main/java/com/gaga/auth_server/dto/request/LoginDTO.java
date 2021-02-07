package com.gaga.auth_server.dto.request;

import com.gaga.auth_server.utils.ResponseMessage;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class LoginDTO {
	@NotBlank(message = ResponseMessage.REQUIRED_EMAIL)
	@Email(message = ResponseMessage.NOT_EMAIL_FORM)
	private String email;

	@NotBlank(message = ResponseMessage.REQUIRED_PASSWORD)
	@Pattern(regexp = ResponseMessage.PASSWORD_REGEXP,
			message = ResponseMessage.NOT_PASSWORD_FORM)
	private String password;
}
