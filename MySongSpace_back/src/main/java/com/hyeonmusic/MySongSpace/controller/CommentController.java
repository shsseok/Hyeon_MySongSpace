package com.hyeonmusic.MySongSpace.controller;


import com.hyeonmusic.MySongSpace.dto.CommentRequestDTO;
import com.hyeonmusic.MySongSpace.dto.CommentResponseDTO;
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
    public ResponseEntity<CommentResponseDTO> addComment(
            @PathVariable Long trackId,
            @RequestBody CommentRequestDTO commentRequestDTO) {
        CommentResponseDTO createdComment = commentService.createComment(trackId, commentRequestDTO);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    // 2. 대댓글 작성
    @PostMapping("/{parentId}/reply")
    public ResponseEntity<CommentResponseDTO> addReply(
            @PathVariable Long trackId,
            @PathVariable Long parentId,
            @RequestBody CommentRequestDTO commentRequestDTO) {
        CommentResponseDTO createdReply = commentService.addReply(trackId, parentId, commentRequestDTO);
        return new ResponseEntity<>(createdReply, HttpStatus.CREATED);
    }

    // 3. 댓글 조회
    @GetMapping
    public ResponseEntity<List<CommentResponseDTO>> getComments(@PathVariable Long trackId) {
        List<CommentResponseDTO> comments = commentService.getCommentsByTrackId(trackId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    // 4. 댓글 수정
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDTO> updateComment(
            @PathVariable Long trackId,
            @PathVariable Long commentId,
            @RequestBody CommentRequestDTO commentRequestDTO) {
        CommentResponseDTO updatedComment = commentService.updateComment(commentId, commentRequestDTO);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }

    // 5. 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long trackId,
            @PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
