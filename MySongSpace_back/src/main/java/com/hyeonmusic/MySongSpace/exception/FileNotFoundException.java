package com.hyeonmusic.MySongSpace.exception;

import com.hyeonmusic.MySongSpace.exception.utils.CustomException;
import com.hyeonmusic.MySongSpace.exception.utils.ErrorCode;

public class FileNotFoundException extends CustomException {
    public FileNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
