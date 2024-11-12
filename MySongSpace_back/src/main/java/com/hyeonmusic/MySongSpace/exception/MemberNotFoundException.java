package com.hyeonmusic.MySongSpace.exception;

import com.hyeonmusic.MySongSpace.exception.utils.CustomException;
import com.hyeonmusic.MySongSpace.exception.utils.ErrorCode;

public class MemberNotFoundException extends CustomException {
    public MemberNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
