package com.hyeonmusic.MySongSpace.service;

import com.hyeonmusic.MySongSpace.entity.Likes;
import com.hyeonmusic.MySongSpace.entity.Member;
import com.hyeonmusic.MySongSpace.entity.Track;
import com.hyeonmusic.MySongSpace.exception.MemberNotFoundException;
import com.hyeonmusic.MySongSpace.exception.TrackNotFoundException;
import com.hyeonmusic.MySongSpace.repository.LikeRepository;
import com.hyeonmusic.MySongSpace.repository.MemberRepository;
import com.hyeonmusic.MySongSpace.repository.Track.TrackRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.hyeonmusic.MySongSpace.exception.utils.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class LikeService {
    private final MemberRepository memberRepository;
    private final TrackRepository trackRepository;
    private final LikeRepository likeRepository;


    //좋아요 토글 로직
    @Transactional
    public void likeTrack(Long trackId, String memberKey) {
        Member member = memberRepository.findByMemberKey(memberKey)
                .orElseThrow(() -> new MemberNotFoundException(MEMBER_NOT_FOUND));
        Track track = trackRepository.findByIdWithLock(trackId)
                .orElseThrow(() -> new TrackNotFoundException(TRACK_NOT_FOUND));
        if (track.getMember().getMemberId() == member.getMemberId()) {
            throw new IllegalStateException("본인 트랙에는 좋아요를 누를 수 없습니다.");
        }
        Likes existingLike = likeRepository.findByTrackAndMember(track, member);
        if (existingLike != null) {
            track.decreaseLikeCount();//좋아요 수 감소
            likeRepository.delete(existingLike);
        } else {
            Likes like = Likes.createLikes(track, member);
            track.increaseLikeCount(); // 좋아요 수 증가
            likeRepository.save(like);
        }
    }


}

