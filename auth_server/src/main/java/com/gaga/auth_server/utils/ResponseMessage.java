package com.gaga.auth_server.utils;

public class ResponseMessage {

	private ResponseMessage() { }

	//s.f
	public static final String SUCCESS = "Success";
	public static final String FAIL = "fail";

	//test
	public static final String TEST_DELETE_EMAIL_RECORD = "[test] email 삭제 성공";

	//Input
	public static final String REQUIRED_EMAIL = "이메일은 필수 입력입니다.";
	public static final String NOT_EMAIL_FORM = "이메일 형식에 맞지 않습니다.";
	public static final String REQUIRED_PASSWORD = "비밀번호은 필수 입력입니다.";
	public static final String REQUIRED_NICKNAME = "닉네임은 필수 입력입니다.";
	public static final String NOT_NICKNAME_FORM = "닉네임 형식에 맞지 않습니다.";

	//메일 전송
	public static final String SEND_EMAIL_TITLE = "[Morse] 메일 전송";
	public static final String SEND_EMAIL = "이메일을 전송했습니다.";
	public static final String SEND_FAIL_EMAIL = "이메일을 전송에 실패했습니다.";
	public static final String SEND_EMAIL_CONTENT = "안녕하세요. morse앱에 오신 것을 환영합니다. \n" +
			"10분 안에 인증번호를 입력해주세요. \n 인증번호는 ";
	public static final String SEND_LAST_CONTENT = " 입니다.";
	public static final String SEND_TEMP_PW_CODE = "임시 PASSWORD가 전송되었습니다.";
	public static final String ALREADY_CHECKED_EMAIL = "이전에 이메일 인증한 기록이 있습니다.";

	//이메일
	public static final String NOT_FOUND_EMAIL = "존재하지 않은 이메일입니다.";
	public static final String ALREADY_USED_EMAIL = "이미 사용중인 이메일입니다.";
	public static final String VERIFY_EMAIL_FIRST = "이메일 인증을 해주세요.";
	public static final String CERTIFICATE_EMAIL = "이메일 인증되었습니다.";

	//signup
	public static final String ALREADY_OUR_MEMBER = "이전에 회원가입한 정보가 있습니다.";
	public static final String SIGN_UP_SUCCESS = "회원가입 성공";
	public static final String SIGN_UP_FAIL = "회원가입 실패";
	public static final String SIGN_UP_FIRST = "회원가입을 완료해주세요.";

	//login
	public static final String LOG_IN_SUCCESS = "로그인 성공";
	public static final String LOG_IN_FAIL = "로그인 실패";

	//token
	public static final String EXPIRED_TOKEN = "만료된 토큰입니다.";
	public static final String SIGNATURE_VERIFICATION_TOKEN = "변조된 토큰입니다.";
	public static final String JWT_DECODE_TOKEN = "토큰의 유형이 아닙니다.";
	public static final String JWT_EXCEPTION = "Jwt Exception";

	public static final String NO_TOKEN = "TOKEN이 존재하지 않습니다.";
	public static final String REISSUE_REFRESH_TOKEN = "토큰 재발급";

	//비밀번호
	public static final String NOT_PASSWORD_FORM = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.";
	public static final String PASSWORD_REGEXP = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}";

	public static final String TEMP_PW = "임시비밀번호";
	public static final String NOT_CORRECT_PW = "비밀번호가 일치하지 않습니다.";
	public static final String CHANGE_PW_SUCCESS = "비밀번호 변경을 완료했습니다.";

	//닉네임
	public static final String ALREADY_USED_NICKNAME = "이미 사용중인 닉네임입니다.";
	public static final String CAN_USE_NICKNAME = "사용가능한 닉네임입니다.";
	public static final String CHECK_NICKNAME_FIRST = "닉네임 중복체크를 해주세요.";
	public static final String CHANGE_NICKNAME_SUCCESS = "닉네임이 정상적으로 변경되었습니다.";

	//인증번호
	public static final String SEND_CERTIFICATION = "인증번호전송";
	public static final String CERTIFICATION = "인증되었습니다.";
	public static final String NOT_FOUND_CODE = "일치하는 인증번호가 없습니다.";

	//조회
	public static final String GET_ALL_USERS_SUCCESS = "회원 조회 성공";

	//마이페이지 조회
	public static final String GET_MY_PAGE = "마이페이지 조회 성공";

	//포인트
	public static final String POINT_UPDATE_SUCCESS = "포인트 업데이트 성공";
	public static final String POINT_UPDATE_FAIL = "포인트 업데이트 실패(잔액부족)";

	// 500 error
	public static final String INTERNAL_SERVER_ERROR = "서버 내부 에러입니다.";

	//권한
	public static final String NO_AUTHORIZATION = "권한이 없습니다.";

	//탈퇴
	public static final String WITHDRAW_MORSE_SUCCESS = "탈퇴처리 완료되었습니다.";

	//Redis
	public static final String REDIS_FAIL = "레디스 관련 에러입니다.";
}
