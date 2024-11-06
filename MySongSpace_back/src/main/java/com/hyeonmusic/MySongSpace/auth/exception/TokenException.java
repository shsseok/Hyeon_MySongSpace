package com.hyeonmusic.MySongSpace.auth.exception;


import com.hyeonmusic.MySongSpace.exception.CustomException;
import com.hyeonmusic.MySongSpace.exception.ErrorCode;

public class TokenException extends CustomException {

    public TokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
