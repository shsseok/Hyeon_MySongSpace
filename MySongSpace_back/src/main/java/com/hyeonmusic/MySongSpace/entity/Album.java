package com.hyeonmusic.MySongSpace.entity;

import com.hyeonmusic.MySongSpace.dto.AlbumRequestDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
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

    // 특정 트랙을 앨범에서 삭제하는 메서드
//    public void removeTrack(Track track) {
//        // 앨범 트랙 리스트에서 해당 트랙과의 관계를 찾아 제거
//        Iterator<AlbumTrack> iterator = albumTracks.iterator();
//        while (iterator.hasNext()) {
//            AlbumTrack albumTrack = iterator.next();
//            if (albumTrack.getTrack().equals(track)) {
//                iterator.remove(); // 리스트에서 제거
//                albumTrack.setAlbum(null); // AlbumTrack에서 Album과의 관계 제거
//                albumTrack.setTrack(null); // AlbumTrack에서 Track과의 관계 제거
//                break; // 첫 번째 일치하는 트랙만 제거하고 반복 종료
//            }
//        }
//    }
}

