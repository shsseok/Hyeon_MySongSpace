package com.hyeonmusic.MySongSpace.service;

import com.hyeonmusic.MySongSpace.dto.AlbumRequestDTO;
import com.hyeonmusic.MySongSpace.dto.AlbumResponseDTO;
import com.hyeonmusic.MySongSpace.dto.TrackResponseDTO;
import com.hyeonmusic.MySongSpace.entity.Album;
import com.hyeonmusic.MySongSpace.entity.AlbumTrack;
import com.hyeonmusic.MySongSpace.entity.Member;
import com.hyeonmusic.MySongSpace.entity.Track;
import com.hyeonmusic.MySongSpace.repository.Album.AlbumRepository;
import com.hyeonmusic.MySongSpace.repository.Album.AlbumTrackRepository;
import com.hyeonmusic.MySongSpace.repository.MemberRepository;
import com.hyeonmusic.MySongSpace.repository.Track.TrackRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlbumService {


    private final AlbumTrackRepository albumTrackRepository;
    private final AlbumRepository albumRepository;
    private final MemberRepository memberRepository; // 사용자를 찾기 위해 필요
    private final TrackRepository trackRepository;

    @Transactional
    public void createAlbum(AlbumRequestDTO albumRequestDTO) {
        Member member = memberRepository.findById(albumRequestDTO.getMemberId())
                .orElseThrow(() -> new RuntimeException("Member not found"));
        boolean albumExists = member.getAlbums().stream().anyMatch(album ->
                albumRequestDTO.getTitle().equals(album.getTitle()));
        if (albumExists) {
            throw new RuntimeException("An album with the same title already exists");
        }

        Album album = Album.createAlbum(albumRequestDTO, member);
        albumRepository.save(album);
    }

    @Transactional
    public void addTrackToAlbum(Long trackId, Long albumId) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new RuntimeException("Album not found"));
        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new RuntimeException("Track not found"));
        // 앨범의 AlbumTrack 리스트에서 트랙이 이미 있는지 객체 비교로 확인
        boolean exists = album.getAlbumTracks().stream()
                .anyMatch(albumTrack -> albumTrack.getTrack().equals(track)); // 객체로 비교

        if (exists) {
            throw new RuntimeException("Track is already in the album");
        }

        AlbumTrack albumTrack = AlbumTrack.createAlbumTrack();
        //연관관계 메서드
        albumTrack.setAlbum(album);
        albumTrack.setTrack(track);
    }

    @Transactional
    public void deleteAlbum(Long albumId) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new RuntimeException("Album not found"));
        albumRepository.delete(album);
    }

    @Transactional
    public void removeTrackFromAlbum(Long albumId, Long trackId) {
        int deletedCount = albumTrackRepository.deleteByAlbumIdAndTrackId(albumId, trackId);
        if (deletedCount == 0) {
            throw new RuntimeException("Track not found in the album or album does not exist.");
        }
    }

    @Transactional
    public void updateComment(Long albumId, String title) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new RuntimeException("Album not found"));
        album.updateTitle(title);
    }

    public List<AlbumResponseDTO> getAllAlbumsByUser(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        return member.getAlbums().stream().map(album -> new AlbumResponseDTO(
                album.getAlbumId(),
                album.getTitle(),
                album.getAlbumTracks().size(),
                album.getCreatedAt()
        )).collect(Collectors.toList());
    }


    public Page<TrackResponseDTO> getTracksByAlbum(Long albumId, int page) {
        // 1. 페이지 요청 생성 (한 페이지에 10개의 트랙을 가져옴)
        Pageable pageable = PageRequest.of(page, 10);

        // 2. Repository를 통해 앨범 트랙 가져오기
        Page<AlbumTrack> albumTracksPage = albumTrackRepository.findAlbumTracksByAlbumId(pageable, albumId)
                .orElseThrow(() -> new EntityNotFoundException("Album not found for id: " + albumId));

        // 3. AlbumTrack에서 TrackResponseDTO로 변환
        Page<TrackResponseDTO> trackResponseDTOPage = albumTracksPage.map(albumTrack -> {
            Track track = albumTrack.getTrack(); // AlbumTrack에서 Track 가져오기

            // Track을 TrackResponseDTO로 변환
            return new TrackResponseDTO(
                    track.getTrackId(),
                    track.getTitle(),
                    track.getDescription(), // 추가된 description 필드
                    track.getCoversPath(), // 커버 경로 가져오기
                    track.getFilePath(), // 트랙 파일 경로 가져오기
                    track.getDuration(), // 트랙 길이
                    albumTrack.getAlbum().getMember().getNickname(), // 앨범 트랙의 멤버 이름 가져오기
                    track.getGenres(), // Genre 리스트
                    track.getMoods() // Mood 리스트
            );
        });

        // 4. 변환된 페이지 반환
        return trackResponseDTOPage;
    }


}
