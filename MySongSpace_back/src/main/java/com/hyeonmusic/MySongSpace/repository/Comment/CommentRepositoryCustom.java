package com.hyeonmusic.MySongSpace.repository.Comment;

import com.hyeonmusic.MySongSpace.entity.Comment;
import com.hyeonmusic.MySongSpace.entity.Track;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface CommentRepositoryCustom {
    Page<Comment> findParentCommentByTrack(Pageable pageable, Track track);


}
