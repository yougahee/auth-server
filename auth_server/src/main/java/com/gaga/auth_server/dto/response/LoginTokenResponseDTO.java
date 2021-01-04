package com.gaga.auth_server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class LoginTokenResponseDTO {
    private String accessToken;
    private String refreshToken;
}
