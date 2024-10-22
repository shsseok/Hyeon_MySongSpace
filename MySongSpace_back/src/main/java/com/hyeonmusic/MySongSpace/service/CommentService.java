package com.hyeonmusic.MySongSpace.service;

import com.hyeonmusic.MySongSpace.dto.CommentRequestDTO;
import com.hyeonmusic.MySongSpace.dto.CommentResponseDTO;
import com.hyeonmusic.MySongSpace.entity.Comment;
import com.hyeonmusic.MySongSpace.entity.Member;
import com.hyeonmusic.MySongSpace.entity.Track;
import com.hyeonmusic.MySongSpace.repository.Comment.CommentRepository;
import com.hyeonmusic.MySongSpace.repository.MemberRepository;
import com.hyeonmusic.MySongSpace.repository.Track.TrackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final TrackRepository trackRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Comment createComment(Long trackId, CommentRequestDTO commentRequestDTO) {
        // 트랙 조회
        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new IllegalArgumentException("Track not found with id: " + trackId));

        // 사용자 조회
        Member member = memberRepository.findById(commentRequestDTO.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Member not found with id: " + commentRequestDTO.getMemberId()));

        // 부모 댓글이 있으면 조회 (대댓글의 경우)
        Comment parentComment = null;
        if (commentRequestDTO.getParentId() != null) {
            parentComment = commentRepository.findById(commentRequestDTO.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent comment not found with id: " + commentRequestDTO.getParentId()));
        }
        Comment comment = Comment.createComment(commentRequestDTO, track, member, parentComment);
        Comment savedComment = commentRepository.save(comment);
        return savedComment;
    }

    public List<CommentResponseDTO> getComments(Long trackId) {
        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new IllegalArgumentException("Track not found with id: " + trackId));
        List<Comment> comments = track.getComments();
        List<CommentResponseDTO> commentResponseDTOList = new ArrayList<>();
        Map<Long, CommentResponseDTO> commentResponseDTOMap = new HashMap<>();
        comments.forEach(comment -> {
            CommentResponseDTO commentResponseDTO = CommentResponseDTO.convertCommentToDto(comment);
            commentResponseDTOMap.put(comment.getCommentId(), commentResponseDTO);
            if (comment.getParent() == null) {
                commentResponseDTOList.add(commentResponseDTO);
            } else {
                commentResponseDTOMap.get(comment.getParent().getCommentId()).getChildren().add(commentResponseDTO);
            }
        });
        return commentResponseDTOList;
    }

    @Transactional
    public void updateComment(Long commentId, String content) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        comment.updateContent(content);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        commentRepository.delete(comment);
    }

}
