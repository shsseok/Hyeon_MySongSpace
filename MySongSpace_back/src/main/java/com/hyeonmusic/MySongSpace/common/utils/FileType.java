package com.hyeonmusic.MySongSpace.common.utils;

import java.util.List;

public enum FileType {
    MUSIC("music", List.of("audio/mpeg", "audio/wav", "audio/ogg", "audio/flac", "audio/aac")),
    COVERS("covers", List.of("image/png", "image/jpeg", "image/gif", "image/webp", "image/bmp", "image/svg+xml"));

    private final String type;
    private final List<String> mimeTypes;

    FileType(String type, List<String> mimeTypes) {
        this.type = type;
        this.mimeTypes = mimeTypes;
    }

    public String getType() {

        return type;
    }

    public List<String> getMimeTypes() {
        return mimeTypes;
    }
}

