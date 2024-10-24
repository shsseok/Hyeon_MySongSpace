package com.hyeonmusic.MySongSpace.repository.Album;

import com.hyeonmusic.MySongSpace.entity.AlbumTrack;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AlbumTrackRepositoryCustom {

    Optional<Page<AlbumTrack>> findAlbumTracksByAlbumId(Pageable pageable, @Param("albumId") Long albumId);
}
