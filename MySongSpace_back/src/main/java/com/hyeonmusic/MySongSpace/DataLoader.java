package com.hyeonmusic.MySongSpace;


import com.hyeonmusic.MySongSpace.entity.*;
import com.hyeonmusic.MySongSpace.repository.MemberRepository;
import com.hyeonmusic.MySongSpace.repository.Track.TrackRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
public class DataLoader {
    private final TrackRepository trackRepository;
    private final MemberRepository memberRepository;
    private final DataSource dataSource;
    @PostConstruct
    public void init() {
        // Member 객체 생성
        Member member = new Member("username", "email@example.com", "profilePicture.jpg", "memberKey12345123", Role.USER);
        memberRepository.save(member); // Member 저장

        // 초기 데이터를 추가
        List<Track> tracks = generateTracks(member);
        trackRepository.saveAll(tracks); // 생성한 Track 리스트를 저장
//        try {
//            saveTracksInBatch(tracks); // 배치로 저장
//        } catch (SQLException e) {
//            throw new RuntimeException("Batch insert failed", e);
//        }
//        System.out.println("초기 데이터 로드 완료");
    }

    public static List<Track> generateTracks(Member member) {
        return IntStream.range(0,1000) // 0부터 99까지의 숫자 생성
                .mapToObj(i -> createTrack(member, i)) // 각 숫자에 대해 트랙 생성
                .collect(Collectors.toList()); // 리스트로 수집
    }

    private static Track createTrack(Member member, int index) {
        String title = generateTitle(index); // 제목 설정
        String description = generateDescription(index); // 설명 설정
        int duration = (int) (Math.random() * 300); // 랜덤 길이 설정 (0-300초)
        List<Genre> genres = getRandomGenres(); // 랜덤 장르 설정
        List<Mood> moods = getRandomMoods(); // 랜덤 무드 설정

        String filePath = "/tracks/track" + index + ".mp3"; // 파일 경로 설정
        String coversPath = "/covers/cover" + index + ".jpg"; // 커버 사진 경로 설정

        // Track 객체 생성 및 반환
        return new Track(title, description, filePath, coversPath, duration, genres, moods, member);
    }

    private static String generateTitle(int index) {
        // 제목에 키워드를 추가하여 검색이 용이하도록
        String[] keywords = {"Love", "Adventure", "Mystery", "Hope", "Dream"};
        String keyword = keywords[index % keywords.length];
        return keyword + " Symphony " + index;
    }

    private static String generateDescription(int index) {
        // 설명에 키워드와 컨텍스트를 추가하여 검색 가능성 증가
        String[] topics = {
                "A journey through emotions.",
                "An uplifting melody of hope.",
                "An adventurous soundscape.",
                "A mysterious and thrilling harmony.",
                "A dreamlike tune to inspire."
        };
        String topic = topics[index % topics.length];
        return "Track " + index + ": " + topic + " Perfect for relaxation and exploration.";
    }
    private void saveTracksInBatch(List<Track> tracks) throws SQLException {
        String sql = "INSERT INTO track (title, description, music_path, cover_path, duration, member_id, like_count, uploaded_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            int batchSize = 1000;
            int count = 0;

            for (Track track : tracks) {
                statement.setString(1, track.getTitle());
                statement.setString(2, track.getDescription());
                statement.setString(3, track.getMusicPath());
                statement.setString(4, track.getCoverPath());
                statement.setInt(5, track.getDuration());
                statement.setLong(6, track.getMember().getMemberId());
                statement.setLong(7, 0); // like_count 초기값 설정
                statement.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now())); // uploaded_at 현재 시간 설정
                statement.addBatch(); // 배치 추가

                if (++count % batchSize == 0) {
                    statement.executeBatch(); // 배치 실행
                }
            }
            statement.executeBatch(); // 남은 데이터 실행
        }
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
