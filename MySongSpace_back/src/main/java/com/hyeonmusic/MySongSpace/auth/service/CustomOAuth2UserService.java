package com.hyeonmusic.MySongSpace.auth.service;

import com.hyeonmusic.MySongSpace.auth.dto.OAuth2UserInfo;
import com.hyeonmusic.MySongSpace.auth.dto.PrincipalDetails;
import com.hyeonmusic.MySongSpace.entity.Member;
import com.hyeonmusic.MySongSpace.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 1. 유저 정보(attributes) 가져오기
        Map<String, Object> oAuth2UserAttributes = super.loadUser(userRequest).getAttributes();

        // 2. resistrationId 가져오기 (third-party id)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        log.info("인증 성공 함");
        // 3. userNameAttributeName 가져오기
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        // 4. 유저 정보 dto 생성
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfo.of(registrationId, oAuth2UserAttributes);

        // 5. 회원가입 및 로그인
        Member member = getOrSave(oAuth2UserInfo);

        // 6. OAuth2User로 반환
        return new PrincipalDetails(member, oAuth2UserAttributes, userNameAttributeName);
    }

    private Member getOrSave(OAuth2UserInfo oAuth2UserInfo) {
            Member member = memberRepository.findByEmail(oAuth2UserInfo.email())
                    .orElseGet(oAuth2UserInfo::toEntity);
        return memberRepository.save(member);
    }
}

