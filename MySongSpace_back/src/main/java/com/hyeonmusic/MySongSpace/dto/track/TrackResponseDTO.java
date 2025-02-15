package com.hyeonmusic.MySongSpace.dto.track;

import com.hyeonmusic.MySongSpace.entity.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class TrackResponseDTO {
    private Long trackId;
    private String title;
    private String description;
    private String trackCoverPath;
    private String trackFilePath;
    private int duration;
    private String memberName; // 업로드한 멤버 이름
    private List<Genre> genres;
    private List<Mood> moods;

    // 생성자
    public TrackResponseDTO(Long trackId, String title, String description, String trackCoverPath, String trackFilePath,
                            int duration, String memberName, List<Genre> genres, List<Mood> moods) {
        this.trackId = trackId;
        this.title = title;
        this.description = description;
        this.trackCoverPath = trackCoverPath;
        this.trackFilePath = trackFilePath;
        this.duration = duration;
        this.memberName = memberName;
        this.genres = genres;
        this.moods = moods;
    }

    public static TrackResponseDTO toResponse(Track track) {
        List<Genre> genreList = extractGenres(track);
        List<Mood> moodList = extractMoods(track);

        return new TrackResponseDTO(
                track.getTrackId(),
                track.getTitle(),
                track.getDescription(),
                track.getFilePath().getCoverPath(),
                track.getFilePath().getMusicPath(),
                track.getDuration(),
                track.getMember().getUsername(),
                genreList,
                moodList
        );
    }


    private static List<Genre> extractGenres(Track track) {
        return track.getGenres().stream()
                .map(TrackGenre::getGenre)
                .collect(Collectors.toList());
    }

    private static List<Mood> extractMoods(Track track) {
        return track.getMoods().stream()
                .map(TrackMood::getMood)
                .collect(Collectors.toList());
    }

}
