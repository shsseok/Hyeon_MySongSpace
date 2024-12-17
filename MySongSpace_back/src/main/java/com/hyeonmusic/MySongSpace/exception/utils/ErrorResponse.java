package com.hyeonmusic.MySongSpace.exception.utils;


public record ErrorResponse(
        ErrorCode errorCode,
        String message
) {

}
