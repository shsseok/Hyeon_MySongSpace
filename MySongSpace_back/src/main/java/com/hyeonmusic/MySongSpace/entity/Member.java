package com.hyeonmusic.MySongSpace.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;
    private String username;
    private String email;
    private String nickname;
    private String password;
    private String profilePicture;
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "member") // mappedBy도 변경
    private List<Track> tracks;

    @OneToMany(mappedBy = "member") // mappedBy도 변경
    private List<Album> albums;

    // 필요한 경우 getter와 setter 추가
}

