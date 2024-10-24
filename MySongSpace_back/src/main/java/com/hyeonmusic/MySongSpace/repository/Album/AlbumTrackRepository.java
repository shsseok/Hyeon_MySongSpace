package com.hyeonmusic.MySongSpace.repository.Album;

import com.hyeonmusic.MySongSpace.entity.AlbumTrack;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AlbumTrackRepository extends JpaRepository<AlbumTrack, Long>, AlbumTrackRepositoryCustom {
    @Modifying
    @Query("DELETE FROM AlbumTrack at WHERE at.album.albumId = :albumId AND at.track.trackId = :trackId")
    int deleteByAlbumIdAndTrackId(@Param("albumId") Long albumId, @Param("trackId") Long trackId);
}
