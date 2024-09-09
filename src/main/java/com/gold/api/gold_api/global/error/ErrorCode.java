package com.gold.api.gold_api.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "사용자를 찾을 수 없습니다."),
    DATE_FORMAT_ERROR(HttpStatus.BAD_REQUEST, "날짜 형식이 맞지 않습니다."),
    USER_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 존재하는 사용자 입니다"),

    // 인증&인가
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "사용자 인증에 실패했습니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    AUTHORIZATION_HEADER_MISSING(HttpStatus.UNAUTHORIZED, "Authorization 헤더값이 유효하지 않습니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰 인증 시간이 만료되었습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰 형식이 유효하지 않습니다."),
    ACCESS_TOKEN_HEADER_MISSING(HttpStatus.UNAUTHORIZED, "access 토큰 헤더값이 유효하지 않습니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "refresh 토큰을 찾을 수 없습니다.");



    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
