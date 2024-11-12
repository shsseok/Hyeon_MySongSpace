package com.hyeonmusic.MySongSpace.repository.Album;

import com.hyeonmusic.MySongSpace.entity.Album;
import com.hyeonmusic.MySongSpace.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AlbumRepository extends JpaRepository<Album, Long> {

    @Query("SELECT a FROM Album a " +
            " LEFT JOIN FETCH a.member m " +
            " LEFT JOIN FETCH a.albumTracks " +
            "WHERE a.member = :member")
    Optional<List<Album>> findAlbumByMember(@Param("member") Member member);


}

