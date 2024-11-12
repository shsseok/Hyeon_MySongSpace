package com.hyeonmusic.MySongSpace.repository.Track;

import com.hyeonmusic.MySongSpace.entity.*;
import com.hyeonmusic.MySongSpace.repository.Track.TrackRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TrackRepositoryImpl implements TrackRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public TrackRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Track> findTracksWithFilters(List<Mood> moods, List<Genre> genres, String sortBy, Pageable pageable) {
        QTrack qTrack = QTrack.track;
        QMember qMember = QMember.member;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        // moods 필터 적용
        if (moods != null && !moods.isEmpty()) {
            for (Mood mood : moods) {
                booleanBuilder.and(qTrack.moods.contains(mood)); // AND 조건
            }
        }
        // genres 필터 적용
        if (genres != null && !genres.isEmpty()) {
            for (Genre genre : genres) {
                booleanBuilder.and(qTrack.genres.contains(genre)); // AND 조건
            }
        }
        // 쿼리 실행 및 로그 확인
        System.out.println("Generated Query: " + booleanBuilder.toString());
        // 트랙 목록 쿼리 실행
        List<Track> content = queryFactory.selectFrom(qTrack)
                .join(qTrack.member, qMember)
                .fetchJoin()
                .where(booleanBuilder)
                .orderBy(qTrack.uploadedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 총 카운트 쿼리 실행
        JPAQuery<Long> countQuery = queryFactory.select(qTrack.count())
                .from(qTrack)
                .where(booleanBuilder);

        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetchOne());
    }
}
