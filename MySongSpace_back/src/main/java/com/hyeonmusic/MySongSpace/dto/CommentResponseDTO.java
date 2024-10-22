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
    private String content;
    private String writer;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int rating;

    private LocalDateTime createDate; // 업로드 날짜 필드 추가

    private List<CommentResponseDTO> children = new ArrayList<>();

    public CommentResponseDTO(Long commentId, String content, String writer, LocalDateTime createDate) {
        this.commentId = commentId;
        this.content = content;
        this.writer = writer;
        this.createDate = createDate;
    }

    public CommentResponseDTO(Long commentId, String content, String writer, int rating, LocalDateTime createDate) {
        this.commentId = commentId;
        this.content = content;
        this.writer = writer;
        this.rating = rating;
        this.createDate = createDate;
    }

    public static CommentResponseDTO convertCommentToDto(Comment comment) {
        // 부모가 없는 경우와 있는 경우에 따라 DTO 생성
        return comment.getParent() == null ?
                new CommentResponseDTO(comment.getCommentId(),
                        comment.getContent(),
                        comment.getMember().getNickname(),
                        comment.getRating(),
                        comment.getCreatedAt()) :
                new CommentResponseDTO(comment.getCommentId(),
                        comment.getContent(),
                        comment.getMember().getNickname(),
                        comment.getCreatedAt());
    }
}
