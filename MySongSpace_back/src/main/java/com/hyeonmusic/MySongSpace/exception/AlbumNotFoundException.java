package com.hyeonmusic.MySongSpace.exception;

import com.hyeonmusic.MySongSpace.exception.utils.CustomException;
import com.hyeonmusic.MySongSpace.exception.utils.ErrorCode;

public class AlbumNotFoundException extends CustomException {
    public AlbumNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
