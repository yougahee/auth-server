package com.gaga.auth_server.utils;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ResponseMessage {
    //이메일 전송
    public String SEND_EMAIL = "이메일을 전송했습니다.";
    public String SEND_FAIL_EMAIL = "이메일을 전송에 실패했습니다.";
    public String SEND_EMAIL_CONTENT = "안녕하세요. morse앱에 오신 것을 환영합니다. \n" +
            "10분 안에 인증번호를 입력해주세요. \n 인증번호는 ";
    public String SEND_LAST_CONTENT = " 입니다.";

    //이메일
    public String NOT_FOUND_EMAIL = "존재하지 않은 이메일입니다.";
    public String ALREADY_USED_EMAIL = "이미 사용중인 이메일입니다.";

    //signup
    public String SIGN_UP_SUCCESS = "회원가입 성공";
    public String SIGN_UP_FAIL = "회원가입 실패";

    //login
    public String LOG_IN_SUCCESS = "로그인 성공";
    public String LOG_IN_FAIL = "로그인 실패";

    //비밀번호
    public String TEMP_PW = "임시비밀번호";
    public String NOT_CORRECT_PW = "비밀번호가 일치하지 않습니다.";

    //닉네임
    public String ALREADY_USED_NICKNAME = "이미 사용중인 닉네임입니다.";
    public String CAN_USE_NICKNAME = "사용가능한 닉네임입니다.";

    //인증번호
    public String SEND_CERTIFICATION = "인증번호전송";
    public String CERTIFICATION = "인증되었습니다.";
    public String NOT_FOUND_CODE = "일치하는 인증번호가 없습니다.";

    //조회
    public String GET_ALL_USERS = "회원 조회 성공";
}
