package com.hyeonmusic.MySongSpace.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDTO {
    @NotNull(message = "댓글 내용을 입력해야 합니다.") // 댓글 내용은 반드시 필요
    @Size(min = 1, message = "댓글 내용은 최소 1자 이상이어야 합니다.")
    private String content; // 댓글 내용

    private Long memberId;

    private Long parentId; // 대댓글일 경우, 부모 댓글 ID (최상위 댓글의 경우 null 가능)

    private Integer rating; // 별점 (최상위 댓글에만 필요, 대댓글은 null일 수 있음)
}
