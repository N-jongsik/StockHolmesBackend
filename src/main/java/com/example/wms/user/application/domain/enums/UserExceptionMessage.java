package com.example.wms.user.application.domain.enums;

public enum UserExceptionMessage {

    SIGN_UP_NOT_VALID("회원가입에 필요한 정보가 부족합니다."),
    USER_NOT_FOUND("존재하지 않는 회원입니다."),
    LOGIN_PASSWORD_ERROR("비밀번호가 일치하지 않습니다."),
    DUPLICATED_STAFF_NUMBER("이미 존재하는 사번입니다."),
    REFRESH_TOKEN_EXPIRED("리프레시 토큰이 만료되었습니다. 로그인을 다시 해주세요."),
    TOKEN_MISMATCH("토큰이 일치하지 않습니다."),
    USER_TYPE_MISMATCH("유저 타입이 일치하지 않습니다."),
    REFRESH_TOKEN_NOT_FOUND("Refresh token을 찾을 수 없습니다."),
    ACCESS_TOKEN_NOT_FOUND("Access token을 찾을 수 없습니다.");

    private final String message;
    UserExceptionMessage(String message) {this.message = message;}
    public String getMessage() {return message;}
}
