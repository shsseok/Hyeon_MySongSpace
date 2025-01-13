package com.hyeonmusic.MySongSpace.exception;

import com.hyeonmusic.MySongSpace.exception.utils.CustomException;
import com.hyeonmusic.MySongSpace.exception.utils.ErrorCode;

public class SelfTrackLikeNotAllowedException extends CustomException {
    public SelfTrackLikeNotAllowedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
