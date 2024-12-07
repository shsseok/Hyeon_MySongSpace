package com.hyeonmusic.MySongSpace.controller;

import com.hyeonmusic.MySongSpace.dto.TrackResponseDTO;
import com.hyeonmusic.MySongSpace.dto.TrackUploadDTO;
import com.hyeonmusic.MySongSpace.entity.Genre;
import com.hyeonmusic.MySongSpace.entity.Mood;
import com.hyeonmusic.MySongSpace.entity.Track;
import com.hyeonmusic.MySongSpace.service.TrackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tracks")
@RequiredArgsConstructor
public class TrackController {

    private final TrackService trackService;

    // 1. 트랙 업로드 (POST)
    @PostMapping
    public ResponseEntity<String> uploadTrack(@ModelAttribute TrackUploadDTO trackUploadDTO) {
        trackService.uploadTrack(trackUploadDTO);
        return ResponseEntity.ok("트랙 업로드 성공");
    }

    // 2. 모든 트랙 조회 (GET)
    @GetMapping
    public ResponseEntity<List<TrackResponseDTO>> getAllTracks(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "sortBy", defaultValue = "latest") String sortBy,
            @RequestParam(value = "moods", required = false) List<Mood> moods,
            @RequestParam(value = "genres", required = false) List<Genre> genres,
            @RequestParam(value = "search", required = false) String keyword) {

        List<TrackResponseDTO> tracks = trackService.getAllTracks(page,sortBy,moods,genres,keyword);
        return ResponseEntity.ok(tracks);
    }

    // 3. 특정 트랙 조회 (GET)
    @GetMapping("/{trackId}")
    public ResponseEntity<TrackResponseDTO> getTrackById(@PathVariable Long trackId) {
        TrackResponseDTO track = trackService.getTrackById(trackId);
        return ResponseEntity.ok(track);
    }



    // 5. 트랙 삭제 (DELETE)
    @DeleteMapping("/{trackId}")
    public ResponseEntity<String> deleteTrack(@PathVariable Long trackId) {
        trackService.deleteTrack(trackId);
        return ResponseEntity.ok("트랙 삭제 성공");
    }
}
