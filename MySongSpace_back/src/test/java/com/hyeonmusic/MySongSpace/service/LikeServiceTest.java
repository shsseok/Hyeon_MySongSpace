package com.hyeonmusic.MySongSpace.service;

import com.hyeonmusic.MySongSpace.entity.*;
import com.hyeonmusic.MySongSpace.repository.LikeRepository;
import com.hyeonmusic.MySongSpace.repository.MemberRepository;
import com.hyeonmusic.MySongSpace.repository.Track.TrackRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class LikeServiceTest {

    @Autowired
    private LikeService likeService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private LikeRepository likeRepository;
    private Member member;
    private Track track;
    private Member testMember;

    @BeforeEach
    void setUp() {
        String title = "Test Track";
        String description = "This is a test track.";
        String musicPath = "/music/test.mp3";
        String coverPath = "/covers/test.jpg";
        int duration = 300; // 5분
        List<Genre> genres = List.of(Genre.POP, Genre.ROCK);
        List<Mood> moods = List.of(Mood.HAPPY);
        member = new Member("테스트", "test1@naver.com", "프로필 사진", "memberkey1234", Role.USER);
        memberRepository.save(member);
        track = new Track(title, description, musicPath, coverPath, duration, genres, moods, member);
        trackRepository.save(track);
        List<Member> members = IntStream.rangeClosed(1, 100)
                .mapToObj(i -> new Member("심현석" + i, "test" + i + "@naver.com", "프로필 사진" + i, "memberKey" + i, Role.USER))
                .collect(Collectors.toList());
        memberRepository.saveAll(members);

    }

    @DisplayName("100명의 사용자가 동시에 100개의 요청을 보낸다면")
    @Test
    void LikeMulti() throws InterruptedException {
        //given
        int count = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch countDownLatch = new CountDownLatch(count);
        //when
        for (int i = 1; i <= count; i++) {
            final String memberkey = "memberKey" + i;
            executorService.submit(() -> {
                try {
                    likeService.likeTrack(track.getTrackId(), memberkey);
                }finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        //then
        Track findTrack = trackRepository.findById(track.getTrackId()).get();
        assertThat(findTrack.getLikeCount()).isEqualTo(100);
    }


}
