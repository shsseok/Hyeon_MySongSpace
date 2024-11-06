package com.hyeonmusic.MySongSpace.entity;

public enum Role {
    USER("ROLE_USER"),       // 일반 사용자
    ADMIN("ROLE_ADMIN"),     // 관리자
    MODERATOR("ROLE_MODERATOR"); // 중재자

    private final String key;

    Role(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}

