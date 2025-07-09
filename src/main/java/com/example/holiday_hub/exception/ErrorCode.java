package com.example.holiday_hub.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    COUNTRY_CODE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 국가코드 입니다."),
    UNSUPPORTED_HOLIDAY_TYPE(HttpStatus.BAD_REQUEST, "존재하지 않는 공휴일 타입 입니다."),
    HOLIDAY_FETCH_FAILED(HttpStatus.SERVICE_UNAVAILABLE, "외부 공휴일 정보를 불러오지 못했습니다."),
    UNSUPPORTED_YEAR_RANGE(HttpStatus.BAD_REQUEST, "지원하지 않는 연도 범위입니다."),
    INVALID_DATE_RANGE(HttpStatus.BAD_REQUEST, "잘못된 날짜 범위입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부에서 오류 발생했습니다.");

    private HttpStatus status;
    private String message;
}
