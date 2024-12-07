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
            trackRepository.save(track);
        } catch (Exception e) {
            // 이미 업로드된 파일이 있을 경우 삭제 시도
            deleteIfFileUploaded(musicPath, coverPath);
            throw new FileUploadException(FILE_UPLOAD_FAILED);
        }
    }

    @Transactional
    public void deleteTrack(Long id) {
        // ID로 트랙 조회
        Track track = trackRepository.findById(id)
                .orElseThrow(() -> new TrackNotFoundException(TRACK_NOT_FOUND));
        fileService.deleteFile(track.getMusicPath().substring(1));
        fileService.deleteFile(track.getCoverPath().substring(1));
        trackRepository.delete(track); // 트랙 삭제
    }

    public List<TrackResponseDTO> getAllTracks(int page, String sortBy, List<Mood> moods, List<Genre> genres, String keyword) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(getSortDirection(sortBy)).descending());
        Page<Track> trackPage = trackRepository.findTracksWithFilters(moods, genres, sortBy, keyword, pageable);
        return trackPage.stream()
                .map(track -> TrackResponseDTO.toResponse(track))
                .collect(Collectors.toList());
    }

    public TrackResponseDTO getTrackById(Long id) {
        Track track = trackRepository.findById(id)
                .orElseThrow(() -> new TrackNotFoundException(TRACK_NOT_FOUND));

        return TrackResponseDTO.toResponse(track);
    }

    private String getSortDirection(String sortBy) {
        return sortBy.equals("popular") ? "likeCount" : "uploadedAt"; // 최신순과 인기순 모두 DESC로 설정
    }

    private void deleteIfFileUploaded(String musicPath, String coverPath) {
        if (musicPath != null) fileService.deleteFile(musicPath.substring(1));
        if (coverPath != null) fileService.deleteFile(coverPath.substring(1));
    }

}

