package com.hyeonmusic.MySongSpace.entity;

import com.hyeonmusic.MySongSpace.dto.comment.CommentRequestDTO;
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

    private Integer depth;

    //최상위 댓글 고유 식별자
    private Long rootId;

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

    public static Comment createComment(CommentRequestDTO commentRequestDTO, Track track, Member member, Comment parent, Long rootId) {
        Comment comment = new Comment();
        comment.content = commentRequestDTO.getContent();
        comment.track = track;
        comment.member = member;
        comment.parent = parent;
        comment.rootId = rootId;
        comment.depth = comment.createDepth(parent);
        comment.createdAt = LocalDateTime.now();
        comment.updatedAt = LocalDateTime.now();
        comment.rating = comment.determineRating(commentRequestDTO, parent);
        return comment;
    }

    public Integer determineRating(CommentRequestDTO commentRequestDTO, Comment parent) {
        return (parent == null) ? commentRequestDTO.getRating() : null;
    }

    private Integer createDepth(Comment parent) {
        return parent == null ? 0 : parent.getDepth() + 1;
    }

    public void updateContent(String content) {
        this.content = content;
        this.updatedAt = LocalDateTime.now();
    }

}

