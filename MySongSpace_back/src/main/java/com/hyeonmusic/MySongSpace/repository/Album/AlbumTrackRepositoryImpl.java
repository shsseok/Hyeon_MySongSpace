package com.hyeonmusic.MySongSpace.repository.Album;

import com.hyeonmusic.MySongSpace.entity.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AlbumTrackRepositoryImpl implements AlbumTrackRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public AlbumTrackRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    QAlbumTrack albumTrack = QAlbumTrack.albumTrack;
    QTrack track = QTrack.track;
    QMember member = QMember.member;
    QAlbum album = QAlbum.album;

    @Override
    public Optional<Page<AlbumTrack>> findAlbumTracksByAlbumId(Pageable pageable, Long albumId) {
        List<AlbumTrack> albumTracks = queryFactory
                .select(albumTrack)
                .from(albumTrack)
                .join(albumTrack.album, album).fetchJoin()
                .join(album.member, member).fetchJoin()
                .join(albumTrack.track, track).fetchJoin()
                .where(albumTrack.album.albumId.eq(albumId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        long total = queryFactory
                .selectFrom(albumTrack)
                .where(albumTrack.album.albumId.eq(albumId))
                .fetchCount();

        Page<AlbumTrack> page = new PageImpl<>(albumTracks, pageable, total);


        return Optional.of(page);
    }

}
