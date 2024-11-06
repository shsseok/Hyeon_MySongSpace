package com.hyeonmusic.MySongSpace.exception;

public record ErrorResponse(
        ErrorCode errorCode,
        String message
) {

}
