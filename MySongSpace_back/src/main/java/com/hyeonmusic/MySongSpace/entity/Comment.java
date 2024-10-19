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

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "track_id")
    private Track track;

    private Integer rating; // 별점 (대댓글에는 null로 설정)

    public static Comment createComment(CommentRequestDTO commentRequestDTO, Track track, Member member, Comment parent) {
        Comment comment = new Comment();
        comment.content = commentRequestDTO.getContent();
        comment.track = track; // 댓글이 속한 트랙 설정
        comment.member = member; // 댓글을 작성한 사용자 설정
        comment.parent = parent; // 부모 댓글 설정 (대댓글인 경우)
        comment.createdAt = LocalDateTime.now(); // 생성 시간 설정
        comment.rating= commentRequestDTO.getRating();

        return comment;
    }
}

