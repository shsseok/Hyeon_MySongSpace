package com.hyeonmusic.MySongSpace.service;

import com.hyeonmusic.MySongSpace.dto.album.AlbumRequestDTO;
import com.hyeonmusic.MySongSpace.dto.album.AlbumResponseDTO;
import com.hyeonmusic.MySongSpace.dto.track.TrackResponseDTO;
import com.hyeonmusic.MySongSpace.entity.Album;
import com.hyeonmusic.MySongSpace.entity.AlbumTrack;
import com.hyeonmusic.MySongSpace.entity.Member;
import com.hyeonmusic.MySongSpace.entity.Track;
import com.hyeonmusic.MySongSpace.exception.*;
import com.hyeonmusic.MySongSpace.repository.Album.AlbumRepository;
import com.hyeonmusic.MySongSpace.repository.Album.AlbumTrackRepository;
import com.hyeonmusic.MySongSpace.repository.MemberRepository;
import com.hyeonmusic.MySongSpace.repository.Track.TrackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.hyeonmusic.MySongSpace.exception.utils.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlbumService {


    private final AlbumTrackRepository albumTrackRepository;
    private final AlbumRepository albumRepository;
    private final MemberRepository memberRepository;
    private final TrackRepository trackRepository;

    @Transactional
    public void createAlbum(AlbumRequestDTO albumRequestDTO) {
        Member member = memberRepository.findById(albumRequestDTO.getMemberId())
                .orElseThrow(() -> new MemberNotFoundException(MEMBER_NOT_FOUND));
        boolean albumExists = member.getAlbums().stream().anyMatch(album ->
                albumRequestDTO.getTitle().equals(album.getTitle()));
        if (albumExists) {
            throw new AlbumNameDuplicateException(ALBUM_NAME_DUPLICATE);
        }

        Album album = Album.createAlbum(albumRequestDTO, member);
        albumRepository.save(album);
    }

    @Transactional
    public void addTrackToAlbum(Long trackId, Long albumId) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new AlbumNotFoundException(ALBUM_NOT_FOUND));
        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new TrackNotFoundException(TRACK_NOT_FOUND));
        // 앨범의 AlbumTrack 리스트에서 트랙이 이미 있는지 객체 비교로 확인
        boolean exists = album.getAlbumTracks().stream()
                .anyMatch(albumTrack -> albumTrack.getTrack().equals(track)); // 객체로 비교

        if (exists) {
            throw new AlbumNameDuplicateException(ALBUM_NAME_DUPLICATE);
        }

        AlbumTrack albumTrack = AlbumTrack.createAlbumTrack();
        //연관관계 메서드
        albumTrack.setAlbum(album);
        albumTrack.setTrack(track);
    }

    @Transactional
    public void deleteAlbum(Long albumId) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new AlbumNotFoundException(ALBUM_NOT_FOUND));
        albumRepository.delete(album);
    }

    @Transactional
    public void removeTrackFromAlbum(Long albumId, Long trackId) {
        int deletedCount = albumTrackRepository.deleteByAlbumIdAndTrackId(albumId, trackId);
        if (deletedCount == 0) {
            throw new TrackNotFoundInAlbumException(TRACK_NOT_FOUND_IN_ALBUM);
        }
    }

    @Transactional
    public void updateComment(Long albumId, String title) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new AlbumNotFoundException(ALBUM_NOT_FOUND));
        album.updateTitle(title);
    }

    public List<AlbumResponseDTO> getAllAlbumsByUser(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(MEMBER_NOT_FOUND));
        List<Album> albums = albumRepository.findAlbumByMember(member)
                .orElseThrow(() -> new AlbumNotFoundException(ALBUM_NOT_FOUND));
        return albums.stream()
                .map(album -> new AlbumResponseDTO(
                        album.getAlbumId(),
                        album.getTitle(),
                        album.getAlbumTracks().size(),
                        album.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }


    public List<TrackResponseDTO> getTracksByAlbum(Long albumId, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<AlbumTrack> albumTracksPage = albumTrackRepository.findAlbumTracksByAlbumId(pageable, albumId)
                .orElseThrow(() -> new AlbumNotFoundException(ALBUM_NOT_FOUND));

        return albumTracksPage.stream().map(albumTrack -> {
            Track track = albumTrack.getTrack();
            return TrackResponseDTO.toResponse(track);
        }).collect(Collectors.toList());
    }


}
