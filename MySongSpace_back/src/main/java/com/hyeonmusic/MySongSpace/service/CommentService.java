package com.hyeonmusic.MySongSpace.service;

import com.hyeonmusic.MySongSpace.dto.CommentRequestDTO;
import com.hyeonmusic.MySongSpace.dto.CommentResponseDTO;
import com.hyeonmusic.MySongSpace.entity.Comment;
import com.hyeonmusic.MySongSpace.entity.Member;
import com.hyeonmusic.MySongSpace.entity.Track;
import com.hyeonmusic.MySongSpace.repository.CommentRepository;
import com.hyeonmusic.MySongSpace.repository.MemberRepository;
import com.hyeonmusic.MySongSpace.repository.TrackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final TrackRepository trackRepository;
    private final MemberRepository memberRepository;
    @Transactional
    public CommentResponseDTO createComment(Long trackId, CommentRequestDTO commentRequestDTO) {
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

        // 댓글 엔티티 생성
        Comment comment = new Comment();
        comment.setContent(commentRequestDTO.getContent());
        comment.setTrack(track);
        comment.setMember(member);
        comment.setParent(parentComment); // 대댓글인 경우 부모 댓글을 설정
        comment.setCreatedAt(LocalDateTime.now());

        // 부모 댓글일 경우에만 별점을 설정
        if (parentComment == null) {
            comment.setRating(commentRequestDTO.getRating());
        } else {
            comment.setRating(null); // 대댓글은 별점이 없음
        }

        // 댓글 저장
        Comment savedComment = commentRepository.save(comment);

        // 응답 DTO 생성
        return new CommentResponseDTO(
                savedComment.getCommentId(),
                savedComment.getContent(),
                savedComment.getTrack().getId(),
                savedComment.getMember().getUsername(),
                savedComment.getRating(),
                savedComment.getCreatedAt(),
                savedComment.getUpdatedAt(),
                savedComment.getParent() != null ? savedComment.getParent().getCommentId() : null // 대댓글일 경우 부모 ID 포함
        );
    }
}
