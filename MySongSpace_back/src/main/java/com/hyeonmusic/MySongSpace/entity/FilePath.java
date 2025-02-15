package com.hyeonmusic.MySongSpace.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;



@Getter
@NoArgsConstructor
@Embeddable
public class FilePath {
    private String musicPath;
    private String coverPath;

    public FilePath(String musicPath, String coverPath) {
        this.musicPath = musicPath;
        this.coverPath = coverPath;
    }


}
