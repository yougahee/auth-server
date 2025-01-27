package com.gaga.auth_server.dto;

import com.gaga.auth_server.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@Getter @Setter
public class UserDTO {
    private long userIdx;
    private String email;
    private String nickname;
    private Byte grade;
    private Long point;
    private Date createDate;
    private Date loginDate;
    private Date updateDate;

    public static UserDTO fromEntity(User entity) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserIdx(entity.getUserIdx());
        userDTO.setEmail(entity.getEmail());
        userDTO.setNickname(entity.getNickname());
        userDTO.setGrade(entity.getGrade());
        userDTO.setPoint(entity.getPoint());
        userDTO.setLoginDate(entity.getLoginDate());

        return userDTO;
    }
}
