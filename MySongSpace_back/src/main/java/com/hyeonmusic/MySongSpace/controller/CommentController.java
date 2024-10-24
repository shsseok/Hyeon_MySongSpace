package com.hyeonmusic.MySongSpace.controller;


import com.hyeonmusic.MySongSpace.dto.CommentRequestDTO;
import com.hyeonmusic.MySongSpace.dto.CommentResponseDTO;
import com.hyeonmusic.MySongSpace.entity.Comment;
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
    public ResponseEntity<Comment> addComment(
            @PathVariable("trackId") Long trackId,
            @RequestBody CommentRequestDTO commentRequestDTO) {
        Comment comment = commentService.createComment(trackId, commentRequestDTO);
        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }

    // 3. 댓글 조회
    @GetMapping
    public ResponseEntity<List<CommentResponseDTO>> getComments(@PathVariable("trackId") Long trackId) {
        List<CommentResponseDTO> comments = commentService.getComments(trackId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    //댓글 수정
    @PatchMapping("/{commentId}")
    public ResponseEntity<String> updateComment(
            @PathVariable("trackId") Long trackId,
            @PathVariable("commentId") Long commentId,
            @RequestBody String content) {
        commentService.updateComment(commentId, content);
        return new ResponseEntity<>("Comment updated successfully", HttpStatus.OK);

    }

    //댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable("commentId") Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
