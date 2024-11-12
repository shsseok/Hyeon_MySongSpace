package com.hyeonmusic.MySongSpace.exception;

import com.hyeonmusic.MySongSpace.exception.utils.CustomException;
import com.hyeonmusic.MySongSpace.exception.utils.ErrorCode;

public class CommentNotFoundException extends CustomException {
    public CommentNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
