package com.hyeonmusic.MySongSpace.repository;

import com.hyeonmusic.MySongSpace.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
