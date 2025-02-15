package com.hyeonmusic.MySongSpace.controller;

import com.hyeonmusic.MySongSpace.dto.track.TrackResponseDTO;
import com.hyeonmusic.MySongSpace.dto.track.TrackUploadDTO;
import com.hyeonmusic.MySongSpace.entity.FilePath;
import com.hyeonmusic.MySongSpace.entity.Genre;
import com.hyeonmusic.MySongSpace.entity.Mood;
import com.hyeonmusic.MySongSpace.service.FileService;
import com.hyeonmusic.MySongSpace.service.TrackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/tracks")
@RequiredArgsConstructor
public class TrackController {

    private final TrackService trackService;
    private final FileService fileService;

    // 1. 트랙 업로드 (POST)
    @PostMapping
    public ResponseEntity<String> saveTrack(@ModelAttribute TrackUploadDTO trackUploadDTO) {
        FilePath filePath = fileService.uploadTrackFileAndTrackCoverFile(trackUploadDTO.getTrackFile(), trackUploadDTO.getTrackCover());
        trackService.saveTrack(trackUploadDTO, filePath);
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

        List<TrackResponseDTO> tracks = trackService.getAllTracks(page, sortBy, moods, genres, keyword);
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
