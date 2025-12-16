package com.hyeonmusic.MySongSpace.exception;

import com.hyeonmusic.MySongSpace.exception.utils.CustomException;
import com.hyeonmusic.MySongSpace.exception.utils.ErrorCode;

public class UnsupportedFileTypeException extends CustomException {
    public UnsupportedFileTypeException(ErrorCode errorCode) {
        super(errorCode);
    }
}
