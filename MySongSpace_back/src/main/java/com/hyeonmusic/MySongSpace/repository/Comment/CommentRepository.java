package com.hyeonmusic.MySongSpace.repository.Comment;

import com.hyeonmusic.MySongSpace.entity.Comment;
import com.hyeonmusic.MySongSpace.entity.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {
    @Query("SELECT c FROM Comment c " +
            "JOIN FETCH c.member cm " +
            "WHERE c.track = :track AND c.parent IS NOT NULL " +
            "ORDER BY c.depth ASC, c.createdAt desc")
    List<Comment> findChildCommentByTrack(@Param("track") Track track);




}
