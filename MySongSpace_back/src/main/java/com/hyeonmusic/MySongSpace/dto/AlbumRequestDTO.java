package com.hyeonmusic.MySongSpace.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class AlbumRequestDTO {
    @NotNull(message = "제목을 필수로 입력해주세요.") // 제목 필수
    private String title;   // 앨범 제목

    @NotNull(message = "회원 ID는 필수입니다.") // 사용자 ID 필수
    private Long memberId;  // 생성하는 사용자의 ID
}
