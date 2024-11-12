package com.hyeonmusic.MySongSpace.entity;

import com.hyeonmusic.MySongSpace.dto.TrackUploadDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor
public class Track {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trackId; // 트랙 ID
    private String coverPath; //이미지 경로
    private String title; // 트랙 제목
    private String description; // 트랙 설명
    private String musicPath; // 트랙 오디오 파일 경로
    private int duration; // 트랙 지속 시간 (초 단위)
    private LocalDateTime uploadedAt; // 업로드 시간

    @ElementCollection(targetClass = Genre.class)
    @CollectionTable(name = "track_genres", joinColumns = @JoinColumn(name = "track_id"))
    @Enumerated(EnumType.STRING)
    @BatchSize(size = 10)
    private List<Genre> genres = new ArrayList<>(); // 여러 개의 장르 관리

    @ElementCollection(targetClass = Mood.class)
    @CollectionTable(name = "track_moods", joinColumns = @JoinColumn(name = "track_id"))
    @Enumerated(EnumType.STRING)
    @BatchSize(size = 10)
    private List<Mood> moods = new ArrayList<>(); // 여러 개의 분위기 관리

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 업로드한 사용자

    // 앨범과의 관계를 관리하기 위한 리스트
    @OneToMany(mappedBy = "track" , cascade = CascadeType.ALL)
    private List<AlbumTrack> albumTracks = new ArrayList<>(); // 중간 엔티티 리스트

    @OneToMany(mappedBy = "track")
    private List<Comment> comments; // 댓글 목록

    @OneToMany(mappedBy = "track")
    private List<Likes> likes; // 좋아요 목록

    public Track(String title, String description, String musicPath, String coverPath, int duration, List<Genre> genres, List<Mood> moods, Member member) {
        this.title = title;
        this.description = description;
        this.musicPath = musicPath;
        this.coverPath = coverPath;
        this.duration = duration;
        this.genres = genres;
        this.moods = moods;
        this.uploadedAt = LocalDateTime.now();
        this.member = member;
    }


    public static Track createTrack(TrackUploadDTO trackUploadDTO, Member member, String filePath, String coverPath) {
        Track track = new Track();
        track.title = trackUploadDTO.getTitle();
        track.description = trackUploadDTO.getDescription();
        track.musicPath = filePath;
        track.coverPath = coverPath;
        track.duration = trackUploadDTO.getDuration();
        track.genres = trackUploadDTO.getGenres();
        track.moods = trackUploadDTO.getMoods();
        track.uploadedAt = LocalDateTime.now();
        track.member = member; // 업로드한 사용자를 설정
        return track;
    }


}


