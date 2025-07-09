package com.example.holiday_hub.exception;

public record ErrorResponse(
        int status,
        String message,
        String code
) {
    public static ErrorResponse from(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.getStatus().value(), errorCode.getMessage(), errorCode.name());
    }
}
