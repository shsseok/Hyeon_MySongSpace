package com.hyeonmusic.MySongSpace.entity;

import com.hyeonmusic.MySongSpace.dto.album.AlbumRequestDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long albumId;

    private String title;

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 앨범과 트랙 간의 관계를 관리하기 위한 리스트
    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL)
    private List<AlbumTrack> albumTracks = new ArrayList<>(); // 중간 엔티티 리스트

    public static Album createAlbum(AlbumRequestDTO albumRequestDTO, Member member) {
        Album album = new Album();
        album.title = albumRequestDTO.getTitle();
        album.createdAt = LocalDateTime.now();
        album.member = member;
        return album;
    }
    public void updateTitle(String title) {
        this.title = title;
    }

}

