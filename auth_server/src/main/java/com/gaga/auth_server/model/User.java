package com.gaga.auth_server.model;

import lombok.*;
import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "user")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_idx")
    private Long userIdx;

    @Column(name = "email")
    private String email;

    @Column(length = 100, name = "pwd")
    private String password;

    @Column(length = 100, name = "salt")
    private String salt;

    @Column(length = 10, name = "nickname")
    private String nickname;

    @Column(name = "grade", columnDefinition = "TINYINT(4) default 0")
    private Byte grade;

    @Column(name = "point", columnDefinition = "BIGINT(20) default '50000'")
    private Long point;

    @Column(name = "login_dt")
    private Date loginDate;
}
