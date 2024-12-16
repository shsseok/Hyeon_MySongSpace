package com.hyeonmusic.MySongSpace.repository.Track;

import com.hyeonmusic.MySongSpace.entity.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
public class TrackRepositoryImpl implements TrackRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QTrack qTrack = QTrack.track;
    private final QMember qMember = QMember.member;
    private final QTrackMood qTrackMood = QTrackMood.trackMood;
    private final QTrackGenre qTrackGenre = QTrackGenre.trackGenre;

    public TrackRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Track> findTracksWithFilters(List<Mood> moods, List<Genre> genres, String sortBy, String keyword, Pageable pageable) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        addMoodsFilter(booleanBuilder, moods);
        addGenresFilter(booleanBuilder, genres);
        addKeywordFilter(booleanBuilder, keyword);
        // 트랙 목록 쿼리 실행
        List<Track> content = queryFactory.selectFrom(qTrack)
                .join(qTrack.member, qMember).fetchJoin()
                .where(booleanBuilder)
                .orderBy(orderMethod(sortBy))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(qTrack.count())
                .from(qTrack)
                .where(booleanBuilder);

        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetchOne());
    }

    private OrderSpecifier<?> orderMethod(String sortBy) {
        if (sortBy.equals("popular")) {
            return qTrack.likeCount.desc();
        }
        return qTrack.uploadedAt.desc();
    }

    private BooleanBuilder addMoodsFilter(BooleanBuilder booleanBuilder, List<Mood> moods) {
        if (moods != null && !moods.isEmpty()) {
            booleanBuilder.and(qTrack.trackId.in(
                    JPAExpressions.select(qTrackMood.track.trackId)
                            .from(qTrackMood)
                            .where(qTrackMood.mood.in(moods))
                            .groupBy(qTrackMood.track.trackId)
                            .having(qTrackMood.mood.countDistinct().eq((long) moods.size()))));

        }
        return booleanBuilder;
    }

    private BooleanBuilder addGenresFilter(BooleanBuilder booleanBuilder, List<Genre> genres) {
        if (genres != null && !genres.isEmpty()) {
            booleanBuilder.and(qTrack.trackId.in(
                    JPAExpressions.select(qTrackGenre.track.trackId)
                            .from(qTrackGenre)
                            .where(qTrackGenre.genre.in(genres))
                            .groupBy(qTrackGenre.track.trackId)
                            .having(qTrackGenre.genre.countDistinct().eq((long) genres.size()))));
        }
        return booleanBuilder;
    }

    private BooleanBuilder addKeywordFilter(BooleanBuilder booleanBuilder, String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            booleanBuilder.and(
                    Expressions.booleanTemplate(
                            "function('match_against', {0}, {1}, {2}) > 0",
                            qTrack.title,
                            qTrack.description,
                            keyword
                    )
            );
        }
        return booleanBuilder;
    }


}
