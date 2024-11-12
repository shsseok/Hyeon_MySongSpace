package com.hyeonmusic.MySongSpace.auth.exception;


import com.hyeonmusic.MySongSpace.exception.utils.CustomException;
import com.hyeonmusic.MySongSpace.exception.utils.ErrorCode;

public class TokenException extends CustomException {

    public TokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
