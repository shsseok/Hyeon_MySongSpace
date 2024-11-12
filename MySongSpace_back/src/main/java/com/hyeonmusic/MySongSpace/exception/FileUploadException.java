package com.hyeonmusic.MySongSpace.exception;

import com.hyeonmusic.MySongSpace.exception.utils.CustomException;
import com.hyeonmusic.MySongSpace.exception.utils.ErrorCode;

public class FileUploadException extends CustomException {
    public FileUploadException(ErrorCode errorCode) {
        super(errorCode);
    }
}
