package com.hyeonmusic.MySongSpace.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hyeonmusic.MySongSpace.entity.Comment;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class CommentResponseDTO {
    private Long commentId;
    private Long parentId;
    private String content;
    private String writer;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int rating;

    private LocalDateTime createDate; // 업로드 날짜 필드 추가

    private List<CommentResponseDTO> children = new ArrayList<>();

    public CommentResponseDTO(Long commentId, String content, String writer, LocalDateTime createDate, Long parentId) {
        this.commentId = commentId;
        this.parentId = parentId;
        this.content = content;
        this.writer = writer;
        this.createDate = createDate;
    }

    public CommentResponseDTO(Long commentId, String content, String writer, int rating, LocalDateTime createDate, Long parentId) {
        this.commentId = commentId;
        this.parentId = parentId;
        this.content = content;
        this.writer = writer;
        this.rating = rating;
        this.createDate = createDate;
    }

    public static CommentResponseDTO convertCommentToDto(Comment comment) {
        return comment.getParent() == null ?
                new CommentResponseDTO(comment.getCommentId(),
                        comment.getContent(),
                        comment.getMember().getUsername(),
                        comment.getRating(),
                        comment.getCreatedAt(),
                        null) :
                new CommentResponseDTO(comment.getCommentId(),
                        comment.getContent(),
                        comment.getMember().getUsername(),
                        comment.getCreatedAt(),
                        comment.getParent().getCommentId());
    }
}
