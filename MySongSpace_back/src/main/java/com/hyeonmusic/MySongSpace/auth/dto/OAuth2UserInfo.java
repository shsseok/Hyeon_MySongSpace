package com.hyeonmusic.MySongSpace.auth.dto;

import com.hyeonmusic.MySongSpace.auth.exception.AuthException;
import com.hyeonmusic.MySongSpace.common.utils.KeyGenerator;
import com.hyeonmusic.MySongSpace.entity.Member;
import com.hyeonmusic.MySongSpace.entity.Role;
import lombok.Builder;


import java.util.Map;

import static com.hyeonmusic.MySongSpace.exception.utils.ErrorCode.ILLEGAL_REGISTRATION_ID;


@Builder
public record OAuth2UserInfo(
        String name,
        String email,
        String profile
) {

    public static OAuth2UserInfo of(String registrationId, Map<String, Object> attributes) {
        return switch (registrationId) { // registration id별로 userInfo 생성
            case "kakao" -> ofKakao(attributes);
            default -> throw new AuthException(ILLEGAL_REGISTRATION_ID);
        };
    }

    private static OAuth2UserInfo ofKakao(Map<String, Object> attributes) {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        return OAuth2UserInfo.builder()
                .name((String) profile.get("nickname"))
                .email((String) account.get("email"))
                .profile((String) profile.get("profile_image_url"))
                .build();
    }

    public Member toEntity() {
        return new Member(name, email, profile, KeyGenerator.generateKey(), Role.USER);
    }
}
