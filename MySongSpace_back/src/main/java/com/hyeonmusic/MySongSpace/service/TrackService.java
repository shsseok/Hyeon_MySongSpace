package com.hyeonmusic.MySongSpace.service;

import com.hyeonmusic.MySongSpace.dto.track.TrackResponseDTO;
import com.hyeonmusic.MySongSpace.dto.track.TrackUploadDTO;
import com.hyeonmusic.MySongSpace.entity.*;
import com.hyeonmusic.MySongSpace.exception.MemberNotFoundException;
import com.hyeonmusic.MySongSpace.exception.TrackNotFoundException;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
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
    public void saveTrack(TrackUploadDTO trackUploadDTO, FilePath filePath) {

        Member member = memberRepository.findById(trackUploadDTO.getMemberId())
                .orElseThrow(() -> new MemberNotFoundException(MEMBER_NOT_FOUND));

        Track track = Track.createTrack(trackUploadDTO, member, filePath.getMusicPath(), filePath.getMusicPath());
        trackRepository.save(track);

    }

    @Transactional
    public void deleteTrack(Long id) {
        Track track = trackRepository.findById(id)
                .orElseThrow(() -> new TrackNotFoundException(TRACK_NOT_FOUND));
        fileService.deleteFile(track.getFilePath().getMusicPath().substring(1));
        fileService.deleteFile(track.getFilePath().getCoverPath().substring(1));
        trackRepository.delete(track);
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

}

