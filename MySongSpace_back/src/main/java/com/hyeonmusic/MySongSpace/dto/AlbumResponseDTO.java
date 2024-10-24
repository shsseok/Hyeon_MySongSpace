package com.hyeonmusic.MySongSpace.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class AlbumResponseDTO {
    private Long albumId;
    private String title;
    private int trackCount;
    private LocalDateTime createdAt;

    public AlbumResponseDTO(Long albumId, String title, int trackCount, LocalDateTime createdAt) {
        this.albumId = albumId;
        this.title = title;
        this.trackCount = trackCount;
        this.createdAt = createdAt;
    }
}
