package com.hyeonmusic.MySongSpace.auth.exception;

import com.hyeonmusic.MySongSpace.exception.CustomException;
import com.hyeonmusic.MySongSpace.exception.ErrorCode;

public class AuthException extends CustomException {

    public AuthException(ErrorCode errorCode) {
        super(errorCode);
    }
}
