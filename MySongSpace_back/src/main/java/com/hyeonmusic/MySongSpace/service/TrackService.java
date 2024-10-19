package com.hyeonmusic.MySongSpace.service;

import com.amazonaws.services.s3.AmazonS3;
import com.hyeonmusic.MySongSpace.dto.TrackResponseDTO;
import com.hyeonmusic.MySongSpace.dto.TrackUploadDTO;
import com.hyeonmusic.MySongSpace.entity.Member;
import com.hyeonmusic.MySongSpace.entity.Track;
import com.hyeonmusic.MySongSpace.repository.MemberRepository;
import com.hyeonmusic.MySongSpace.repository.TrackRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
        Track track = Track.createTrack(trackUploadDTO,member,filePath,coversPath);

        return trackRepository.save(track);
    }

    public List<TrackResponseDTO> getAllTracks() {

        return trackRepository.findAll().stream()
                .map(track -> new TrackResponseDTO(
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

    public TrackResponseDTO getTrackById(Long id) {
        Track track = trackRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Track not found"));

        return new TrackResponseDTO(
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

