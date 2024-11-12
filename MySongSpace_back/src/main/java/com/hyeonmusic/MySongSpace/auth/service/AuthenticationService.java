package com.hyeonmusic.MySongSpace.auth.service;

import com.hyeonmusic.MySongSpace.auth.exception.AuthException;
import com.hyeonmusic.MySongSpace.entity.Member;
import com.hyeonmusic.MySongSpace.repository.MemberRepository;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.hyeonmusic.MySongSpace.exception.utils.ErrorCode.MEMBER_NOT_FOUND;
import static com.hyeonmusic.MySongSpace.exception.utils.ErrorCode.NO_ACCESS;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private final MemberRepository memberRepository;

    public Member getMemberOrThrow(String memberKey) {
        return memberRepository.findByMemberKey(memberKey)
                .orElseThrow(() -> new AuthException(MEMBER_NOT_FOUND));
    }

    public void checkAccess(String memberKey, Member member) {
        if (!member.getMemberKey().equals(memberKey)) {
            throw new AuthException(NO_ACCESS);
        }
    }
}
