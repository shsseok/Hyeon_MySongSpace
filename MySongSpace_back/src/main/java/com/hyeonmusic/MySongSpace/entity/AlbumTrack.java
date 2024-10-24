package com.hyeonmusic.MySongSpace.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class AlbumTrack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long AlbumTrackId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id") // Album 외래 키
    private Album album;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "track_id") // Track 외래 키
    private Track track;

    public static AlbumTrack createAlbumTrack() {
        AlbumTrack albumTrack = new AlbumTrack();
        return albumTrack;
    }

    public void setAlbum(Album album) {
        // 현재 album이 null이 아니면 기존의 AlbumTrack 리스트에서 제거
        if (this.album != null) {
            this.album.getAlbumTracks().remove(this);
        }

        this.album = album;

        // 새로운 album이 null이 아니면 AlbumTrack 리스트에 추가
        if (album != null) {
            album.getAlbumTracks().add(this);
        }

    }

    public void setTrack(Track track) {
        // 현재 album이 null이 아니면 기존의 AlbumTrack 리스트에서 제거
        if (this.track != null) {
            this.track.getAlbumTracks().remove(this);
        }

        this.track = track;

        // 새로운 album이 null이 아니면 AlbumTrack 리스트에 추가
        if (track != null) {
            track.getAlbumTracks().add(this);
        }
    }


}

