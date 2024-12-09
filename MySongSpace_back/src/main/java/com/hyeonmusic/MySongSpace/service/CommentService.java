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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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

    public List<CommentResponseDTO> getComments(Long trackId, int page) {
        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new TrackNotFoundException(TRACK_NOT_FOUND));
        Pageable pageable = PageRequest.of(page, 10);

        // 부모 댓글 10개 가져오기
        Page<Comment> parentComments = commentRepository.findParentCommentByTrack(pageable, track);
        List<CommentResponseDTO> commentResponseDTOList = new ArrayList<>();

        // 부모 댓글에 대해 처리
        parentComments.forEach(parentComment -> {
            CommentResponseDTO parentDto = CommentResponseDTO.convertCommentToDto(parentComment);
            commentResponseDTOList.add(parentDto);

            // 부모 댓글이 자식 댓글을 가지고 있다면, 자식 댓글을 재귀적으로 추가
            if (!parentComment.getChildren().isEmpty()) {
                addRepliesToParentComment(parentComment, parentDto);
            }
        });

        return commentResponseDTOList;
    }

    private void addRepliesToParentComment(Comment parentComment, CommentResponseDTO parentDto) {
        // 자식 댓글을 재귀적으로 추가
        List<Comment> sortedChildren = parentComment.getChildren().stream()
                .sorted(Comparator.comparing(Comment::getCreatedAt).reversed())
                .collect(Collectors.toList());
        for (Comment child : sortedChildren) {
            CommentResponseDTO childDto = CommentResponseDTO.convertCommentToDto(child);
            parentDto.getChildren().add(childDto); // 부모 DTO에 자식 댓글 추가
            if (!child.getChildren().isEmpty()) {
                addRepliesToParentComment(child, childDto); // 자식 댓글이 또 자식 댓글을 가지고 있다면 재귀 호출
            }
        }
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
