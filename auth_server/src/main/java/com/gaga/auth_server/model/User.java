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
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_idx")
    private Long userIdx;

    @Column(name = "email")
    private String email;

    @Column(length = 100, name = "password")
    private String password;

    @Column(length = 100, name = "salt")
    private String salt;

    @Column(length = 10, name = "nickname")
    private String nickname;

    @Column(name = "grade", columnDefinition = "int default 0")
    private int grade;

    @Column(name = "point", columnDefinition = "int default 0")
    private int point;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "login_at")
    private Date loginAt;

    @Column(name = "update_at")
    private Date updateAt;
}
