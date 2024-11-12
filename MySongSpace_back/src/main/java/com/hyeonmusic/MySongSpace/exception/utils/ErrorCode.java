package com.hyeonmusic.MySongSpace.exception.utils;

import static org.springframework.http.HttpStatus.*;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // auth
    ILLEGAL_REGISTRATION_ID(NOT_ACCEPTABLE, "illegal registration id"),
    TOKEN_EXPIRED(UNAUTHORIZED, "토큰이 만료되었습니다."),
    INVALID_TOKEN(UNAUTHORIZED, "올바르지 않은 토큰입니다."),
    INVALID_JWT_SIGNATURE(UNAUTHORIZED, "잘못된 JWT 시그니처입니다."),
    // file
    FILE_DELETE_FAILED(INTERNAL_SERVER_ERROR, "파일 삭제 도중 문제가 생겼습니다"),
    FILE_UPLOAD_FAILED(INTERNAL_SERVER_ERROR, "업로드 중 문제가 발생했습니다."),
    FILE_NOT_FOUND(NOT_FOUND, "파일을 찾을 수 없습니다."),
    // member
    MEMBER_NOT_FOUND(NOT_FOUND, "회원을 찾을 수 없습니다."),
    // track
    TRACK_NOT_FOUND(NOT_FOUND, "트랙을 찾을 수 없습니다."),
    TRACK_NOT_FOUND_IN_ALBUM(NOT_FOUND, "앨범에 해당 트랙이 존재하지 않습니다."),
    // album
    ALBUM_NAME_DUPLICATE(CONFLICT, "이미 존재하는 앨범 이름입니다."),
    ALBUM_NOT_FOUND(NOT_FOUND, "앨범을 찾을 수 없습니다."),
    //comment
    PARENT_COMMENT_NOT_FOUND(NOT_FOUND, "부모 댓글을 찾을 수 없습니다."),
    COMMENT_NOT_FOUND(NOT_FOUND, "댓글을 찾을 수 없습니다."),
    // global
    RESOURCE_LOCKED(LOCKED, "자원이 잠겨있어 접근할 수 없습니다."),
    NO_ACCESS(FORBIDDEN, "접근 권한이 없습니다."),
    RESOURCE_NOT_FOUND(NOT_FOUND, "요청한 자원(파일)을 찾을 수 없습니다."),
    INVALID_REQUEST(BAD_REQUEST, "올바르지 않은 요청입니다."),
    INTERNAL_ERROR(INTERNAL_SERVER_ERROR, "예상치못한 에러가 발생했습니다.");


    private final HttpStatus httpStatus;
    private final String message;
}
