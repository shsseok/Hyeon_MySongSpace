package com.hyeonmusic.MySongSpace.repository.Comment;

import com.hyeonmusic.MySongSpace.entity.Comment;
import org.springframework.data.domain.Page;


public interface CommentRepositoryCustom {
    Page<Comment> findCommentByTrackId();
}
