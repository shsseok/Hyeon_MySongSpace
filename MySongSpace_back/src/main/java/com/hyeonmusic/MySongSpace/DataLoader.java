package com.hyeonmusic.MySongSpace;


import com.hyeonmusic.MySongSpace.entity.Genre;
import com.hyeonmusic.MySongSpace.entity.Member;
import com.hyeonmusic.MySongSpace.entity.Mood;
import com.hyeonmusic.MySongSpace.entity.Track;
import com.hyeonmusic.MySongSpace.repository.MemberRepository;
import com.hyeonmusic.MySongSpace.repository.Track.TrackRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class DataLoader {
    private final TrackRepository trackRepository;
    private final MemberRepository memberRepository;

    @PostConstruct
    public void init() {
        // Member 객체 생성
        Member member = new Member("username", "email@example.com", "nickname", "password", "profilePicture");
        memberRepository.save(member); // Member 저장

        // 초기 데이터를 추가
        List<Track> tracks = generateTracks(member);
        trackRepository.saveAll(tracks); // 생성한 Track 리스트를 저장

        System.out.println("초기 데이터 로드 완료");
    }

    public static List<Track> generateTracks(Member member) {
        return IntStream.range(0, 100) // 0부터 99까지의 숫자 생성
                .mapToObj(i -> createTrack(member, i)) // 각 숫자에 대해 트랙 생성
                .collect(Collectors.toList()); // 리스트로 수집
    }

    private static Track createTrack(Member member, int index) {
        String title = "Track Title " + index; // 제목 설정
        String description = "This is a description for track " + index; // 설명 설정
        int duration = (int) (Math.random() * 300); // 랜덤 길이 설정 (0-300초)
        List<Genre> genres = getRandomGenres(); // 랜덤 장르 설정
        List<Mood> moods = getRandomMoods(); // 랜덤 무드 설정

        String filePath = "/tracks/track" + index + ".mp3"; // 파일 경로 설정
        String coversPath = "/covers/cover" + index + ".jpg"; // 커버 사진 경로 설정

        // Track 객체 생성 및 반환
        return new Track(title, description, filePath, coversPath, duration, genres, moods, member);
    }


    // 랜덤 장르를 선택하는 메서드
    private static List<Genre> getRandomGenres() {
        Random random = new Random();
        int numGenres = random.nextInt(3) + 1; // 1개에서 3개까지 선택
        List<Genre> genres = new ArrayList<>();

        while (genres.size() < numGenres) { // 선택된 장르의 수가 numGenres에 도달할 때까지 반복
            int randomIndex = random.nextInt(Genre.values().length); // 랜덤 인덱스 선택
            Genre selectedGenre = Genre.values()[randomIndex]; // 랜덤 장르 선택
            if (!genres.contains(selectedGenre)) { // 중복 추가 방지
                genres.add(selectedGenre); // 랜덤 장르 추가
            }
        }

        return genres; // 선택된 장르 반환
    }

    // 랜덤 무드를 선택하는 메서드
    private static List<Mood> getRandomMoods() {
        Random random = new Random();
        int numMoods = random.nextInt(3) + 1; // 1개에서 3개까지 선택
        List<Mood> moods = new ArrayList<>();

        while (moods.size() < numMoods) { // 선택된 무드의 수가 numMoods에 도달할 때까지 반복
            int randomIndex = random.nextInt(Mood.values().length); // 랜덤 인덱스 선택
            Mood selectedMood = Mood.values()[randomIndex]; // 랜덤 무드 선택
            if (!moods.contains(selectedMood)) { // 중복 추가 방지
                moods.add(selectedMood); // 랜덤 무드 추가
            }
        }
        return moods;
    }
}
