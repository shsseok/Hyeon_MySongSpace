package com.hyeonmusic.MySongSpace.service;

import com.hyeonmusic.MySongSpace.dto.CommentRequestDTO;
import com.hyeonmusic.MySongSpace.dto.CommentResponseDTO;
import com.hyeonmusic.MySongSpace.entity.Comment;
import com.hyeonmusic.MySongSpace.entity.Member;
import com.hyeonmusic.MySongSpace.entity.Track;
import com.hyeonmusic.MySongSpace.exception.CommentNotFoundException;
import com.hyeonmusic.MySongSpace.exception.MemberNotFoundException;
import com.hyeonmusic.MySongSpace.exception.TrackNotFoundException;
import com.hyeonmusic.MySongSpace.exception.utils.ErrorCode;
import com.hyeonmusic.MySongSpace.repository.Comment.CommentRepository;
import com.hyeonmusic.MySongSpace.repository.MemberRepository;
import com.hyeonmusic.MySongSpace.repository.Track.TrackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hyeonmusic.MySongSpace.exception.utils.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final TrackRepository trackRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void createComment(Long trackId, CommentRequestDTO commentRequestDTO) {

        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new TrackNotFoundException(TRACK_NOT_FOUND));

        Member member = memberRepository.findById(commentRequestDTO.getMemberId())
                .orElseThrow(() -> new MemberNotFoundException(MEMBER_NOT_FOUND));
        Comment parentComment = getParentComment(commentRequestDTO.getParentId());
        Comment comment = Comment.createComment(commentRequestDTO, track, member, parentComment);
        commentRepository.save(comment);

    }

    @Transactional
    public void updateComment(Long commentId, String content) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(COMMENT_NOT_FOUND));
        comment.updateContent(content);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(COMMENT_NOT_FOUND));
        commentRepository.delete(comment);
    }
    public List<CommentResponseDTO> getComments(Long trackId) {
        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new TrackNotFoundException(TRACK_NOT_FOUND));
        List<CommentResponseDTO> commentResponseDTOList = new ArrayList<>();
        Map<Long, CommentResponseDTO> commentResponseDTOMap = new HashMap<>();
        track.getComments().forEach(comment -> {
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

    //부모 댓글 가져오는 메소드
    private Comment getParentComment(Long parentId) {
        if (parentId != null) {
            return commentRepository.findById(parentId)
                    .orElseThrow(() -> new CommentNotFoundException(PARENT_COMMENT_NOT_FOUND));
        }
        return null;
    }
}
