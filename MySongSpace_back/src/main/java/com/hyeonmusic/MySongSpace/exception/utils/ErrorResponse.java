package com.hyeonmusic.MySongSpace.exception.utils;

import com.hyeonmusic.MySongSpace.exception.utils.ErrorCode;

public record ErrorResponse(
        ErrorCode errorCode,
        String message
) {

}
