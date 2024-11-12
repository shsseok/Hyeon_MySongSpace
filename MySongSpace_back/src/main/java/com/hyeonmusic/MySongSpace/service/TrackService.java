package com.hyeonmusic.MySongSpace.service;

import com.hyeonmusic.MySongSpace.dto.TrackResponseDTO;
import com.hyeonmusic.MySongSpace.dto.TrackUploadDTO;
import com.hyeonmusic.MySongSpace.entity.Genre;
import com.hyeonmusic.MySongSpace.entity.Member;
import com.hyeonmusic.MySongSpace.entity.Mood;
import com.hyeonmusic.MySongSpace.entity.Track;
import com.hyeonmusic.MySongSpace.exception.FileUploadException;
import com.hyeonmusic.MySongSpace.exception.MemberNotFoundException;
import com.hyeonmusic.MySongSpace.exception.TrackNotFoundException;
import com.hyeonmusic.MySongSpace.exception.utils.ErrorCode;
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

import static com.hyeonmusic.MySongSpace.exception.utils.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TrackService {

    private final TrackRepository trackRepository;
    private final MemberRepository memberRepository;
    private final FileService fileService;


    @Transactional
    public void uploadTrack(TrackUploadDTO trackUploadDTO) {

        Member member = memberRepository.findById(trackUploadDTO.getMemberId())
                .orElseThrow(() -> new MemberNotFoundException(MEMBER_NOT_FOUND));
        String musicPath = null;
        String coverPath = null;
        try {
            musicPath = fileService.uploadFile(trackUploadDTO.getTrackFile(), "music");
            coverPath = fileService.uploadFile(trackUploadDTO.getTrackCover(), "covers");
            Track track = Track.createTrack(trackUploadDTO, member, musicPath, coverPath);
        } catch (Exception e) {
            // 이미 업로드된 파일이 있을 경우 삭제 시도
            if (musicPath != null) {
                fileService.deleteFile(musicPath.substring(1));
            }
            if (coverPath != null) {
                fileService.deleteFile(coverPath.substring(1));
            }
            throw new FileUploadException(FILE_UPLOAD_FAILED);
        }
    }

    public List<TrackResponseDTO> getAllTracks(int page, String sortBy, List<Mood> moods, List<Genre> genres) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(getSortDirection(sortBy)).descending());
        Page<Track> trackPage = trackRepository.findTracksWithFilters(moods, genres, sortBy, pageable);
        return trackPage.stream()
                .map(track -> new TrackResponseDTO(
                        track.getTrackId(),
                        track.getTitle(),
                        track.getDescription(),
                        track.getCoverPath(),
                        track.getMusicPath(),
                        track.getDuration(),
                        track.getMember().getUsername(),
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
                .orElseThrow(() -> new TrackNotFoundException(TRACK_NOT_FOUND));

        return new TrackResponseDTO(
                track.getTrackId(),
                track.getTitle(),
                track.getDescription(),
                track.getCoverPath(),
                track.getMusicPath(),
                track.getDuration(),
                track.getMember().getUsername(),
                track.getGenres(),
                track.getMoods()
        );
    }

    @Transactional
    public void deleteTrack(Long id) {
        // ID로 트랙 조회
        Track track = trackRepository.findById(id)
                .orElseThrow(() -> new TrackNotFoundException(TRACK_NOT_FOUND)); // 트랙이 존재하지 않으면 예외 발생
        fileService.deleteFile(track.getMusicPath().substring(1)); // music/ 폴더에 있는 파일 삭제
        fileService.deleteFile(track.getCoverPath().substring(1)); // covers/ 폴더에 있는 파일 삭제
        trackRepository.delete(track); // 트랙 삭제
    }

}

