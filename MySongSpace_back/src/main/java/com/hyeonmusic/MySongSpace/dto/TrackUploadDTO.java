package com.hyeonmusic.MySongSpace.dto;

import com.hyeonmusic.MySongSpace.entity.Genre;
import com.hyeonmusic.MySongSpace.entity.Mood;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
public class TrackUploadDTO {
    private String title; // 트랙 제목
    private String description; // 트랙 설명
    private MultipartFile trackCover; // 음악 커버 사진
    private MultipartFile trackFile; // 실제 음악 파일
    private int duration; // 트랙 지속 시간
    private List<Genre> genres; // 장르 목록
    private List<Mood> moods; // 분위기 목록
    private Long memberId; // 업로드한 사용자 ID

    // Getters and Setters
}

