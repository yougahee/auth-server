package com.gaga.auth_server.dto.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class TokenDTO {
    String accessToken;
    String refreshToken;
}
