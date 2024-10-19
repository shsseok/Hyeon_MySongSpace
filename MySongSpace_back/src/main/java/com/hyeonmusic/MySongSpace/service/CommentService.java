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
}
