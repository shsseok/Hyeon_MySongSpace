package com.hyeonmusic.MySongSpace.exception;

import com.hyeonmusic.MySongSpace.exception.utils.CustomException;
import com.hyeonmusic.MySongSpace.exception.utils.ErrorCode;

public class TrackNotFoundException extends CustomException {
    public TrackNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
