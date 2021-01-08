package com.gaga.auth_server.dto.response;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MyPageDTO {
    String nickname;
    Long point;
    Set<String> followNickname = new HashSet<>();
}
