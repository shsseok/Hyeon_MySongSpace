package com.hyeonmusic.MySongSpace.controller;

import com.hyeonmusic.MySongSpace.dto.album.AlbumRequestDTO;
import com.hyeonmusic.MySongSpace.dto.album.AlbumResponseDTO;
import com.hyeonmusic.MySongSpace.dto.track.TrackResponseDTO;
import com.hyeonmusic.MySongSpace.service.AlbumService;
import lombok.RequiredArgsConstructor;
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
        return ResponseEntity.ok("앨범 생성에 성공하셨습니다.");
    }

    //앨범에 트랙 넣기
    @PostMapping("/{albumId}/tracks/{trackId}")
    public ResponseEntity<String> addTrackToAlbum(@PathVariable("trackId") Long trackId,
                                                  @PathVariable("albumId") Long albumId) {
        albumService.addTrackToAlbum(trackId, albumId);
        return ResponseEntity.ok("앨범에 트랙이 성공적으로 추가 되었습니다.");
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
    public ResponseEntity<List<TrackResponseDTO>> getTracksByAlbum(
            @PathVariable("albumId") Long albumId,
            @RequestParam(defaultValue = "0", value = "page") int page
    ) {
        List<TrackResponseDTO> tracksByAlbum = albumService.getTracksByAlbum(albumId, page);
        return ResponseEntity.ok(tracksByAlbum);
    }

    //앨범 삭제
    // --> 그냥 앨범 자체를 통으로 삭제
    @DeleteMapping("/{albumId}")
    public ResponseEntity<String> deleteAlbum(@PathVariable("albumId") Long albumId) {
        albumService.deleteAlbum(albumId);
        return ResponseEntity.ok("앨범 삭제가 성공적으로 완료 되었습니다.");
    }

    // --> 앨범에 들어가 있는 트랙 삭제
    @DeleteMapping("/{albumId}/tracks/{trackId}")
    public ResponseEntity<String> removeTrackFromAlbum(@PathVariable("albumId") Long albumId, @PathVariable("trackId") Long trackId) {
        albumService.removeTrackFromAlbum(albumId, trackId);
        return ResponseEntity.ok("앨범트랙이 삭제가 되었습니다.");
    }

    //앨범 수정 (제목)
    // --> 앨범 제목 수정
    @PatchMapping("/{albumId}")
    public ResponseEntity<String> updateAlbum(
            @PathVariable("albumId") Long albumId,
            @RequestBody String title) {
        albumService.updateComment(albumId, title);
        return ResponseEntity.ok("앨범 제목이 수정되었습니다.");

    }

}
