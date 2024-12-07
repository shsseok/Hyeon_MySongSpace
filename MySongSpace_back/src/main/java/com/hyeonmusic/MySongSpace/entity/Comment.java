package com.hyeonmusic.MySongSpace.entity;

import com.hyeonmusic.MySongSpace.dto.CommentRequestDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(nullable = false)
    @Lob
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "track_id")
    private Track track;

    private Integer rating; // 별점 (대댓글에는 null로 설정)

    public static Comment createComment(CommentRequestDTO commentRequestDTO, Track track, Member member, Comment parent) {
        Comment comment = new Comment();
        comment.content = commentRequestDTO.getContent();
        comment.track = track;
        comment.member = member;
        comment.parent = parent;
        comment.createdAt = LocalDateTime.now();
        comment.updatedAt = LocalDateTime.now();
        if (parent == null) {
            comment.rating = commentRequestDTO.getRating();
        } else {
            comment.rating = null;
        }
        return comment;
    }

    public void updateContent(String content) {
        this.content = content;
        this.updatedAt = LocalDateTime.now();
    }

}

