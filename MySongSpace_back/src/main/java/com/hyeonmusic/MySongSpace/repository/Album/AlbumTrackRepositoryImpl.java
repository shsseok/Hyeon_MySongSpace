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
                .offset(pageable.getOffset())  // 페이징 처리를 위해 offset 설정
                .limit(pageable.getPageSize()) // 한 번에 가져올 데이터 수 설정
                .fetch(); // 결과 리스트로 가져오기

        // 총 레코드 수 계산 (페이징 처리를 위해 필요)
        long total = queryFactory
                .selectFrom(albumTrack)
                .where(albumTrack.album.albumId.eq(albumId))
                .fetchCount();

        // PageImpl을 사용하여 Page 객체로 변환
        Page<AlbumTrack> page = new PageImpl<>(albumTracks, pageable, total);

        // Optional로 반환
        return Optional.of(page);
    }

}
