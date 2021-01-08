package com.gaga.auth_server.utils;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ResponseMessage {
    //test
    public String TEST_DELETE_EMAIL_RECORD = "[test] email 삭제 성공";

    //메일 전송
    public String SEND_EMAIL = "이메일을 전송했습니다.";
    public String SEND_FAIL_EMAIL = "이메일을 전송에 실패했습니다.";
    public String SEND_EMAIL_CONTENT = "안녕하세요. morse앱에 오신 것을 환영합니다. \n" +
            "10분 안에 인증번호를 입력해주세요. \n 인증번호는 ";
    public String SEND_LAST_CONTENT = " 입니다.";
    public String SEND_TEMP_PW_CODE = "임시 PASSWORD가 전송되었습니다.";

    //이메일
    public String NOT_FOUND_EMAIL = "존재하지 않은 이메일입니다.";
    public String ALREADY_USED_EMAIL = "이미 사용중인 이메일입니다.";
    public String VERIFY_EMAIL_FIRST = "이메일 인증을 해주세요.";
    public String CERTIFICATE_EMAIL = "이메일 인증되었습니다.";

    //signup
    public String ALREADY_OUR_MEMBER = "이전에 회원가입한 정보가 있습니다.";
    public String SIGN_UP_SUCCESS = "회원가입 성공";
    public String SIGN_UP_FAIL = "회원가입 실패";

    //login
    public String LOG_IN_SUCCESS = "로그인 성공";
    public String LOG_IN_FAIL = "로그인 실패";

    //token
    public String EXPIRED_TOKEN = "만료된 토큰입니다.";
    public String REISSUE_REFRESH_TOKEN = "토큰 재발급";

    //비밀번호
    public String TEMP_PW = "임시비밀번호";
    public String NOT_CORRECT_PW = "비밀번호가 일치하지 않습니다.";
    public String CHANGE_PW_SUCCESS = "비밀번호 변경을 완료했습니다.";

    //닉네임
    public String ALREADY_USED_NICKNAME = "이미 사용중인 닉네임입니다.";
    public String CAN_USE_NICKNAME = "사용가능한 닉네임입니다.";
    public String CHECK_NICKNAME_FIRST = "닉네임 중복체크를 해주세요.";

    //인증번호
    public String SEND_CERTIFICATION = "인증번호전송";
    public String CERTIFICATION = "인증되었습니다.";
    public String NOT_FOUND_CODE = "일치하는 인증번호가 없습니다.";

    //조회
    public String GET_ALL_USERS_SUCCESS = "회원 조회 성공";

    //마이페이지 조회
    public String GET_MY_PAGE = "마이페이지 조회 성공";

    //포인트
    public String POINT_UPDATE_SUCCESS = "포인트 업데이트 성공";
    public String POINT_UPDATE_FAIL = "포인트 업데이트 실패(잔액부족)";

    public String INTERNAL_SERVER_ERROR = "서버 내부 에러입니다.";
}
