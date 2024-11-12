package com.hyeonmusic.MySongSpace.exception;

import com.hyeonmusic.MySongSpace.exception.utils.CustomException;
import com.hyeonmusic.MySongSpace.exception.utils.ErrorCode;

public class FileDeleteException extends CustomException {
    public FileDeleteException(ErrorCode errorCode) {
        super(errorCode);
    }
}
