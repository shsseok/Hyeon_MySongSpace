package com.hyeonmusic.MySongSpace.controller;


import com.hyeonmusic.MySongSpace.dto.comment.CommentRequestDTO;
import com.hyeonmusic.MySongSpace.dto.comment.CommentResponseDTO;
import com.hyeonmusic.MySongSpace.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tracks/{trackId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 1. 댓글 작성
    @PostMapping
    public ResponseEntity<String> addComment(
            @PathVariable("trackId") Long trackId,
            @RequestBody CommentRequestDTO commentRequestDTO) {
        commentService.createComment(trackId, commentRequestDTO);
        return ResponseEntity.ok("댓글을 작성하였습니다");
    }

    // 3. 댓글 조회
    @GetMapping
    public ResponseEntity<List<CommentResponseDTO>> getComments(@PathVariable("trackId") Long trackId,
                                                                @RequestParam(defaultValue = "0") int page) {
        List<CommentResponseDTO> comments = commentService.getComments(trackId, page);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    //댓글 수정
    @PatchMapping("/{commentId}")
    public ResponseEntity<String> updateComment(
            @PathVariable("trackId") Long trackId,
            @PathVariable("commentId") Long commentId,
            @RequestBody String content) {
        commentService.updateComment(commentId, content);
        return ResponseEntity.ok("댓글을 수정을 성공 하였습니다");

    }

    //댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable("commentId") Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok("댓글을 삭제를 성공 하였습니다");
    }
}
