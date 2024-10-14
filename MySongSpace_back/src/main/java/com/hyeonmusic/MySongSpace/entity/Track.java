package com.hyeonmusic.MySongSpace.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Track {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trackId;

    private String title;
    private String artist;
    private String filePath;
    private int duration;
    private LocalDateTime uploadedAt;

    @ElementCollection(targetClass = Genre.class)
    @CollectionTable(name = "track_genres", joinColumns = @JoinColumn(name = "track_id"))
    @Enumerated(EnumType.STRING)
    private List<Genre> genres = new ArrayList<>(); // 여러 개의 장르 관리

    @ElementCollection(targetClass = Mood.class)
    @CollectionTable(name = "track_moods", joinColumns = @JoinColumn(name = "track_id"))
    @Enumerated(EnumType.STRING)
    private List<Mood> moods = new ArrayList<>(); // 여러 개의 분위기 관리
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "album_id")
    private Album album;

    @OneToMany(mappedBy = "track")
    private List<Comment> comments;

    @OneToMany(mappedBy = "track")
    private List<Like> likes;

    // Getters and Setters
}
