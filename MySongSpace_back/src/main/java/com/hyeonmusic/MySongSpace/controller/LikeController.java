package com.hyeonmusic.MySongSpace.controller;

import com.hyeonmusic.MySongSpace.service.LikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/track")
@RequiredArgsConstructor
@Slf4j
public class LikeController {

    private final LikeService likeService;

    // 좋아요 토글 API
    @PostMapping("/{trackId}/like")
    public ResponseEntity<String> likeTrack(@PathVariable Long trackId,
                                            @AuthenticationPrincipal UserDetails userDetails) {

        likeService.likeTrack(trackId, userDetails.getUsername());
        return ResponseEntity.ok("좋아요 여부 성공");
    }

}

