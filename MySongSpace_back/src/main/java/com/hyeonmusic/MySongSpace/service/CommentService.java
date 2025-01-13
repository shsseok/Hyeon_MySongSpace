package com.hyeonmusic.MySongSpace.service;

import com.hyeonmusic.MySongSpace.dto.comment.CommentRequestDTO;
import com.hyeonmusic.MySongSpace.dto.comment.CommentResponseDTO;
import com.hyeonmusic.MySongSpace.entity.Comment;
import com.hyeonmusic.MySongSpace.entity.Member;
import com.hyeonmusic.MySongSpace.entity.Track;
import com.hyeonmusic.MySongSpace.exception.CommentNotFoundException;
import com.hyeonmusic.MySongSpace.exception.MemberNotFoundException;
import com.hyeonmusic.MySongSpace.exception.TrackNotFoundException;
import com.hyeonmusic.MySongSpace.repository.Comment.CommentRepository;
import com.hyeonmusic.MySongSpace.repository.MemberRepository;
import com.hyeonmusic.MySongSpace.repository.Track.TrackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        Long rootId = getRootIdIfExists(commentRequestDTO.getRootId());
        Comment comment = Comment.createComment(commentRequestDTO, track, member, parentComment, rootId);
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

        //최상위 댓글 10개 가져오기
        Page<Comment> rootComments = commentRepository.findParentCommentByTrack(pageable, track);
        List<CommentResponseDTO> commentResponseDTOList = new ArrayList<>();
        HashMap<Long, CommentResponseDTO> parentMap = new HashMap<>();
        rootComments.forEach(rootComment -> {
            CommentResponseDTO parentCommentDTO = CommentResponseDTO.convertCommentToDto(rootComment);
            commentResponseDTOList.add(parentCommentDTO);
            parentMap.put(rootComment.getCommentId(), parentCommentDTO);
        });
        //최상위 댓글 Id 리스트 뽑아오기
        List<Long> rootIds = getRootIds(rootComments);
        List<Comment> childComments = commentRepository.findChildCommentByRootIds(track, rootIds);

        childComments.forEach(childComment -> {
            CommentResponseDTO parentDTO = parentMap.get(childComment.getParent().getCommentId());
            CommentResponseDTO childDTO = CommentResponseDTO.convertCommentToDto(childComment);
            parentDTO.getChildren().add(childDTO);
            parentMap.put(childComment.getCommentId(), childDTO);
        });

        return commentResponseDTOList;
    }

    //최상위 댓글 고유 식별자 가져오는 메소드
    private List<Long> getRootIds(Page<Comment> rootComments) {
        List<Long> rootIds = rootComments.getContent().stream()
                .map(Comment::getCommentId)
                .collect(Collectors.toList());
        return rootIds;
    }


    //부모 댓글 가져오는 메소드
    private Comment getParentComment(Long parentId) {
        if (parentId != null) {
            return commentRepository.findById(parentId)
                    .orElseThrow(() -> new CommentNotFoundException(PARENT_COMMENT_NOT_FOUND));
        }
        return null;
    }

    //최상위 댓글이 존재하는지 안하는지 판단 후 rootId 반환
    private Long getRootIdIfExists(Long rootId) {
        //만약에 최상위 댓글 Id 가 null 이거나 최상위 댓글이 실제로 존재하지 않으면
        if (rootId == null || !commentRepository.existsById(rootId)) {
            return null;
        }
        return rootId;
    }


}
