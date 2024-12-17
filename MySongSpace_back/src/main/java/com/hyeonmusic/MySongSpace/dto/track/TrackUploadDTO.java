package com.hyeonmusic.MySongSpace.dto.track;

import com.hyeonmusic.MySongSpace.entity.Genre;
import com.hyeonmusic.MySongSpace.entity.Mood;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
public class TrackUploadDTO {
    @NotNull(message = "트랙 제목은 필수입니다.")
    private String title; // 트랙 제목

    @NotNull(message = "트랙 설명은 필수입니다.")
    private String description; // 트랙 설명

    @NotNull(message = "트랙 커버는 필수입니다.")
    private MultipartFile trackCover; // 음악 커버 사진

    @NotNull(message = "트랙 파일은 필수입니다.")
    private MultipartFile trackFile; // 실제 음악 파일

    @NotNull(message = "트랙 지속 시간은 필수입니다.")
    private int duration; // 트랙 지속 시간

    @NotEmpty(message = "장르는 필수입니다.")
    private List<Genre> genres; // 장르 목록

    @NotEmpty(message = "분위기는 필수입니다.")
    private List<Mood> moods; // 분위기 목록

    @NotNull(message = "회원 ID는 필수입니다.")
    private Long memberId; // 업로드한 사용자 ID
}

