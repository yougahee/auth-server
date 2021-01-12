package com.gaga.auth_server.utils;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserGrade {
    public byte BASIC_GRADE = 0;
    public byte SIGNUP_COMPLETE = 1;
    public byte EMAIL_CHECK_COMPLETE = 3;

    public byte MANAGER = 5;
    public byte LEAVE_SERVICE = 9;
}
