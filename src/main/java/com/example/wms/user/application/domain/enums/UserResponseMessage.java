package com.example.wms.user.application.domain.enums;

public enum UserResponseMessage {
    CHECK_STAFF_NUMBER_DUPLICATED_SUCCESS("이메일을 사용할 수 있습니다."),
    SIGN_UP_SUCCESS("회원가입이 완료되었습니다."),
    LOGIN_SUCCESS("로그인이 완료되었습니다."),
    READ_ONE_SUCCESS("회원 정보를 조회했습니다."),
    DELETE_SUCCESS("회원 탈퇴가 완료되었습니다."),
    CHECK_PASSWORD_SUCCESS("비밀번호가 일치합니다."),
    UPDATE_USER_SUCCESS("회원 정보가 수정되었습니다."),
    FIND_ID_SUCCESS("아이디를 전송했습니다."),
    FIND_PASSWORD_SUCCESS("임시 비밀번호를 전송했습니다."),
    REISSUE_TOKENS_SUCCESS("토큰이 재발급되었습니다.");

    private final String message;
    UserResponseMessage(String message) {this.message = message;}
    public String getMessage() {return message;}
}
