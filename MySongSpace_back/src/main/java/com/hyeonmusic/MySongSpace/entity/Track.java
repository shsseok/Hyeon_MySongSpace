package com.hyeonmusic.MySongSpace.entity;

import com.hyeonmusic.MySongSpace.dto.track.TrackUploadDTO;
import jakarta.persistence.*;
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
    private int likeCount;
    private LocalDateTime uploadedAt; // 업로드 시간

    @OneToMany(mappedBy = "track", cascade = CascadeType.ALL)
    private List<TrackGenre> genres = new ArrayList<>();
    @OneToMany(mappedBy = "track", cascade = CascadeType.ALL)
    private List<TrackMood> moods = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 업로드한 사용자

    // 앨범과의 관계를 관리하기 위한 리스트
    @OneToMany(mappedBy = "track", cascade = CascadeType.ALL)
    private List<AlbumTrack> albumTracks = new ArrayList<>();

    @OneToMany(mappedBy = "track")
    private List<Comment> comments = new ArrayList<>();
    // 댓글 목록

    @OneToMany(mappedBy = "track")
    private List<Likes> likes = new ArrayList<>();

    public Track(String title, String description, String musicPath, String coverPath, int duration, List<Genre> genres, List<Mood> moods, Member member) {
        this.title = title;
        this.description = description;
        this.musicPath = musicPath;
        this.coverPath = coverPath;
        this.duration = duration;
        this.genres = TrackGenre.createTrackGenreList(this, genres);
        this.moods = TrackMood.createTrackMoodList(this, moods);
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
        track.genres = TrackGenre.createTrackGenreList(track, trackUploadDTO.getGenres());
        track.moods = TrackMood.createTrackMoodList(track, trackUploadDTO.getMoods());
        track.uploadedAt = LocalDateTime.now();
        track.member = member; // 업로드한 사용자를 설정
        return track;
    }

    //비즈니스 로직

    public void increaseLikeCount() {
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        this.likeCount--;
    }
}


