package com.hyeonmusic.MySongSpace.exception;

import com.hyeonmusic.MySongSpace.exception.utils.CustomException;
import com.hyeonmusic.MySongSpace.exception.utils.ErrorCode;

public class TrackNotFoundInAlbumException extends CustomException {
    public TrackNotFoundInAlbumException(ErrorCode errorCode) {
        super(errorCode);
    }
}
