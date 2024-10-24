package com.hyeonmusic.MySongSpace.controller;

import com.hyeonmusic.MySongSpace.dto.AlbumRequestDTO;
import com.hyeonmusic.MySongSpace.dto.AlbumResponseDTO;
import com.hyeonmusic.MySongSpace.dto.TrackResponseDTO;
import com.hyeonmusic.MySongSpace.service.AlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/albums")
@RequiredArgsConstructor
public class AlbumController {


    private final AlbumService albumService;

    //앨범 생성
    @PostMapping
    public ResponseEntity<String> createAlbum(@RequestBody AlbumRequestDTO albumRequestDTO) {
        albumService.createAlbum(albumRequestDTO);
        return ResponseEntity.ok("성공입니다");
    }

    //앨범에 트랙 넣기
    @PostMapping("/{albumId}/tracks/{trackId}")
    public ResponseEntity<String> addTrackToAlbum(@PathVariable("trackId") Long trackId,
                                                  @PathVariable("albumId") Long albumId) {
        albumService.addTrackToAlbum(trackId, albumId);
        return ResponseEntity.ok("Track added to album successfully");
    }

    //앨범 조회
    // --> 사용자가 가지고 있는 모든 앨범 조회
    @GetMapping("/user/{memberId}")
    public ResponseEntity<List<AlbumResponseDTO>> getAllAlbumsByUser(@PathVariable("memberId") Long memberId) {
        List<AlbumResponseDTO> albums = albumService.getAllAlbumsByUser(memberId);
        return ResponseEntity.ok(albums);
    }

    // --> 단건 앨범에 들어가 있는 모든 트랙 조회
    @GetMapping("/{albumId}/tracks")
    public ResponseEntity<Page<TrackResponseDTO>> getTracksByAlbum(
            @PathVariable("albumId") Long albumId,
            @RequestParam(defaultValue = "0", value = "page") int page
    ) {
        Page<TrackResponseDTO> tracks = albumService.getTracksByAlbum(albumId, page);
        return ResponseEntity.ok(tracks);
    }

    //앨범 삭제
    // --> 그냥 앨범 자체를 통으로 삭제
    @DeleteMapping("/{albumId}")
    public ResponseEntity<String> deleteAlbum(@PathVariable("albumId") Long albumId) {
        albumService.deleteAlbum(albumId);
        return ResponseEntity.ok("Album deleted successfully.");
    }

    // --> 앨범에 들어가 있는 트랙 삭제
    @DeleteMapping("/{albumId}/tracks/{trackId}")
    public ResponseEntity<String> removeTrackFromAlbum(@PathVariable("albumId") Long albumId, @PathVariable("trackId") Long trackId) {
        albumService.removeTrackFromAlbum(albumId, trackId); // 앨범과 트랙의 연관관계 삭제 서비스 호출
        return ResponseEntity.ok("Track removed from album successfully.");
    }

    //앨범 수정 (제목)
    // --> 앨범 제목 수정
    @PatchMapping("/{albumId}")
    public ResponseEntity<String> updateAlbum(
            @PathVariable("albumId") Long albumId,
            @RequestBody String title) {
        albumService.updateComment(albumId, title);
        return new ResponseEntity<>("Album updated successfully", HttpStatus.OK);

    }

}
