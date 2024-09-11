package com.gold.api.gold_api.global.error;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

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
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "refresh 토큰을 찾을 수 없습니다."),
    TOKEN_TYPE_ERROR(HttpStatus.UNAUTHORIZED, "토큰 type이 유효하지 않습니다."),
    SERVER_ERROR_GRPC(HttpStatus.INTERNAL_SERVER_ERROR,"인증 서버 요청 오류가 발생했습니다."),

    // 사용자 비밀번호 관련
    PASSWORD_VIOLATES_POLICY(BAD_REQUEST, "비밀번호 정책에 맞지 않습니다."),
    PASSWORD_TOO_SHORT(BAD_REQUEST, "비밀번호는 최소 10자 이상이어야 합니다."),
    PASSWORD_TOO_COMMON(BAD_REQUEST, "통상적으로 자주 사용되는 비밀번호는 사용할 수 없습니다."),
    PASSWORD_LACKS_VARIETY(BAD_REQUEST, "숫자, 문자, 특수문자 중 2가지 이상을 포함해야 합니다."),
    PASSWORD_SIMILAR_TO_PERSONAL_INFO(BAD_REQUEST, "비밀번호는 다른 개인 정보와 유사할 수 없습니다."),
    PASSWORD_SAME_AS_PREVIOUS(BAD_REQUEST, "이전 비밀번호와 동일하게 설정할 수 없습니다."),
    PASSWORD_HAS_SEQUENTIAL_CHARS(BAD_REQUEST, "3회 이상 연속되는 문자는 사용할 수 없습니다."),

    // 상품 관련
    PRODUCT_NOT_FOUND(BAD_REQUEST, "존재하지 않는 상품 입니다");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
