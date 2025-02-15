package com.hyeonmusic.MySongSpace.exception;

import com.hyeonmusic.MySongSpace.exception.utils.CustomException;
import com.hyeonmusic.MySongSpace.exception.utils.ErrorCode;

public class S3UploadException extends CustomException {
    public S3UploadException(ErrorCode errorCode) {
        super(errorCode);
    }
}
