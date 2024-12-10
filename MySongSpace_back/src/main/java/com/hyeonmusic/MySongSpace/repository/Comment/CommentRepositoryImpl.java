package com.hyeonmusic.MySongSpace.repository.Comment;

import com.hyeonmusic.MySongSpace.entity.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommentRepositoryImpl implements CommentRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public CommentRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Comment> findParentCommentByTrack(Pageable pageable, Track track) {
        QMember qMember = QMember.member;
        QComment qComment = QComment.comment;
        QComment qChildComment = new QComment("childComment");
        QTrack qTrack = QTrack.track;
        List<Comment> content = queryFactory.selectFrom(qComment)
                .join(qComment.member, qMember).fetchJoin()
                .where(qComment.track.eq(track)
                        .and(qComment.parent.isNull()))
                .orderBy(qComment.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory.select(qComment.count())
                .from(qComment)
                .fetchOne();

        return new PageImpl<>(content, pageable, count);

    }
}
