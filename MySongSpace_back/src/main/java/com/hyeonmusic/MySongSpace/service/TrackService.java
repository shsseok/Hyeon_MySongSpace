package com.hyeonmusic.MySongSpace.service;

import com.hyeonmusic.MySongSpace.dto.TrackResponseDTO;
import com.hyeonmusic.MySongSpace.dto.TrackUploadDTO;
import com.hyeonmusic.MySongSpace.entity.Genre;
import com.hyeonmusic.MySongSpace.entity.Member;
import com.hyeonmusic.MySongSpace.entity.Mood;
import com.hyeonmusic.MySongSpace.entity.Track;
import com.hyeonmusic.MySongSpace.repository.MemberRepository;
import com.hyeonmusic.MySongSpace.repository.Track.TrackRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TrackService {

    private final TrackRepository trackRepository;
    private final MemberRepository memberRepository;
    private final FileService fileService;

    @Transactional
    public Track uploadTrack(TrackUploadDTO trackUploadDTO) {

        Member member = memberRepository.findById(trackUploadDTO.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        String filePath = fileService.uploadFile(trackUploadDTO.getTrackFile(), "music");
        String coversPath = fileService.uploadFile(trackUploadDTO.getTrackCover(), "covers");
        Track track = Track.createTrack(trackUploadDTO, member, filePath, coversPath);

        return trackRepository.save(track);
    }

    public List<TrackResponseDTO> getAllTracks(int page, String sortBy, List<Mood> moods, List<Genre> genres) {
        // 페이지 크기를 10으로 설정
        Pageable pageable = PageRequest.of(page, 10, Sort.by(getSortDirection(sortBy)).descending());
        // JPQL 메서드를 호출하여 필터링된 트랙을 조회
        Page<Track> trackPage = trackRepository.findTracksWithFilters(moods, genres, sortBy, pageable);
        // Track 엔티티를 TrackResponseDTO로 변환
        return trackPage.stream()
                .map(track -> new TrackResponseDTO(
                        track.getTrackId(),
                        track.getTitle(),
                        track.getDescription(),
                        track.getCoversPath(),
                        track.getFilePath(),
                        track.getDuration(),
                        track.getMember().getUsername(), // Member의 이름을 가져옴
                        track.getGenres(),
                        track.getMoods()
                ))
                .collect(Collectors.toList());
    }

    public String getSortDirection(String sortBy) {
        return sortBy.equals("popular") ? null : "uploadedAt"; // 최신순과 인기순 모두 DESC로 설정
    }

    public TrackResponseDTO getTrackById(Long id) {
        Track track = trackRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Track not found"));

        return new TrackResponseDTO(
                track.getTrackId(),
                track.getTitle(),
                track.getDescription(),
                track.getCoversPath(),
                track.getFilePath(),
                track.getDuration(),
                track.getMember().getUsername(), // Member의 이름을 가져옴
                track.getGenres(),
                track.getMoods()
        );
    }

    @Transactional
    public void deleteTrack(Long id) {
        // ID로 트랙 조회
        Track track = trackRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Track not  found")); // 트랙이 존재하지 않으면 예외 발생
        // 음악 파일 삭제
        fileService.deleteFile(track.getFilePath().substring(1)); // music/ 폴더에 있는 파일 삭제

        // 이미지 커버 파일 삭제
        fileService.deleteFile(track.getCoversPath().substring(1)); // covers/ 폴더에 있는 파일 삭제

        trackRepository.delete(track); // 트랙 삭제
    }

}

