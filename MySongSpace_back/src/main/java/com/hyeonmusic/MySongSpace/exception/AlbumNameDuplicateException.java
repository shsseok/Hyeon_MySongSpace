package com.hyeonmusic.MySongSpace.exception;

import com.hyeonmusic.MySongSpace.exception.utils.CustomException;
import com.hyeonmusic.MySongSpace.exception.utils.ErrorCode;

public class AlbumNameDuplicateException extends CustomException {
    public AlbumNameDuplicateException(ErrorCode errorCode) {
        super(errorCode);
    }
}
