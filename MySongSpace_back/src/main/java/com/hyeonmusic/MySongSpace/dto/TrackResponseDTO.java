package com.hyeonmusic.MySongSpace.dto;

import com.hyeonmusic.MySongSpace.entity.Genre;
import com.hyeonmusic.MySongSpace.entity.Mood;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

}
