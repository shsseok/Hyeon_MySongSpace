package com.hyeonmusic.MySongSpace.exception.utils;


public class ErrorResponse {
    private final ErrorCode errorCode;
    private final String message;

    public ErrorResponse(ErrorCode errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }
}
